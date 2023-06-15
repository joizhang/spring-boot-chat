package com.joizhang.chat.web.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 好友请求状态
 * <p>
 * {@link MessageContentType#FRIEND_REQ}
 */
@RequiredArgsConstructor
@Getter
public enum FriendRequestStatus {

    PENDING(1, "Pending"),

    ACCEPTED(2, "Accepted"),
    ;

    private final Integer status;

    private final String description;
}
