package com.joizhang.chat.web.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.common.core.constant.CommonConstants;
import com.joizhang.chat.web.api.dto.CustomerDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.api.vo.CustomerVo;
import com.joizhang.chat.web.mapper.ChatCustomerMapper;
import com.joizhang.chat.web.service.ChatCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatCustomerServiceImpl extends ServiceImpl<ChatCustomerMapper, ChatCustomer>
        implements ChatCustomerService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Override
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
    public Boolean updateCustomer(ChatCustomer customer) {
        LambdaUpdateWrapper<ChatCustomer> updateWrapper = Wrappers.<ChatCustomer>lambdaUpdate()
                .eq(ChatCustomer::getId, customer.getId())
                .set(StrUtil.isNotBlank(customer.getAvatar()), ChatCustomer::getAvatar, customer.getAvatar())
                .set(StrUtil.isNotBlank(customer.getUsername()), ChatCustomer::getUsername, customer.getUsername())
                .set(StrUtil.isNotBlank(customer.getPhone()), ChatCustomer::getPhone, customer.getPhone())
                .set(StrUtil.isNotBlank(customer.getAbout()), ChatCustomer::getAbout, customer.getAbout())
                .set(ChatCustomer::getUpdateTime, LocalDateTimeUtil.now())
                .set(ChatCustomer::getUpdateBy, customer.getUsername());
        return this.update(updateWrapper);
    }

    @Override
    public IPage<CustomerVo> queryCustomers(Page<ChatCustomer> page, ChatCustomer customer) {
        LambdaQueryWrapper<ChatCustomer> queryWrapper = Wrappers.<ChatCustomer>lambdaQuery()
                .like(StrUtil.isNotBlank(customer.getUsername()), ChatCustomer::getUsername, customer.getUsername());
        Page<ChatCustomer> customerPage = this.page(page, queryWrapper);
        List<CustomerVo> records = customerPage.getRecords().stream()
                .map(c -> {
                    CustomerVo customerVo = new CustomerVo();
                    BeanUtils.copyProperties(c, customerVo);
                    return customerVo;
                })
                .collect(Collectors.toList());
        Page<CustomerVo> customerVoPage =
                new Page<>(customerPage.getCurrent(), customerPage.getSize(), customerPage.getTotal());
        customerVoPage.setRecords(records);
        return customerVoPage;
    }
}
