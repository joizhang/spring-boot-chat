package com.joizhang.chat.auth.config;

import com.joizhang.chat.auth.support.core.CustomOAuth2AccessTokenGenerator;
import com.joizhang.chat.auth.support.core.CustomOAuth2TokenCustomizer;
import com.joizhang.chat.auth.support.core.FormIdentityLoginConfigurer;
import com.joizhang.chat.auth.support.core.MyDaoAuthenticationProvider;
import com.joizhang.chat.auth.support.handler.MyAuthenticationFailureEventHandler;
import com.joizhang.chat.auth.support.handler.MyAuthenticationSuccessEventHandler;
import com.joizhang.chat.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.joizhang.chat.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.joizhang.chat.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationConverter;
import com.joizhang.chat.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.util.Arrays;

/**
 * 认证服务器配置
 *
 * @author lengleng
 */
@RequiredArgsConstructor
@Configuration
public class AuthorizationServerConfiguration {

    private final OAuth2AuthorizationService authorizationService;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // OAuth 2.1 默认配置
        // 缺省配置：authorizeRequests.anyRequest().authenticated()、
        // csrf.ignoringRequestMatchers(endpointsMatcher) 等等
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 使用 HttpSecurity 获取 OAuth 2.1 配置中的 OAuth2AuthorizationServerConfigurer 对象
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class);

        authorizationServerConfigurer
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint // 个性化认证授权端点
                        .accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                        .accessTokenResponseHandler(new MyAuthenticationSuccessEventHandler()) // 登录成功处理器
                        .errorResponseHandler(new MyAuthenticationFailureEventHandler()) // 登录失败处理器
                )
                .clientAuthentication(oAuth2ClientAuthenticationConfigurer -> oAuth2ClientAuthenticationConfigurer // 个性化客户端认证
                        .errorResponseHandler(new MyAuthenticationFailureEventHandler())
                ) // 处理客户端认证异常
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint // 授权码端点个性化confirm页面
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)
                );

        DefaultSecurityFilterChain securityFilterChain = authorizationServerConfigurer
                .authorizationService(authorizationService)// redis存储token的实现
                .authorizationServerSettings(
                        AuthorizationServerSettings
                                .builder()
                                .issuer(SecurityConstants.PROJECT_LICENSE)
                                .build()
                )
                // 授权码登录的登录页个性化
                .and()
                .apply(new FormIdentityLoginConfigurer())
                .and()
                .build();

        // 注入自定义授权模式实现
        addCustomOAuth2GrantAuthenticationProvider(http);
        return securityFilterChain;
    }

    /**
     * 令牌生成规则实现 </br>
     * client:username:uuid
     *
     * @return OAuth2TokenGenerator
     */
    @Bean
    public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator() {
        CustomOAuth2AccessTokenGenerator accessTokenGenerator = new CustomOAuth2AccessTokenGenerator();
        // 注入Token 增加关联用户信息
        accessTokenGenerator.setAccessTokenCustomizer(new CustomOAuth2TokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
    }

    /**
     * request -> xToken 注入请求转换器
     *
     * @return DelegatingAuthenticationConverter
     */
    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(
                Arrays.asList(
                        new OAuth2ResourceOwnerPasswordAuthenticationConverter(),
                        new OAuth2ResourceOwnerSmsAuthenticationConverter(),
                        new OAuth2RefreshTokenAuthenticationConverter(),
                        new OAuth2ClientCredentialsAuthenticationConverter(),
                        new OAuth2AuthorizationCodeAuthenticationConverter(),
                        new OAuth2AuthorizationCodeRequestAuthenticationConverter()
                )
        );
    }

    /**
     * 注入授权模式实现提供方
     * <p>
     * 1. 密码模式 </br>
     * 2. 短信登录 </br>
     */
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
                new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                        authenticationManager, authorizationService, oAuth2TokenGenerator());

        OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider =
                new OAuth2ResourceOwnerSmsAuthenticationProvider(
                        authenticationManager, authorizationService, oAuth2TokenGenerator());

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new MyDaoAuthenticationProvider());
        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }

}
