package com.joizhang.chat.web.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.exception.ValidateCodeException;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.core.util.WebUtils;
import com.joizhang.chat.web.config.ChatConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ValidateCodeGatewayFilter extends OncePerRequestFilter {

    private final ChatConfigProperties chatConfigProperties;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 不是登录请求，直接向下执行
        boolean isAuthToken = CharSequenceUtil.containsAnyIgnoreCase(request.getRequestURI(),
                SecurityConstants.OAUTH_TOKEN_URL);
        if (!isAuthToken) {
            filterChain.doFilter(request, response);
            return;
        }

        // 刷新token，手机号登录（也可以这里进行校验） 直接向下执行
        String grantType = request.getParameter("grant_type");
        if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isIgnoreClient = chatConfigProperties.getIgnoreClients().contains(WebUtils.getClientId());
        try {
            // only oauth and the request not in ignore clients need check code.
            if (!isIgnoreClient) {
                checkCode(request);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setStatus(HttpStatus.PRECONDITION_REQUIRED.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().print(objectMapper.writeValueAsString(R.failed(e.getMessage())));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @SneakyThrows
    private void checkCode(HttpServletRequest request) {
        String code = request.getParameter("code");
        if (CharSequenceUtil.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        String randomStr = request.getParameter("randomStr");
        if (CharSequenceUtil.isBlank(randomStr)) {
            randomStr = request.getParameter(SecurityConstants.SMS_PARAMETER_NAME);
        }
        String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
        Object codeObj = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isEmpty(codeObj) || !code.equals(codeObj)) {
            throw new ValidateCodeException("验证码不合法");
        }
        redisTemplate.delete(key);
    }
}
