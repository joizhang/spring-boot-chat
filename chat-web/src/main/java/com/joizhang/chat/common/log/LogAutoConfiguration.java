package com.joizhang.chat.common.log;

import com.joizhang.chat.admin.controller.SysLogController;
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
    public SysLogListener sysLogListener(SysLogController remoteLogService) {
        return new SysLogListener(remoteLogService);
    }

    @Bean
    public SysLogAspect sysLogAspect() {
        return new SysLogAspect();
    }

}
