package com.joizhang.chat.web.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 聊天消息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "发送者不能为空")
    @Schema(description = "发送者ID")
    private Long senderId;

    @NotNull(message = "接收者不能为空")
    @Schema(description = "接收者ID")
    private Long receiverId;

    @NotNull(message = "序号不能为空")
    @Schema(description = "序号")
    private Long seqNum;

    @NotBlank(message = "内容不能为空")
    @Schema(description = "消息内容")
    private String content;

    /**
     * {@link com.joizhang.chat.web.api.constant.MessageContentType}
     */
    @NotNull(message = "消息类型不能为空")
    @Schema(description = "消息类型：0-error, 1-text and emoji, 2-image, 3-video, 4-audio...")
    private Integer contentType;

    @NotNull(message = "消息子类型不能为空")
    @Schema(description = "消息子类型，默认为0")
    private Integer contentSubtype;
}
