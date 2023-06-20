package com.joizhang.chat.web.config;

import com.joizhang.chat.web.filter.ApiLoggingFilter;
import com.joizhang.chat.web.filter.PasswordDecoderFilter;
import com.joizhang.chat.web.filter.ValidateCodeGatewayFilter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("chat")
public class ChatConfigProperties {

    /**
     * 网关解密前端登录密码 秘钥 {@link PasswordDecoderFilter}
     */
    private String encodeKey;

    /**
     * 网关不需要校验验证码的客户端 {@link ValidateCodeGatewayFilter}
     */
    private List<String> ignoreClients;

    /**
     * 是否开启接口的日志{@link ApiLoggingFilter}
     */
    private boolean apiLogging;

}
