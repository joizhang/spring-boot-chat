package com.joizhang.chat.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.web.api.entity.ChatGroup;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天群 Mapper
 */
@Mapper
public interface ChatGroupMapper extends BaseMapper<ChatGroup> {
}
