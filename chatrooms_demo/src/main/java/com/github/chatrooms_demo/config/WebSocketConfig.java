package com.github.chatrooms_demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 将 /chat 路径映射到处理类，并允许跨域
        registry.addHandler(new MyChatHandler(), "/chat").setAllowedOrigins("*");
    }
}