package com.joizhang.chat.web.controller;

import com.joizhang.chat.common.core.constant.CacheConstants;
import com.joizhang.chat.common.core.constant.SecurityConstants;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ImageCodeController {

    private static final Integer DEFAULT_IMAGE_WIDTH = 150;

    private static final Integer DEFAULT_IMAGE_HEIGHT = 56;

    private final RedisTemplate<String, Object> redisTemplate;

    @SneakyThrows
    @GetMapping("/code")
    public void getCode(HttpServletRequest request, HttpServletResponse response) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        String result = captcha.text();
        // 保存验证码信息

        Optional<String> randomStr = Optional.of(request.getParameter("randomStr"));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        randomStr.ifPresent(s ->
                redisTemplate.opsForValue().set(
                        CacheConstants.DEFAULT_CODE_KEY + s, result,
                        SecurityConstants.CODE_TIME,
                        TimeUnit.SECONDS
                )
        );
        // 转换流信息写出
//        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(response.getOutputStream());
    }
}
