package com.joizhang.chat.web.api.dto;

import com.joizhang.chat.web.api.entity.ChatCustomer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerDTO extends ChatCustomer {

    /**
     * 新密码
     */
    private String newpassword1;

    /**
     * 验证码
     */
    private String code;

}
