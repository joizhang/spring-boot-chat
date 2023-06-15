package com.joizhang.chat.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.web.api.entity.ChatFriend;
import org.apache.ibatis.annotations.Mapper;

/**
 * 朋友关系 Mapper
 */
@Mapper
public interface ChatFriendMapper extends BaseMapper<ChatFriend> {
}
