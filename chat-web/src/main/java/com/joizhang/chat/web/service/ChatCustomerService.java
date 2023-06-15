package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.web.api.dto.CustomerDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;

/**
 * 聊天客户服务类
 */
public interface ChatCustomerService extends IService<ChatCustomer> {

    /**
     * 保存用户信息
     *
     * @param customerDTO DTO 对象
     * @return success/fail
     */
    Boolean saveUser(CustomerDTO customerDTO);

    /**
     * 通过查用户的全部信息
     *
     * @param customer 用户
     * @return
     */
    CustomerInfoVo getCustomerInfo(ChatCustomer customer);

    /**
     * 注册聊天客户
     *
     * @param customerDTO 用户信息
     * @return success/false
     */
    R<Boolean> registerUser(CustomerDTO customerDTO);

}
