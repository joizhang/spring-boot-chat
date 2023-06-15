package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.web.api.dto.ChatFriendRequestDTO;
import com.joizhang.chat.web.api.entity.ChatFriend;

/**
 * 好友关系 Service
 */
public interface ChatFriendService extends IService<ChatFriend> {

    /**
     * 朋友关系是否存在
     *
     * @param chatFriend 查询参数
     * @return 是否存在
     */
    boolean exist(ChatFriend chatFriend);

    /**
     * 保存并发送至消息队列
     *
     * @param chatFriendRequestDTO 实体
     */
    void saveAndSendToMQ(ChatFriendRequestDTO chatFriendRequestDTO);
}
