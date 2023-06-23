package com.joizhang.chat.web.controller;

import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.web.api.entity.ChatCommunityMember;
import com.joizhang.chat.web.service.ChatCommunityMemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/svc/member")
@Tag(name = "聊天群成员模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ChatCommunityMemberController {

    private final ChatCommunityMemberService communityMemberService;

    @PostMapping
    public R<Boolean> createGroup(@Valid @RequestBody ChatCommunityMember communityMember) {
        return R.ok(communityMemberService.save(communityMember));
    }

}
