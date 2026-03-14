package com.github.chatrooms_demo.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class MyChatHandler extends TextWebSocketHandler {

    // 全局在线名单，必须 static
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    // 房间管理：Key是密令，Value是该房间内的Session集合，必须 static
    private static final Map<String, CopyOnWriteArraySet<WebSocketSession>> rooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("【系统】新连接建立: " + session.getId() + "，当前总人数：" + sessions.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
    
        if (payload.contains(":")) {
            String[] parts = payload.split(":", 2);
            String roomCode = parts[0].trim();
            String content = parts[1];

            // 1. 加入房间
            rooms.computeIfAbsent(roomCode, k -> new CopyOnWriteArraySet<>()).add(session);

            // 2. 过滤心跳和入场检测消息
            if ("heartbeat".equals(content) || "[已入场]".equals(content)) {
                // 如果需要，可以回发一个心跳包确认
                // session.sendMessage(new TextMessage("heartbeat"));
                return; 
            }

            // 3. 正常转发
            for (WebSocketSession s : rooms.get(roomCode)) {
                if (s.isOpen() && !s.getId().equals(session.getId())) {
                    s.sendMessage(new TextMessage(content));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        
        // 【重要】用户断开时，从所有房间中彻底移除，防止内存泄漏
        rooms.values().forEach(room -> room.remove(session));
        
        // 可选：清理不再有人存在的房间（节省内存）
        rooms.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        System.out.println("【系统】连接关闭: " + session.getId() + "，剩余总人数：" + sessions.size());
    }
}