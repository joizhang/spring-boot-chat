package com.joizhang.chat.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.web.filter.ApiLoggingFilter;
import com.joizhang.chat.web.filter.ValidateCodeGatewayFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ChatConfigProperties.class)
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ValidateCodeGatewayFilter> validateCodeGatewayFilter(ChatConfigProperties chatConfigProperties,
                                                                                       ObjectMapper objectMapper,
                                                                                       RedisTemplate<String, Object> redisTemplate,
                                                                                       SecurityProperties securityProperties) {
        FilterRegistrationBean<ValidateCodeGatewayFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ValidateCodeGatewayFilter(chatConfigProperties, objectMapper, redisTemplate));
        registrationBean.setOrder(securityProperties.getFilter().getOrder() - 1);
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "chat.api-logging")
    public FilterRegistrationBean<ApiLoggingFilter> apiLoggingFilter() {
        FilterRegistrationBean<ApiLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiLoggingFilter());
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
