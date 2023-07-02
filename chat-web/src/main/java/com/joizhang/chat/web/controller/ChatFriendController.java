package com.joizhang.chat.web.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.web.api.constant.FriendRequestStatus;
import com.joizhang.chat.web.api.dto.ChatFriendRequestDTO;
import com.joizhang.chat.web.api.entity.ChatFriend;
import com.joizhang.chat.web.api.vo.FriendCustomerVo;
import com.joizhang.chat.web.config.ChatConfigProperties;
import com.joizhang.chat.web.service.ChatFriendService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/svc/friend")
@Tag(name = "朋友关系模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ChatFriendController {

    private final ChatFriendService friendService;

    private final ChatConfigProperties chatConfig;

    /**
     * 朋友关系是否存在
     *
     * @param chatFriend 好友
     * @return 朋友关系是否存在
     */
    @GetMapping
    public R<Boolean> exist(ChatFriend chatFriend) {
        Long userId = SecurityUtils.getUser().getId();
        if (!userId.equals(chatFriend.getUserId())) {
            return R.failed(MsgUtils.getSecurityMessage("ChatFriendController.illegalIdentity"));
        }
        return R.ok(friendService.exist(chatFriend));
    }

    /**
     * 好友请求
     *
     * @param friendRequestDTO 参数
     * @return 请求是否发送成功
     */
    @PostMapping
    public R<String> addFriendRequest(@Valid @RequestBody ChatFriendRequestDTO friendRequestDTO) {
        Long userId = SecurityUtils.getUser().getId();
        if (!userId.equals(friendRequestDTO.getUserId())) {
            return R.failed(MsgUtils.getSecurityMessage("ChatFriendController.illegalIdentity"));
        }
        ChatFriend friend = friendService.getOne(
                Wrappers.<ChatFriend>lambdaQuery()
                        .eq(ChatFriend::getUserId, friendRequestDTO.getUserId())
                        .eq(ChatFriend::getFriendId, friendRequestDTO.getFriendId())
        );
        if (ObjectUtil.isNotNull(friend)) {
            // 判断是否有待处理的好友请求
            if (FriendRequestStatus.PENDING.getStatus().equals(friend.getRequestStatus())) {
                return R.failed("Friend request cannot be sent repeatedly");
            }
            // 判断是否已经是朋友
            if (FriendRequestStatus.ACCEPTED.getStatus().equals(friend.getRequestStatus())) {
                return R.failed("You are already friends");
            }
        }
        // 不能超过朋友上限
        long count = friendService.count(
                Wrappers.<ChatFriend>lambdaQuery()
                        .eq(ChatFriend::getUserId, friendRequestDTO.getUserId())
                        .eq(ChatFriend::getRequestStatus, FriendRequestStatus.ACCEPTED.getStatus())
        );
        if (count >= chatConfig.getFriendLimit()) {
            return R.failed("You have reached the limit of friends");
        }
        friendService.saveFriendRequest(friendRequestDTO);
        return R.ok();
    }

    /**
     * 删除好友
     *
     * @param chatFriend 好友
     * @return 是否成功
     */
    @DeleteMapping
    public R<Boolean> deleteFriend(ChatFriend chatFriend) {
        Long userId = SecurityUtils.getUser().getId();
        if (!userId.equals(chatFriend.getUserId())) {
            return R.failed(MsgUtils.getSecurityMessage("ChatFriendController.illegalIdentity"));
        }
        chatFriend.setUserId(userId);
        chatFriend.setRequestStatus(FriendRequestStatus.ACCEPTED.getStatus());
        boolean exist = friendService.exist(chatFriend);
        if (!exist) {
            return R.failed("Cannot delete a friend that does not exist");
        }
        return R.ok(friendService.removeById(chatFriend));
    }


    /**
     * 查询好友详细信息列表
     *
     * @param chatFriend 最新朋友的添加时间
     * @return 好友详细信息列表
     */
    @GetMapping("/customers")
    public R<List<FriendCustomerVo>> getCustomersByFriends(ChatFriend chatFriend) {
        Long userId = SecurityUtils.getUser().getId();
        if (!userId.equals(chatFriend.getUserId())) {
            return R.failed(MsgUtils.getSecurityMessage("ChatFriendController.illegalIdentity"));
        }
        if (FriendRequestStatus.ACCEPTED.getStatus().equals(chatFriend.getRequestStatus())) {
            return R.ok(friendService.getCustomersByAcceptFriends(chatFriend));
        } else if (FriendRequestStatus.PENDING.getStatus().equals(chatFriend.getRequestStatus())) {
            return R.ok(friendService.getCustomersByPendingFriends(chatFriend));
        } else {
            throw new IllegalArgumentException("Illegal friend request status");
        }
    }
}
