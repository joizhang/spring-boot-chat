package com.joizhang.chat.web.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 客户端请求验证码
 */
@Data
public class AppSmsDTO {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 手机号是否存在数据库
     */
    private Boolean exist;

}
