package com.joizhang.chat.common.log;

import com.joizhang.chat.admin.service.SysLogService;
import com.joizhang.chat.common.log.aspect.SysLogAspect;
import com.joizhang.chat.common.log.event.SysLogListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志自动配置
 */
@RequiredArgsConstructor
@EnableAsync
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false)
public class LogAutoConfiguration {

    @Bean
    public SysLogListener sysLogListener(SysLogService sysLogService) {
        return new SysLogListener(sysLogService);
    }

    @Bean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }

}
