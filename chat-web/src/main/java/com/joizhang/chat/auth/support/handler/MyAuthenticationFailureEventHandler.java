package com.joizhang.chat.auth.support.handler;

import cn.hutool.core.util.StrUtil;
import com.joizhang.chat.admin.api.entity.SysLog;
import com.joizhang.chat.common.core.constant.CommonConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.core.util.SpringContextHolder;
import com.joizhang.chat.common.core.util.WebUtils;
import com.joizhang.chat.common.log.event.SysLogEvent;
import com.joizhang.chat.common.log.util.LogTypeEnum;
import com.joizhang.chat.common.log.util.SysLogUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆失败处理
 *
 * @author lengleng
 */
@Slf4j
public class MyAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

    private final MappingJackson2HttpMessageConverter errorHttpResponseConverter =
            new MappingJackson2HttpMessageConverter();

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     */
    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {
        String username = request.getParameter(OAuth2ParameterNames.USERNAME);

        log.info("用户：{} 登录失败，异常：{}", username, exception.getLocalizedMessage());
        SysLog logVo = SysLogUtils.getSysLog();
        logVo.setTitle("登录失败");
        logVo.setType(LogTypeEnum.ERROR.getType());
        logVo.setException(exception.getLocalizedMessage());
        // 发送异步日志事件
        String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
        if (StrUtil.isNotBlank(startTimeStr)) {
            Long startTime = Long.parseLong(startTimeStr);
            Long endTime = System.currentTimeMillis();
            logVo.setTime(endTime - startTime);
        }
        logVo.setServiceId(WebUtils.getClientId());
        logVo.setCreateBy(username);
        logVo.setUpdateBy(username);
        SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        // 写出错误信息
        sendErrorResponse(request, response, exception);
    }

    private void sendErrorResponse(HttpServletRequest request,
                                   HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        String errorMessage;
        if (exception instanceof OAuth2AuthenticationException) {
            OAuth2AuthenticationException authorizationException = (OAuth2AuthenticationException) exception;
            errorMessage = StrUtil.isBlank(authorizationException.getError().getDescription())
                    ? authorizationException.getError().getErrorCode()
                    : authorizationException.getError().getDescription();
        }
        else {
            errorMessage = exception.getLocalizedMessage();
        }
        // 手机号登录
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (SecurityConstants.APP.equals(grantType)) {
            errorMessage = MsgUtils.getSecurityMessage(
                    "AbstractUserDetailsAuthenticationProvider.smsBadCredentials");
        }
        this.errorHttpResponseConverter.write(
                R.failed(errorMessage),
                MediaType.APPLICATION_JSON,
                httpResponse
        );
    }

}
