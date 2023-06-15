package com.joizhang.chat.web.handler;

import org.springframework.web.socket.WebSocketSession;

public interface SessionKeyGenerator {

    /**
     * 生成 SessionKey，返回的是用户ID
     *
     * @param webSocketSession socket session
     * @return SessionKey
     */
    String sessionKey(WebSocketSession webSocketSession);

}
