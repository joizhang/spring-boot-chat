package com.joizhang.chat.web.api.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerVo implements Serializable {

    private Long id;

    private String username;

    private String phone;

    private String avatar;

}
