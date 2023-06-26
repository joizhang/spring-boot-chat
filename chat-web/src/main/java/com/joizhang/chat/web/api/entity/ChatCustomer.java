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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotNull(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度为3-20之间")
    @Schema(title = "用户名")
    private String username;

    @NotNull(message = "密码不能为空")
    @Size(min = 3, max = 20, message = "密码长度为3-20之间")
    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    @JsonIgnore
    @Schema(description = "随机盐")
    private String salt;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "简介")
    private String about;

    @JsonIgnore
    @Schema(description = "锁定标记")
    private String lockFlag;

    /**
     * 0-正常，1-删除
     */
    @JsonIgnore
    @TableLogic
    private String delFlag;
}
