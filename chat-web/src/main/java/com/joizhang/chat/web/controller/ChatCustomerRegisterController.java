package com.joizhang.chat.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.annotation.Inner;
import com.joizhang.chat.web.api.dto.CustomerDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.service.AppService;
import com.joizhang.chat.web.service.ChatCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 客户端注册功能 chat.register = true
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/svc/register")
@ConditionalOnProperty(name = "chat.register", matchIfMissing = true)
public class ChatCustomerRegisterController {

    private final ChatCustomerService customerService;

    private final AppService appService;

    /**
     * 注册用户
     *
     * @param customerDTO 用户信息
     * @return success/false
     */
    @Inner(value = false)
    @PostMapping("/customer")
    public R<Boolean> registerUser(@Valid @RequestBody CustomerDTO customerDTO) {
        // 校验验证码
        if (!appService.check(customerDTO.getPhone(), customerDTO.getCode())) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_ERROR));
        }
        // 判断用户名是否存在
        ChatCustomer customer = customerService.getOne(
                Wrappers.<ChatCustomer>lambdaQuery()
                        .eq(ChatCustomer::getUsername, customerDTO.getUsername())
        );
        if (customer != null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, customerDTO.getUsername()));
        }
        return R.ok(customerService.saveUser(customerDTO));
    }
}
