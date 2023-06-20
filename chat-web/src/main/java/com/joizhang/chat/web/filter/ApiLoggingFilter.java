package com.joizhang.chat.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ApiLoggingFilter extends OncePerRequestFilter {

    private static final String START_TIME = "startTime";

    private static final String X_REAL_IP = "X-Real-IP"; // nginx需要配置

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            String info = String.format(
                    "Method:{%s} Host:{%s} Path:{%s} Query:{%s}",
                    request.getMethod(),
                    request.getRemoteHost(),
                    request.getRequestURI(),
                    request.getQueryString()
            );
            log.debug(info);
        }
        request.setAttribute(START_TIME, System.currentTimeMillis());
        filterChain.doFilter(request, response);
        Long startTime = (Long) request.getAttribute(START_TIME);
        if (startTime != null) {
            Long executeTime = (System.currentTimeMillis() - startTime);
            List<String> ips = Collections.singletonList(request.getHeader(X_REAL_IP));
            String ip = request.getHeader(X_REAL_IP);
            String api = request.getRequestURI();

            int code = response.getStatus();
            // 当前仅记录日志，后续可以添加日志队列，来过滤请求慢的接口
            log.info("来自IP地址：{}，请求接口：{}，响应状态码：{}，请求耗时：{}ms", ip, api, code, executeTime);
        }
    }
}
