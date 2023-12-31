package com.joizhang.chat.web.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.common.security.util.SecurityUtils;
import com.joizhang.chat.web.api.constant.FriendRequestStatus;
import com.joizhang.chat.web.api.constant.MessageContentType;
import com.joizhang.chat.web.api.dto.ChatFriendRequestDTO;
import com.joizhang.chat.web.api.entity.ChatCustomer;
import com.joizhang.chat.web.api.entity.ChatFriend;
import com.joizhang.chat.web.api.entity.ChatMessage;
import com.joizhang.chat.web.api.vo.FriendCustomerVo;
import com.joizhang.chat.web.mapper.ChatFriendMapper;
import com.joizhang.chat.web.service.ChatCustomerService;
import com.joizhang.chat.web.service.ChatFriendService;
import com.joizhang.chat.web.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatFriendServiceImpl extends ServiceImpl<ChatFriendMapper, ChatFriend>
        implements ChatFriendService {

    private final ChatMessageService messageService;

    private final ChatCustomerService customerService;

    @Override
    public boolean exist(ChatFriend chatFriend) {
        LambdaQueryWrapper<ChatFriend> queryWrapper = Wrappers.<ChatFriend>lambdaQuery()
                .eq(ChatFriend::getUserId, chatFriend.getUserId())
                .eq(ChatFriend::getFriendId, chatFriend.getFriendId())
                .eq(ChatFriend::getRequestStatus, FriendRequestStatus.ACCEPTED.getStatus());
        return baseMapper.exists(queryWrapper);
    }

    @Transactional
    @Override
    public void saveFriendRequest(ChatFriendRequestDTO chatFriendRequestDTO) {
        if (FriendRequestStatus.PENDING.getStatus().equals(chatFriendRequestDTO.getRequestStatus())) {
            // 如果是PENDING，则保存好友请求发起者的好友关系信息
            this.save(chatFriendRequestDTO);
        } else if (FriendRequestStatus.ACCEPTED.getStatus().equals(chatFriendRequestDTO.getRequestStatus())) {
            // 如果是ACCEPTED，则保存好友请求接受者的好友关系信息
            this.save(chatFriendRequestDTO);
            // 然后更新好友请求发起者的好友关系信息
            LambdaQueryWrapper<ChatFriend> wrapper = Wrappers.<ChatFriend>lambdaQuery()
                    .eq(ChatFriend::getUserId, chatFriendRequestDTO.getFriendId())
                    .eq(ChatFriend::getFriendId, chatFriendRequestDTO.getUserId())
                    .eq(ChatFriend::getRequestStatus, FriendRequestStatus.PENDING.getStatus());
            ChatFriend friendRequestSender = this.getOne(wrapper);
            friendRequestSender.setRequestStatus(FriendRequestStatus.ACCEPTED.getStatus());
            this.updateById(friendRequestSender);
        } else {
            throw new IllegalArgumentException("Illegal friend request");
        }
    }

    @Override
    public List<FriendCustomerVo> getCustomersByAcceptFriends(ChatFriend chatFriendParam) {
        Long userId = SecurityUtils.getUser().getId();
        // 查询当前用户好友IDs
        LambdaQueryWrapper<ChatFriend> queryWrapper = Wrappers.<ChatFriend>lambdaQuery()
                .eq(ChatFriend::getUserId, userId)
                .eq(ChatFriend::getRequestStatus, FriendRequestStatus.ACCEPTED.getStatus())
                .gt(
                        ObjectUtil.isNotNull(chatFriendParam.getCreateTime()),
                        ChatFriend::getCreateTime,
                        chatFriendParam.getCreateTime()
                );
        List<ChatFriend> chatFriends = this.list(queryWrapper);
        List<Long> friendIds = chatFriends.stream()
                .map(ChatFriend::getFriendId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(friendIds)) {
            return Collections.emptyList();
        }

        Map<Long, ChatFriend> chatFriendMap = new HashMap<>();
        chatFriends.forEach(chatFriend -> chatFriendMap.put(chatFriend.getFriendId(), chatFriend));
        // 查询好友的详细信息
        LambdaQueryWrapper<ChatCustomer> queryWrapper2 = Wrappers.<ChatCustomer>lambdaQuery()
                .in(ChatCustomer::getId, friendIds);
        List<ChatCustomer> chatCustomers = customerService.list(queryWrapper2);

        // 转换
        return chatCustomers.stream()
                .map((customer) -> {
                    FriendCustomerVo friendCustomVo = new FriendCustomerVo();
                    BeanUtils.copyProperties(customer, friendCustomVo);
                    ChatFriend chatFriend = chatFriendMap.get(customer.getId());
                    friendCustomVo.setCreateTime(chatFriend.getCreateTime());
                    friendCustomVo.setUpdateTime(chatFriend.getUpdateTime());
                    return friendCustomVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendCustomerVo> getCustomersByPendingFriends(ChatFriend chatFriendParam) {
        Long userId = SecurityUtils.getUser().getId();
        // 查询当前用户待处理请求的好友IDs
        LambdaQueryWrapper<ChatFriend> queryWrapper = Wrappers.<ChatFriend>lambdaQuery()
                .eq(ChatFriend::getFriendId, userId)
                .eq(ChatFriend::getRequestStatus, FriendRequestStatus.PENDING.getStatus())
                .gt(
                        ObjectUtil.isNotNull(chatFriendParam.getCreateTime()),
                        ChatFriend::getCreateTime,
                        chatFriendParam.getCreateTime()
                );
        List<ChatFriend> chatFriends = this.list(queryWrapper);
        List<Long> friendIds = chatFriends.stream()
                .map(ChatFriend::getUserId)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(friendIds)) {
            return Collections.emptyList();
        }

        // 查询好友的详细信息
        Map<Long, ChatFriend> chatFriendMap = new HashMap<>();
        chatFriends.forEach(chatFriend -> chatFriendMap.put(chatFriend.getUserId(), chatFriend));
        LambdaQueryWrapper<ChatCustomer> queryWrapper2 = Wrappers.<ChatCustomer>lambdaQuery()
                .in(ChatCustomer::getId, friendIds);
        List<ChatCustomer> chatCustomers = customerService.list(queryWrapper2);

        // 转换
        return chatCustomers.stream()
                .map((customer) -> {
                    FriendCustomerVo friendCustomVo = new FriendCustomerVo();
                    BeanUtils.copyProperties(customer, friendCustomVo);
                    ChatFriend chatFriend = chatFriendMap.get(customer.getId());
                    friendCustomVo.setCreateTime(chatFriend.getCreateTime());
                    friendCustomVo.setUpdateTime(chatFriend.getUpdateTime());
                    return friendCustomVo;
                })
                .collect(Collectors.toList());
    }
}
