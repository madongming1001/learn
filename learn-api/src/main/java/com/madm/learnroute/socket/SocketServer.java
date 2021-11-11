package com.madm.learnroute.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9000);
            Socket socket = serverSocket.accept();
            System.out.println("有客户端连接了");
            handler(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handler(Socket socket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备read。。");
        int read = socket.getInputStream().read(bytes);
        System.out.println("read完毕。。");
        if(read != 1){
            System.out.println("接收到客户端的数据：" + new String(bytes,0,read));
        }
    }
}
