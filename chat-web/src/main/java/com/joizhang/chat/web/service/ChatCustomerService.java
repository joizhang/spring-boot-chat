package com.joizhang.chat.web.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.web.api.dto.CustomerDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.vo.CustomerInfoVo;
import com.joizhang.chat.web.api.vo.CustomerVo;

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
     * @return 用户的全部信息
     */
    CustomerInfoVo getCustomerInfo(ChatCustomer customer);


    /**
     * 更新用户信息
     * @param customer 用户信息
     * @return success/false
     */
    Boolean updateCustomer(ChatCustomer customer);

    /**
     * 根据用户名模糊查询
     *
     * @param page     分页相关查询参数
     * @param customer 用户相关查询查询
     * @return 用户列表
     */
    IPage<CustomerVo> queryCustomers(Page<ChatCustomer> page, ChatCustomer customer);
}
