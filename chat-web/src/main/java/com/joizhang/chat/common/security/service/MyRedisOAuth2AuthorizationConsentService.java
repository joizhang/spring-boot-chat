package com.joizhang.chat.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 管理 OAuth2 "consent" 授权信息
 */
@RequiredArgsConstructor
public class MyRedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final static Long TIMEOUT = 10L;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        String key = buildKey(authorizationConsent);
        redisTemplate.opsForValue().set(key, authorizationConsent, TIMEOUT, TimeUnit.MINUTES);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        redisTemplate.delete(buildKey(authorizationConsent));
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String key = buildKey(registeredClientId, principalName);
        return (OAuth2AuthorizationConsent) redisTemplate.opsForValue().get(key);
    }

    private static String buildKey(String registeredClientId, String principalName) {
        return "token:consent:" + registeredClientId + ":" + principalName;
    }

    private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
        return buildKey(authorizationConsent.getRegisteredClientId(),
                authorizationConsent.getPrincipalName());
    }
}
