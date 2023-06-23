package com.joizhang.chat.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.web.api.entity.ChatCommunity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天群 Mapper
 */
@Mapper
public interface ChatCommunityMapper extends BaseMapper<ChatCommunity> {
}
