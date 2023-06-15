package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;

    /**
     * 随机盐
     */
    @JsonIgnore
    @Schema(description = "随机盐")
    private String salt;

    /**
     * 锁定标记
     */
    @Schema(description = "锁定标记")
    private String lockFlag;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 头像
     */
    @Schema(description = "头像地址")
    private String avatar;

    /**
     * 部门ID
     */
    @Schema(description = "用户所属部门id")
    private Long deptId;

    /**
     * 0-正常，1-删除
     */
    @TableLogic
    private String delFlag;

}
