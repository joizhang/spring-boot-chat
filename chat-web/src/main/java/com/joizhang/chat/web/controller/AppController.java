package com.joizhang.chat.web.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joizhang.chat.common.core.exception.ErrorCodes;
import com.joizhang.chat.common.core.util.MsgUtils;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.security.annotation.Inner;
import com.joizhang.chat.web.api.dto.AppSmsDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.service.AppService;
import com.joizhang.chat.web.service.ChatCustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/svc/app")
@Tag(name = "移动端登录模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppController {

    private final AppService appService;

    private final ChatCustomerService customerService;

    /**
     * 发送手机验证码
     *
     * @param sms 请求手机对象
     * @return code
     */
    @Inner(value = false)
    @PostMapping("/sms")
    public R<Boolean> sendSmsCode(@Valid @RequestBody AppSmsDTO sms) {
        return appService.sendSmsCode(sms);
    }

    /**
     * 获取指定用户全部信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Inner
    @GetMapping("/info/{phone}")
    public R<CustomerInfoVo> infoByMobile(@PathVariable String phone) {
        ChatCustomer customer = customerService
                .getOne(Wrappers.<ChatCustomer>query().lambda().eq(ChatCustomer::getPhone, phone));
        if (customer == null) {
            return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_USER_USERINFO_EMPTY, phone));
        }
        return R.ok(customerService.getCustomerInfo(customer));
    }

}
