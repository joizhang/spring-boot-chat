package com.joizhang.chat.common;

import com.joizhang.chat.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理器结合 sentinel 全局异常处理器不能作用在 oauth server
 */
@Slf4j
@Order(10000)
@RestControllerAdvice
//@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalBizExceptionHandler {

    /**
     * 全局异常.
     *
     * @param e the exception
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.failed(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }

    /**
     * 处理业务校验过程中碰到的非法参数异常 该异常基本由{@link Assert}抛出
     *
     * @param exception 参数校验异常
     * @return API返回结果对象包装后的错误输出结果
     * @see Assert#hasLength(String, String)
     * @see Assert#hasText(String, String)
     * @see Assert#isTrue(boolean, String)
     * @see Assert#isNull(Object, String)
     * @see Assert#notNull(Object, String)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("非法参数,ex = {}", exception.getMessage(), exception);
        return R.failed(exception.getMessage());
    }

    /**
     * AccessDeniedException
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R<String> handleAccessDeniedException(AccessDeniedException e) {
        String msg = SpringSecurityMessageSource
                .getAccessor()
                .getMessage("AbstractAccessDecisionManager.accessDenied", e.getMessage());
        log.warn("拒绝授权异常信息 ex={}", msg);
        return R.failed(e.getLocalizedMessage());
    }

    /**
     * validation Exception
     *
     * @param exception the exception
     * @return R
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> handleBodyValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.failed(String.format("%s %s", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage()));
    }

    /**
     * validation Exception (以form-data形式传参)
     *
     * @param exception the exception
     * @return R
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> bindExceptionHandler(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.failed(fieldErrors.get(0).getDefaultMessage());
    }
}
