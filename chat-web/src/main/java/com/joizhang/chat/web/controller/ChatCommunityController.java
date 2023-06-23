package com.joizhang.chat.web.controller;

import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.web.api.entity.ChatCommunity;
import com.joizhang.chat.web.service.ChatCommunityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/svc/community")
@Tag(name = "聊天群模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ChatCommunityController {

    private final ChatCommunityService chatGroupService;

    @PostMapping
    public R<Boolean> createCommunity(@Valid @RequestBody ChatCommunity chatCommunity) {
        return R.ok(chatGroupService.saveGroupAndMembers(chatCommunity));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> deleteCommunity(@PathVariable Long id) {
        return R.ok(chatGroupService.removeById(id));
    }

}
