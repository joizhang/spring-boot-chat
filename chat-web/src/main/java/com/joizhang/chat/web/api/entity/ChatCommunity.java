package com.joizhang.chat.web.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 聊天群
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatCommunity extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "聊天群的名称不能为空")
    @Schema(title = "聊天群的名称")
    private String communityName;

    @Schema(description = "群头像")
    private String avatar;

    @Schema(title = "群描述")
    private String description;

    @NotNull(message = "管理员ID不能为空")
    @Schema(title = "管理员ID")
    private Long adminId;

}
