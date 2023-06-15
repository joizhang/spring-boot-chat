package com.joizhang.chat.web.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joizhang.chat.web.api.constant.MessageContentType;
import com.joizhang.chat.web.api.constant.RabbitConstants;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.mapper.ChatMessageMapper;
import com.joizhang.chat.web.service.ChatMessageService;
import com.joizhang.chat.web.util.WebSocketSessionHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @Override
    public void sendToMQ(ChatMessage chatMessage) {
        rabbitTemplate.convertAndSend(RabbitConstants.QUEUE_WORK_MESSAGE_PERSISTENCE, chatMessage);
    }

    @Transactional
    @Override
    public void pushToReceiver(ChatMessage chatMessage) throws JsonProcessingException {
        String sessionKey = String.valueOf(chatMessage.getReceiverId());
        WebSocketSession session = WebSocketSessionHolder.getSession(sessionKey);
        if (ObjectUtil.isNull(session)) {
            return;
        }
        MessageVo messageVo = new MessageVo();
        BeanUtils.copyProperties(chatMessage, messageVo);
        String jsonStr = objectMapper.writeValueAsString(messageVo);
        try {
            log.debug("推送消息至：{}", sessionKey);
            session.sendMessage(new TextMessage(jsonStr));
        } catch (IOException e) {
            log.error("Socket connection error from {} to {}!",
                    chatMessage.getSenderId(), chatMessage.getReceiverId());
        }
    }

    @Override
    public List<ChatMessage> getHistory(Long userId, Long serverStubId) {
        // 默认只获取三天之内的历史消息
        LocalDateTime threeDaysAgo = LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0)
                .minus(3, ChronoUnit.DAYS);
        LambdaQueryWrapper<ChatMessage> queryWrapper = Wrappers.<ChatMessage>lambdaQuery()
                .eq(ChatMessage::getReceiverId, userId)
                .gt(ChatMessage::getId, serverStubId)
                .gt(ChatMessage::getCreateTime, threeDaysAgo);
        return this.list(queryWrapper);
    }

    @Override
    public void saveAndAck(ChatMessage chatMessage) {
        // 保存消息
        this.save(chatMessage);

        // 发送到消息推送队列，推送给接收者进行消息下发
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_FANOUT_MESSAGE, "", chatMessage);

        // 发送到消息推送队列，推送给发送者进行消息确认
        ChatMessage confirmChatMessage = new ChatMessage();
        BeanUtils.copyProperties(chatMessage, confirmChatMessage);
        confirmChatMessage.setSenderId(0L);
        confirmChatMessage.setReceiverId(chatMessage.getSenderId());
        confirmChatMessage.setContentType(MessageContentType.ACK.getType());
        confirmChatMessage.setContent(String.format("%s_%s_%s",
                chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getSeqNum()));
        rabbitTemplate.convertAndSend(RabbitConstants.EXCHANGE_FANOUT_MESSAGE, "", confirmChatMessage);
    }
}
