package com.joizhang.chat.web.handler;

import com.joizhang.chat.web.util.WebSocketSessionHolder;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

@Deprecated
public class MapSessionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

    private final SessionKeyGenerator sessionKeyGenerator;

    public MapSessionWebSocketHandlerDecorator(WebSocketHandler delegate, SessionKeyGenerator sessionKeyGenerator) {
        super(delegate);
        this.sessionKeyGenerator = sessionKeyGenerator;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        WebSocketSessionHolder.addSession(sessionKey, session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus closeStatus) {
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        WebSocketSessionHolder.removeSession(sessionKey);
    }
}
