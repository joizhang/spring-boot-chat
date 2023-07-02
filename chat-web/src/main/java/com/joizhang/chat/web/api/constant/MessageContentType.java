package com.joizhang.chat.web.api.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 消息类型
 */
@Getter
@RequiredArgsConstructor
public enum MessageContentType {

    ERROR(0, "Error"),

    TEXT(1, "Text and emoji"),

    IMAGE(2, "Image"),

    VIDEO(3, "Video"),

    AUDIO(4, "Audio"),

    /**
     * {@link FriendRequestStatus}
     */
    FRIEND_REQ(5, "Friend request"),

    /**
     * 消息确认
     */
    ACK(6, "Message ACK"),

    /**
     * 提示文本，居中显示
     */
    TOOLTIP(7, "Tooltip"),
    ;

    private final Integer type;

    private final String description;
}
