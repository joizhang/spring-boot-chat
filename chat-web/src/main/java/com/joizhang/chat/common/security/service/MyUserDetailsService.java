package com.joizhang.chat.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.joizhang.chat.admin.api.entity.SysUser;
import com.joizhang.chat.admin.api.vo.UserInfoVO;
import com.joizhang.chat.common.core.constant.CommonConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.core.util.RetOps;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface MyUserDetailsService extends UserDetailsService, Ordered {

    /**
     * 是否支持此客户端校验
     *
     * @param clientId 目标客户端
     * @return true/false
     */
    default boolean support(String clientId, String grantType) {
        return true;
    }

    /**
     * 排序值 默认取最大的
     *
     * @return 排序值
     */
    default int getOrder() {
        return 0;
    }

    /**
     * 构建 user details
     *
     * @param result 用户信息
     * @return UserDetails
     */
    default UserDetails getUserDetails(R<UserInfoVO> result) {
        UserInfoVO info = RetOps.of(result)
                .getData()
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        Set<String> dbAuthsSet = new HashSet<>();

        if (ArrayUtil.isNotEmpty(info.getRoles())) {
            // 获取角色
            Arrays.stream(info.getRoles())
                    .forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));
        }

        Collection<GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = info.getSysUser();

        // 构造security用户
        return new MyUser(
                user.getUserId(),
                user.getDeptId(),
                user.getUsername(),
                SecurityConstants.BCRYPT + user.getPassword(),
                user.getPhone(),
                true,
                true,
                true,
                StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL),
                authorities
        );
    }

    /**
     * 通过用户实体查询
     *
     * @param user user
     * @return UserDetails
     */
    default UserDetails loadUserByUser(MyUser user) {
        return this.loadUserByUsername(user.getUsername());
    }
}
