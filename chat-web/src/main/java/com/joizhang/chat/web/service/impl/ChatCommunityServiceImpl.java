package com.joizhang.chat.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.web.api.entity.ChatCommunity;
import com.joizhang.chat.web.api.entity.ChatCommunityMember;
import com.joizhang.chat.web.mapper.ChatCommunityMapper;
import com.joizhang.chat.web.service.ChatCommunityMemberService;
import com.joizhang.chat.web.service.ChatCommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatCommunityServiceImpl extends ServiceImpl<ChatCommunityMapper, ChatCommunity>
        implements ChatCommunityService {

    private ChatCommunityMemberService communityMemberService;

    @Override
    public Boolean saveGroupAndMembers(ChatCommunity chatCommunity) {
        this.save(chatCommunity);
        ChatCommunityMember chatCommunityMember = new ChatCommunityMember();
        chatCommunityMember.setGroupId(chatCommunity.getId());
        chatCommunityMember.setMemberId(chatCommunity.getAdminId());
        return communityMemberService.save(chatCommunityMember);
    }
}
