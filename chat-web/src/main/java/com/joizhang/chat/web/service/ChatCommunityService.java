package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.web.api.entity.ChatCommunity;

/**
 * 聊天群 Service
 */
public interface ChatCommunityService extends IService<ChatCommunity> {

    Boolean saveGroupAndMembers(ChatCommunity chatCommunity);

}
