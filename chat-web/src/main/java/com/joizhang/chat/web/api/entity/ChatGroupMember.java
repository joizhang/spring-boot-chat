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
public class ChatGroupMember {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "聊天群的id不能为空")
    @Schema(description = "聊天群的id")
    private Long groupId;

    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id")
    private Long userId;

}
