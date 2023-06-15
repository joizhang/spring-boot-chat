package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.joizhang.chat.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 客户端信息
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysOauthClientDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端ID
     */
    @NotBlank(message = "client_id 不能为空")
    @TableId(value = "client_id", type = IdType.INPUT)
    @Schema(description = "客户端id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @NotBlank(message = "client_secret 不能为空")
    @Schema(description = "客户端密钥")
    private String clientSecret;

    /**
     * 资源ID
     */
    @Schema(description = "资源id列表")
    private String resourceIds;

    /**
     * 作用域
     */
    @NotBlank(message = "scope 不能为空")
    @Schema(description = "作用域")
    private String scope;

    /**
     * 授权方式（A,B,C）
     */
    @Schema(description = "授权方式")
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    @Schema(description = "回调地址")
    private String webServerRedirectUri;

    /**
     * 权限
     */
    @Schema(description = "权限列表")
    private String authorities;

    /**
     * 请求令牌有效时间
     */
    @Schema(description = "请求令牌有效时间")
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效时间
     */
    @Schema(description = "刷新令牌有效时间")
    private Integer refreshTokenValidity;

    /**
     * 扩展信息
     */
    @Schema(description = "扩展信息")
    private String additionalInformation;

    /**
     * 是否自动放行
     */
    @Schema(description = "是否自动放行")
    private String autoapprove;

}
