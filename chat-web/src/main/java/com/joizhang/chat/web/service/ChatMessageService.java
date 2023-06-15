package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.joizhang.chat.web.api.entity.ChatMessage;

import java.util.List;

/**
 * 聊天消息 Service
 */
public interface ChatMessageService extends IService<ChatMessage> {

    /**
     * 发送至消息队列
     *
     * @param chatMessage 消息
     */
    void sendToMQ(ChatMessage chatMessage);

    /**
     * 发送给消息接收者
     *
     * @param chatMessage 消息
     */
    void pushToReceiver(ChatMessage chatMessage) throws JsonProcessingException;

    /**
     * 查询聊天历史记录
     *
     * @param userId       当前用户ID
     * @param serverStubId 客户端存储的服务器存根ID
     * @return 聊天历史记录
     */
    List<ChatMessage> getHistory(Long userId, Long serverStubId);

    /**
     * 保存消息并返回确认消息
     *
     * @param chatMessage 消息
     */
    void saveAndAck(ChatMessage chatMessage);
}
