package com.joizhang.chat.admin.api.vo;

import lombok.Data;

/**
 * 前端展示令牌管理
 */
@Data
public class TokenVo {

    private String id;

    private Long userId;

    private String clientId;

    private String username;

    private String accessToken;

    private String issuedAt;

    private String expiresAt;

}
