package com.joizhang.chat.web.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 朋友关系表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatFriend extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @NotNull(message = "用户id不能为空")
    @Schema(description = "用户id")
    private Long userId;

    @NotNull(message = "朋友id不能为空")
    @Schema(description = "朋友id")
    private Long friendId;

    @Schema(description = "朋友备注名")
    private String remark;

    @Schema(description = "好友请求状态")
    private Integer requestStatus;

    /**
     * 0-正常，1-删除
     */
    @TableLogic
    private String delFlag;

}
