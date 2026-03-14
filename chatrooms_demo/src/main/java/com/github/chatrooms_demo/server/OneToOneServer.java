package com.github.chatrooms_demo.server;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// OneToOneServer.java
public class OneToOneServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("服务器已启动，等待客户端连接...");
            
            // 这一步会阻塞，直到有客户端连进来
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接：" + socket.getInetAddress());

            // 获取输入流（收消息）和输出流（发消息）
            // 指定使用 UTF-8 编码来读取网络流
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
            // 指定使用 UTF-8 编码来发送消息
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), 
                true
            );

            // 简单的循环聊天
            // 关键：显式指定读取键盘输入的编码
            Scanner scanner = new Scanner(System.in, "GBK");
            
            String clientMsg;
            while ((clientMsg = in.readLine()) != null) {
                System.out.println("客户端说: " + clientMsg);
                System.out.print("回一个: ");
                String serverMsg = scanner.nextLine();
                out.println(serverMsg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}