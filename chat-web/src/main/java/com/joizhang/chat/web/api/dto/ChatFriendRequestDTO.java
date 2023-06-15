package com.joizhang.chat.web.api.dto;

import com.joizhang.chat.web.api.entity.ChatFriend;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 朋友请求DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatFriendRequestDTO extends ChatFriend {

    @NotNull
    @Schema(description = "序号")
    private Long seqNum;

}
