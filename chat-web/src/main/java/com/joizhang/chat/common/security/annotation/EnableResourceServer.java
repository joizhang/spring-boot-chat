package com.joizhang.chat.common.security.annotation;

import com.joizhang.chat.common.security.component.MyResourceServerAutoConfiguration;
import com.joizhang.chat.common.security.component.MyResourceServerConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.lang.annotation.*;

/**
 * 资源服务注解
 */
@Documented
@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({
        MyResourceServerAutoConfiguration.class,
        MyResourceServerConfiguration.class,
})
public @interface EnableResourceServer {
}
