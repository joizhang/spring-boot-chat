package com.joizhang.chat.web.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.web.api.constant.FriendRequestStatus;
import com.joizhang.chat.web.api.dto.ChatFriendRequestDTO;
import com.joizhang.chat.web.api.entity.ChatFriend;
import com.joizhang.chat.web.service.ChatFriendService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/svc/friend")
@Tag(name = "朋友关系模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ChatFriendController {

    private final ChatFriendService friendService;

    /**
     * 朋友关系是否存在
     *
     * @return 朋友关系是否存在
     */
    @GetMapping("/exist")
    public R<Boolean> exist(ChatFriend chatFriend) {
        return R.ok(friendService.exist(chatFriend));
    }

    /**
     * 好友请求
     *
     * @param friendRequestDTO 参数
     * @return 请求是否发送成功
     */
    @PostMapping("/request")
    public R<String> save(@Valid @RequestBody ChatFriendRequestDTO friendRequestDTO) {
        Long userId = SecurityUtils.getUser().getId();
        // 验证身份
        if (!userId.equals(friendRequestDTO.getUserId())) {
            return R.failed("Illegal identity");
        }
        ChatFriend friend = friendService.getOne(
                Wrappers.<ChatFriend>lambdaQuery()
                        .eq(ChatFriend::getUserId, friendRequestDTO.getUserId())
                        .eq(ChatFriend::getFriendId, friendRequestDTO.getFriendId())
        );
        if (ObjectUtil.isNotNull(friend)) {
            // 判断是否有待处理的好友请求
            if (FriendRequestStatus.PENDING.getStatus().equals(friend.getRequestStatus())) {
                return R.failed("Friend Request cannot be sent repeatedly.");
            }
            // 判断是否已经是朋友
            if (FriendRequestStatus.ACCEPTED.getStatus().equals(friend.getRequestStatus())) {
                return R.failed("You are already friends.");
            }
        }
        friendService.saveAndSendToMQ(friendRequestDTO);
        return R.ok();
    }
}
