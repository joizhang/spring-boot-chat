package com.joizhang.chat.web.service;

import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.web.api.dto.AppSmsDTO;

public interface AppService {

    /**
     * 发送手机验证码
     *
     * @param sms phone
     * @return code
     */
    R<Boolean> sendSmsCode(AppSmsDTO sms);

    /**
     * 校验验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    boolean check(String phone, String code);

}
