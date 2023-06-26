package com.joizhang.chat.web.controller;

import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.annotation.Inner;
import com.joizhang.chat.web.api.dto.CustomerDTO;
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

    /**
     * 注册用户
     *
     * @param customerDTO 用户信息
     * @return success/false
     */
    @Inner(value = false)
    @PostMapping("/customer")
    public R<Boolean> registerUser(@Valid @RequestBody CustomerDTO customerDTO) {
        return customerService.registerUser(customerDTO);
    }
}
