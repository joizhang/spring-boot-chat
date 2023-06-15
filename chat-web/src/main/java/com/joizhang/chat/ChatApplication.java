package com.joizhang.chat;

import com.joizhang.chat.common.security.annotation.EnableResourceServer;
import com.joizhang.chat.common.swagger.annotation.EnableSwaggerDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableSwaggerDoc
@EnableResourceServer
@SpringBootApplication
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

}
