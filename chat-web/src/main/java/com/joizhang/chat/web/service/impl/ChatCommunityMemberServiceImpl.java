package com.joizhang.chat.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.web.api.entity.ChatCommunityMember;
import com.joizhang.chat.web.mapper.ChatCommunityMemberMapper;
import com.joizhang.chat.web.service.ChatCommunityMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatCommunityMemberServiceImpl extends ServiceImpl<ChatCommunityMemberMapper, ChatCommunityMember>
        implements ChatCommunityMemberService {
}
