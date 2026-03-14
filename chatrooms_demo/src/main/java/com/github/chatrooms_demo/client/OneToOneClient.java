package com.github.chatrooms_demo.client;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// OneToOneClient.java
public class OneToOneClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8888)) {
            System.out.println("已连接到服务器！");

            // 指定使用 UTF-8 编码来读取网络流
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
            // 指定使用 UTF-8 编码来发送消息
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), 
                true
            );

            // 关键：显式指定读取键盘输入的编码
            Scanner scanner = new Scanner(System.in, "GBK");
            
            while (true) {
                System.out.print("你想说啥: ");
                String msg = scanner.nextLine();
                out.println(msg); // 发送消息
                
                String response = in.readLine(); // 等待回信
                System.out.println("服务器回你: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}