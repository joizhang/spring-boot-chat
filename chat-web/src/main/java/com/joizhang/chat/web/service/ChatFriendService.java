package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.web.api.dto.ChatFriendRequestDTO;
import com.joizhang.chat.web.api.entity.ChatFriend;
import com.joizhang.chat.web.api.vo.FriendCustomerVo;

import java.util.List;

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
     * 保存好友
     *
     * @param chatFriendRequestDTO 实体
     */
    void saveFriendRequest(ChatFriendRequestDTO chatFriendRequestDTO);

    /**
     * 查询好友详细信息列表
     *
     * @param chatFriend 最新朋友的添加时间
     */
    List<FriendCustomerVo> getCustomersByAcceptFriends(ChatFriend chatFriend);

    /**
     * 查询待处理好友详细信息列表
     *
     * @param chatFriend 最新朋友的添加时间
     */
    List<FriendCustomerVo> getCustomersByPendingFriends(ChatFriend chatFriend);

}
