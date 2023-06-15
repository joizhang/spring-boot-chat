package com.joizhang.chat.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.web.api.entity.ChatGroup;
import com.joizhang.chat.web.mapper.ChatGroupMapper;
import com.joizhang.chat.web.service.ChatGroupService;
import org.springframework.stereotype.Service;

@Service
public class ChatGroupServiceImpl extends ServiceImpl<ChatGroupMapper, ChatGroup> implements ChatGroupService {
}
