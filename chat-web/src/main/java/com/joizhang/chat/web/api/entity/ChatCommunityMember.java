package com.joizhang.chat.web.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 聊天群成员
 */
@Data
public class ChatCommunityMember {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "聊天群的ID不能为空")
    @Schema(description = "聊天群的ID")
    private Long communityId;

    @NotNull(message = "成员ID不能为空")
    @Schema(description = "成员ID")
    private Long memberId;

    @Schema(description = "群内昵称")
    private String nickname;

}
