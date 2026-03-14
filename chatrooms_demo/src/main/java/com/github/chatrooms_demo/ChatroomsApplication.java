package com.github.chatrooms_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 这是核心注解，告诉 Java 这是一个 Spring Boot 项目
public class ChatroomsApplication {
    public static void main(String[] args) {
        // 这一行会启动服务器并自动加载你写的 WebSocketConfig
        SpringApplication.run(ChatroomsApplication.class, args);
        System.out.println("🚀 聊天室后端已启动！");
        System.out.println("🌐 访问地址: http://localhost:8080/index.html");
    }
}