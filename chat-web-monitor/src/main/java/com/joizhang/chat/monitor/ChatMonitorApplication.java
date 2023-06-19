package com.joizhang.chat.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class ChatMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatMonitorApplication.class, args);
    }

}
