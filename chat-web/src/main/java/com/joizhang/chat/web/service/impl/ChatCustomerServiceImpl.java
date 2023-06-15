package com.joizhang.chat.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.common.core.constant.CommonConstants;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.web.api.dto.CustomerDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.mapper.ChatCustomerMapper;
import com.joizhang.chat.web.service.AppService;
import com.joizhang.chat.web.service.ChatCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatCustomerServiceImpl extends ServiceImpl<ChatCustomerMapper, ChatCustomer> implements ChatCustomerService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final AppService appService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(CustomerDTO customerDTO) {
        ChatCustomer customer = new ChatCustomer();
        BeanUtils.copyProperties(customerDTO, customer);
        customer.setDelFlag(CommonConstants.STATUS_NORMAL);
        customer.setPassword(ENCODER.encode(customer.getPassword()));
        this.save(customer);
        return Boolean.TRUE;
    }

    @Override
    public CustomerInfoVo getCustomerInfo(ChatCustomer customer) {
        CustomerInfoVo customerInfo = new CustomerInfoVo();
        customerInfo.setChatCustomer(customer);
        // 设置角色列表
        return customerInfo;
    }

    @Override
    public R<Boolean> registerUser(CustomerDTO customerDTO) {
        // 校验验证码
        if (!appService.check(customerDTO.getPhone(), customerDTO.getCode())) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_ERROR));
        }
        // 判断用户名是否存在
        ChatCustomer customer =
                this.getOne(Wrappers.<ChatCustomer>lambdaQuery().eq(ChatCustomer::getUsername, customerDTO.getUsername()));
        if (customer != null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERNAME_EXISTING, customerDTO.getUsername()));
        }
        return R.ok(saveUser(customerDTO));
    }

}
