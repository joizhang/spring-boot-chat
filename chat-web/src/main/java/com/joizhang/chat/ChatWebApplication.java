package com.joizhang.chat;

import com.joizhang.chat.common.security.annotation.EnableResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableResourceServer
@SpringBootApplication
public class ChatWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatWebApplication.class, args);
    }

}
