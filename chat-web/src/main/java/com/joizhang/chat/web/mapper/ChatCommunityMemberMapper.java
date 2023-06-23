package com.joizhang.chat.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.web.api.entity.ChatCommunityMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天群成员 Mapper
 */
@Mapper
public interface ChatCommunityMemberMapper extends BaseMapper<ChatCommunityMember> {
}
