package com.joizhang.chat.web.controller;

import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.api.vo.MessageVo;
import com.joizhang.chat.web.service.ChatMessageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/svc/message")
@Tag(name = "聊天消息模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ChatMessageController {

    private final ChatMessageService messageService;

    @GetMapping("/history")
    public R<List<MessageVo>> history(Long serverStubId) {
        Long userId = SecurityUtils.getUser().getId();
        List<ChatMessage> chatMessages = messageService.getHistory(userId, serverStubId);
        List<MessageVo> messageVos = chatMessages.stream().map(m -> {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(m, messageVo);
            return messageVo;
        }).collect(Collectors.toList());
        return R.ok(messageVos);
    }

}
