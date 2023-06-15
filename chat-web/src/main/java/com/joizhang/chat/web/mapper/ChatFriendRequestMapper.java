package com.joizhang.chat.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.web.api.dto.ChatFriendRequestDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 好友请求 Mapper
 */
@Mapper
public interface ChatFriendRequestMapper extends BaseMapper<ChatFriendRequestDTO> {
}
