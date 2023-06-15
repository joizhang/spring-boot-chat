package com.joizhang.chat.web.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageVo implements Serializable {

    /**
     * 消息ID
     */
    private Long id;

    /**
     * 发送消息的用户
     */
    private Long senderId;

    /**
     * 目标用户
     */
    private Long receiverId;

    /**
     * 序号
     */
    private Long seqNum;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型
     */
    private Integer contentType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
