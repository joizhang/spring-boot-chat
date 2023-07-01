package com.joizhang.chat.common.security.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.joizhang.chat.admin.api.vo.UserInfoVO;
import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.common.core.constant.CommonConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.core.util.RetOps;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.controller.ChatCustomerController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.Ordered;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Objects;

/**
 * 聊天客户的详细信息实现
 */
@Slf4j
@RequiredArgsConstructor
public class MyCustomerUserDetailsServiceImpl implements MyUserDetailsService {

    /**
     * private final RemoteChatCustomerService remoteChatCustomerService;
     */
    private final ChatCustomerController remoteChatCustomerService;

    private final CacheManager cacheManager;

    @Override
    public boolean support(String clientId, String grantType) {
        return "chat".equals(clientId);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    @Override
    public UserDetails getUserDetails(R<UserInfoVO> result) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
        if (ObjectUtil.isNotNull(cache) && ObjectUtil.isNotNull(cache.get(username))) {
            return (MyUser) Objects.requireNonNull(cache.get(username)).get();
        }
        R<CustomerInfoVo> result = remoteChatCustomerService.getCustomerInfoByUsername(username);
        CustomerInfoVo customerInfoVo = RetOps.of(result)
                .assertSuccess(r -> new RuntimeException("Internal server error"))
                .getData()
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        ChatCustomer chatCustomer = customerInfoVo.getChatCustomer();
        UserDetails userDetails = new MyUser(
                chatCustomer.getId(),
                0L,
                chatCustomer.getUsername(),
                SecurityConstants.BCRYPT + chatCustomer.getPassword(),
                chatCustomer.getPhone(),
                true,
                true,
                true,
                StrUtil.equals(chatCustomer.getLockFlag(), CommonConstants.STATUS_NORMAL),
                Collections.emptyList()
        );
        if (cache != null) {
            cache.put(username, userDetails);
        }
        return userDetails;
    }
}
