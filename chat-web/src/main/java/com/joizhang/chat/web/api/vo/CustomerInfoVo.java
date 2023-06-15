package com.joizhang.chat.web.api.vo;

import com.joizhang.chat.web.api.entity.ChatCustomer;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerInfoVo implements Serializable {

    private ChatCustomer chatCustomer;

}
