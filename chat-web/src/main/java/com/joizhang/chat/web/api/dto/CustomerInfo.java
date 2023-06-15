package com.joizhang.chat.web.api.dto;

import com.joizhang.chat.web.api.entity.ChatCustomer;
import lombok.Data;

import java.io.Serializable;

@Deprecated
@Data
public class CustomerInfo implements Serializable {

    /**
     * 客户基本信息
     */
    private ChatCustomer chatCustomer;

}
