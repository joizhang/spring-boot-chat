package com.joizhang.chat.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(WebSocketProperties.PREFIX)
public class WebSocketProperties {

    public static final String PREFIX = "chat.websocket";

    private String path = "/ws/info";

    private String allowOrigins = "*";

    private boolean supportPartialMessages = false;

    private boolean heartbeat = true;

    private boolean mapSession = true;

    private String messageDistributor = "local";

}
