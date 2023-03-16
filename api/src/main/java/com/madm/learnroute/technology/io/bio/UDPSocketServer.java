package com.madm.learnroute.technology.io.bio;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author dongming.ma
 * @date 2023/3/15 22:59
 */
public class UDPSocketServer {
    @SneakyThrows
    public static void main(String[] args) {
//        /*
//         * 接收客户端发送的数据
//         */
//        //1.创建服务器端DatagramSocket，指定端口
//        DatagramSocket socket = new DatagramSocket(8800);
//        //2.创建数据报，用于接收客户端发送的数据
//        byte[] buf = new byte[1024];//创建字节数组，指定接收的数据包的大小
//        DatagramPacket packet = new DatagramPacket(buf, buf.length);
//        //3.接收客户端发送的数据
//        System.out.println("****服务器端已经启动，等待客户端发送数据");
//        socket.receive(packet);//此方法在接收到数据报之前会一直阻塞
//        //4.读取数据
//        String info = new String(buf, 0, packet.getLength());
//        System.out.println("我是服务器，客户端说：" + info);
//
//        /*
//         * 向客户端响应数据
//         */
//        //1.定义客户端的地址、端口号、数据
//        InetAddress address = packet.getAddress();
//        int port = packet.getPort();
//        byte[] data2 = "欢迎您!".getBytes();
//        //2.创建数据报，包含响应的数据信息
//        DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
//        //3.响应客户端
//        socket.send(packet2);
//        //4.关闭资源
//        socket.close();
        /*
         * 接收客户端发送的数据
         */

        //2.创建数据报，用于接收客户端发送的数据
        byte[] buf = new byte[1024];//创建字节数组，指定接收的数据包的大小
        System.out.println("****服务器启动，等待客户端连接****");
        int count = 1;
        while (true) {
            //1.创建服务器端DatagramSocket，指定端口
            try (DatagramSocket socket = new DatagramSocket(8800)) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                UDPSocketServerThread serverThread = new UDPSocketServerThread(packet, socket);
                serverThread.start();
                System.out.println("客户端数量：" + count++);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class UDPSocketServerThread extends Thread {
    DatagramPacket packet;
    DatagramSocket socket;

    public UDPSocketServerThread(DatagramPacket packet, DatagramSocket socket) {
        super();
        this.packet = packet;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //获取客户端信息
            byte[] buf = packet.getData();
            System.out.println("我是服务器，客户端说：" + new String(buf, 0, packet.getLength()));
            //响应客户端
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            byte[] data1 = "欢迎您！".getBytes();
            DatagramPacket packet1 = new DatagramPacket(data1, data1.length, address, port);
            socket.send(packet1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

