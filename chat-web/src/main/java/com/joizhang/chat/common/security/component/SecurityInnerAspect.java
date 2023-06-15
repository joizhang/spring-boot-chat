package com.joizhang.chat.common.security.component;

import cn.hutool.core.util.StrUtil;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@RequiredArgsConstructor
@Aspect
public class SecurityInnerAspect implements Ordered {

    private final HttpServletRequest request;

    @SneakyThrows
    @Around("@within(inner) || @annotation(inner)")
    public Object around(ProceedingJoinPoint point, Inner inner) {
        // 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
        Objects.requireNonNull(inner);
        String headerFrom = request.getHeader(SecurityConstants.FROM);
        if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, headerFrom)) {
            log.warn("访问接口 {} 没有权限", point.getSignature().getName());
            throw new AccessDeniedException("Access is denied");
        }
        return point.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
