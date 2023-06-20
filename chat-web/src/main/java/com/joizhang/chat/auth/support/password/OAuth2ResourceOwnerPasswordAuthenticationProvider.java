package com.joizhang.chat.auth.support.password;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.joizhang.chat.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.Objects;

/**
 * 处理用户名密码授权
 *
 * @author jumuning
 */
@Slf4j
public class OAuth2ResourceOwnerPasswordAuthenticationProvider
        extends OAuth2ResourceOwnerBaseAuthenticationProvider<OAuth2ResourceOwnerPasswordAuthenticationToken> {

    public static final String KEY = "thanks,pig4cloud";
    public static final String AES = "AES";

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     *
     * @param authenticationManager authenticationManager
     * @param authorizationService  the authorization service
     * @param tokenGenerator        the token generator
     * @since 0.2.3
     */
    public OAuth2ResourceOwnerPasswordAuthenticationProvider(AuthenticationManager authenticationManager,
                                                             OAuth2AuthorizationService authorizationService,
                                                             OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = OAuth2ResourceOwnerPasswordAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        Objects.requireNonNull(registeredClient);
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String username = (String) reqParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) reqParameters.get(OAuth2ParameterNames.PASSWORD);
        // 构建前端对应解密AES 因子
        AES aes = new AES(Mode.CFB, Padding.NoPadding,
                new SecretKeySpec(KEY.getBytes(), AES),
                new IvParameterSpec(KEY.getBytes())
        );
        String decryptPassword = aes.decryptStr(password);
        return new UsernamePasswordAuthenticationToken(username, decryptPassword);
    }
}
