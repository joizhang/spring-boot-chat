package com.joizhang.chat.web.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.web.api.constant.MessageContentSubtype;
import com.joizhang.chat.web.api.constant.MessageContentType;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.service.ChatMessageService;
import com.joizhang.chat.web.util.BeanValidationResult;
import com.joizhang.chat.web.util.ValidationUtil;
import com.joizhang.chat.web.util.WebSocketSessionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler extends TextWebSocketHandler {

    private final SessionKeyGenerator sessionKeyGenerator;

    private final ChatMessageService messageService;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        redisTemplate.opsForValue().set(CacheConstants.WS_SESSION_KEY + sessionKey, true);
        ConcurrentWebSocketSessionDecorator sessionDecorator =
                new ConcurrentWebSocketSessionDecorator(session, 5000, 10240);
        WebSocketSessionHolder.addSession(sessionKey, sessionDecorator);
    }

    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        WebSocketSession sessionDecorator = WebSocketSessionHolder.getSession(sessionKey);
        String payload = message.getPayload();
        MessageVo errorMessage = null;
        try {
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
            log.debug("sessionId {} ,msg {}", session.getId(), chatMessage);
            BeanValidationResult result = ValidationUtil.warpValidate(chatMessage);
            if (!result.isSuccess()) {
                throw new IllegalArgumentException(result.getErrorMessages().get(0).getMessage());
            }
            messageService.sendToMQ(chatMessage);
        } catch (JsonProcessingException e) {
            // 消息结构异常
            log.error("Illegal data format: {}", e.getMessage());
            errorMessage = new MessageVo(
                    0L,
                    0L,
                    0L,
                    0L,
                    "ILLEGAL DATA FORMAT",
                    MessageContentType.ERROR.getType(),
                    MessageContentSubtype.DEFAULT.getType(),
                    LocalDateTime.now()
            );
        } catch (IllegalArgumentException e) {
            // 消息结构异常
            log.error("Illegal message: {}", e.getMessage());
            errorMessage = new MessageVo(
                    0L,
                    0L,
                    0L,
                    0L,
                    "ILLEGAL ARGUMENT",
                    MessageContentType.ERROR.getType(),
                    MessageContentSubtype.DEFAULT.getType(),
                    LocalDateTime.now()
            );
        } catch (AmqpException e) {
            // 消息队列异常
            log.error("Message queue error: {}", e.getMessage());
            errorMessage = new MessageVo(
                    0L,
                    0L,
                    0L,
                    0L,
                    "MESSAGE QUEUE CONNECTION ERROR",
                    MessageContentType.ERROR.getType(),
                    MessageContentSubtype.DEFAULT.getType(),
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            // 其他异常
            log.error("Server error: {}", e.getMessage());
            errorMessage = new MessageVo(
                    0L,
                    0L,
                    0L,
                    0L,
                    "SERVER ERROR",
                    MessageContentType.ERROR.getType(),
                    MessageContentSubtype.DEFAULT.getType(),
                    LocalDateTime.now()
            );
        }
        if (ObjectUtil.isNotNull(errorMessage)) {
            String jsonStr = JSONUtil.toJsonStr(errorMessage);
            try {
                sessionDecorator.sendMessage(new TextMessage(jsonStr));
            } catch (IOException ex) {
                log.error("Socket connection error!");
            }
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session,
                                      @NonNull CloseStatus closeStatus) throws Exception {
        super.afterConnectionClosed(session, closeStatus);
        String sessionKey = this.sessionKeyGenerator.sessionKey(session);
        redisTemplate.opsForValue().set(CacheConstants.WS_SESSION_KEY + sessionKey, false);
        WebSocketSessionHolder.removeSession(sessionKey);
    }
}
