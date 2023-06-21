package com.joizhang.chat.web.api.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 具备好友关系的用户信息VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FriendCustomVo extends CustomerVo {

    /**
     * 添加好友的时间
     */
    private LocalDateTime createTime;

}
