package com.joizhang.chat.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 聊天客户 Mapper
 */
@Mapper
public interface ChatCustomerMapper extends BaseMapper<ChatCustomer> {
}
