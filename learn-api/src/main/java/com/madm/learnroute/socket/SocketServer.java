package com.madm.learnroute.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            //单线程处理可以接受多个客户端，但是只能处理一个客户端的数据
            //多线程处理客户端的数据 handler(socket);
            for (; ; ) {
                Socket socket = serverSocket.accept();
                System.out.println("有客户端连接了");
                new Thread(() -> {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
//                handler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handler(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备read。。");
        int read = socket.getInputStream().read(bytes);
        System.out.println("read完毕。。");
        if (read != 1) {
            System.out.println("接收到客户端的数据：" + new String(bytes, 0, read));
        }
        socket.getOutputStream().write("recevie message！".getBytes());
    }
}
