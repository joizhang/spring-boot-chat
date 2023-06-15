package com.joizhang.chat.auth.support.sms;

import com.joizhang.chat.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationProvider;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;

/**
 * 短信登录的核心处理
 *
 * @author lengleng
 */
@Slf4j
public class OAuth2ResourceOwnerSmsAuthenticationProvider
        extends OAuth2ResourceOwnerBaseAuthenticationProvider<OAuth2ResourceOwnerSmsAuthenticationToken> {

    /**
     * Constructs an {@code OAuth2AuthorizationCodeAuthenticationProvider} using the
     * provided parameters.
     *
     * @param authenticationManager the authentication manager
     * @param authorizationService  the authorization service
     * @param tokenGenerator        the token generator
     * @since 0.2.3
     */
    public OAuth2ResourceOwnerSmsAuthenticationProvider(AuthenticationManager authenticationManager,
                                                        OAuth2AuthorizationService authorizationService,
                                                        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        super(authenticationManager, authorizationService, tokenGenerator);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean supports = OAuth2ResourceOwnerSmsAuthenticationToken.class.isAssignableFrom(authentication);
        log.debug("supports authentication=" + authentication + " returning " + supports);
        return supports;
    }

    @Override
    public void checkClient(RegisteredClient registeredClient) {
        assert registeredClient != null;
        AuthorizationGrantType grantType = new AuthorizationGrantType(SecurityConstants.APP);
        if (!registeredClient.getAuthorizationGrantTypes().contains(grantType)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }
    }

    @Override
    public UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters) {
        String phone = (String) reqParameters.get(SecurityConstants.SMS_PARAMETER_NAME);
        return new UsernamePasswordAuthenticationToken(phone, null);
    }

}
