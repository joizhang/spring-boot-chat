package com.joizhang.chat.admin.api.vo;

import com.joizhang.chat.admin.api.entity.SysUser;
import lombok.Data;

import java.io.Serializable;

/**
 * commit('SET_ROLES', data) commit('SET_NAME', data) commit('SET_AVATAR', data)
 * commit('SET_INTRODUCTION', data) commit('SET_PERMISSIONS', data)
 */
@Data
public class UserInfoVO implements Serializable {

    /**
     * 用户基本信息
     */
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    private String[] permissions;

    /**
     * 角色集合
     */
    private Long[] roles;

}
