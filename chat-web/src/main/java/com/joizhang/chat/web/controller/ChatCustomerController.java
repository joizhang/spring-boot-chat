package com.joizhang.chat.web.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.api.vo.CustomerVo;
import com.joizhang.chat.web.service.ChatCustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat/svc/customer")
@Tag(name = "聊天用户模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ChatCustomerController {

    private final ChatCustomerService customerService;

    @GetMapping("hello")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("hello");
    }

    /**
     * 获取当前用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping(value = {"/info"})
    public R<CustomerInfoVo> infoByUsername() {
        String username = SecurityUtils.getUser().getUsername();
        ChatCustomer customer = customerService
                .getOne(Wrappers.<ChatCustomer>query().lambda().eq(ChatCustomer::getUsername, username));
        if (customer == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_QUERY_ERROR));
        }
        return R.ok(customerService.getCustomerInfo(customer));
    }

    /**
     * 获取指定用户全部信息
     *
     * @return 用户信息
     */
    @GetMapping("/info/{username}")
    public R<CustomerInfoVo> infoByUsername(@PathVariable String username) {
        ChatCustomer customer = customerService
                .getOne(Wrappers.<ChatCustomer>query().lambda().eq(ChatCustomer::getUsername, username));
        if (customer == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, username));
        }
        return R.ok(customerService.getCustomerInfo(customer));
    }

    /**
     * 根据用户名模糊查询
     *
     * @param page     分页相关查询参数
     * @param customer 用户相关查询查询
     * @return 用户列表
     */
    @GetMapping("/query")
    public R<IPage<CustomerVo>> getCustomers(Page<ChatCustomer> page, ChatCustomer customer) {
        LambdaQueryWrapper<ChatCustomer> queryWrapper = Wrappers.<ChatCustomer>lambdaQuery()
                .like(StrUtil.isNotBlank(customer.getUsername()), ChatCustomer::getUsername, customer.getUsername());
        Page<ChatCustomer> customerPage = customerService.page(page, queryWrapper);
        List<CustomerVo> records = customerPage.getRecords().stream().map(c -> {
            CustomerVo customerVo = new CustomerVo();
            BeanUtils.copyProperties(c, customerVo);
            return customerVo;
        }).collect(Collectors.toList());
        Page<CustomerVo> customerVoPage = new Page<>(
                customerPage.getCurrent(), customerPage.getSize(), customerPage.getTotal());
        customerVoPage.setRecords(records);
        return R.ok(customerVoPage);
    }

}
