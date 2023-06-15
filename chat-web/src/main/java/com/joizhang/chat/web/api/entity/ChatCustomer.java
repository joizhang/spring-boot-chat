package com.joizhang.chat.web.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 聊天用户
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatCustomer extends BaseEntity {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @JsonIgnore
    @Schema(description = "随机盐")
    private String salt;

    @Schema(description = "锁定标记")
    private String lockFlag;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像地址")
    private String avatar;

    /**
     * 0-正常，1-删除
     */
    @TableLogic
    private String delFlag;
}
