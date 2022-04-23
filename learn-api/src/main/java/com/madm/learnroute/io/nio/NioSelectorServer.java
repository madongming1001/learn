package com.madm.learnroute.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioSelectorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        NioSelectorServer nioSelectorServer = new NioSelectorServer();
        // 创建NIO ServerSocketChannel
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));
        // 设置ServerSocketChannel为非阻塞
        serverSocket.configureBlocking(false);
        // 打开Selector处理Channel，即创建epoll epoll_create命令创建
        Selector selector = Selector.open();
        // 把ServerSocketChannel注册到selector上，并且selector对客户端accept连接操作感兴趣
        // 并没有把事件和selector绑定
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功");

        while (true) {
            // epoll的事件注册函数，epoll_ctl向epoll对象中添加、修改或者删除感兴趣的事件，返回0表示成功，否则返回–1，此时需要根据errno错误码判断错误类型。
            // 它不同与select()是在监听事件时告诉内核要监听什么类型的事件，而是在这里先注册要监听的事件类型。
            // epoll_wait方法返回的事件必然是通过epoll_ctl添加到epoll中的。
            // 阻塞等待需要处理的事件发生，epoll_ctl进行事件绑定。
            // epoll_wait当socket收到数据后，中断程序调用回调函数回给epoll实例的时间就绪列表 rdlist 里添加该socket引用（这块是操作系统实现linux的，当程序执行到epoll_wait时，
            // 如果rdlist已经引用了socket，那么epoll_wait直接返回，如果rdlist为空，阻塞线程）
            // 中断是系统用来响应硬件设备请求的一种机制，操作系统收到硬件的中断请求，会打断正在执行的进程，然后调用内核中的中断处理程序来响应请求
            // 第一个参数是epoll_create()的返回值，
            // 第二个参数表示动作，用三个宏来表示：
            //              EPOLL_CTL_ADD：注册新的fd到epfd中；
            //              EPOLL_CTL_MOD：修改已经注册的fd的监听事件；
            //              EPOLL_CTL_DEL：从epfd中删除一个fd；
            // 第三个参数是需要监听的fd，
            // 第四个参数是告诉内核需要监听什么事件
            // 返回rdList里面的事件
            // fd代表的是文件描述符号（file descriptor），linux内核为高效管理已打开的"文件"所创建的索引，用该索引可以找到文件
            // fd就是对应的channel无论是serversocketchannel还是socketchannel
            selector.select();
            // 获取selector中注册的全部事件的 SelectionKey 实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("请求数：" + selectionKeys.size());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            // 遍历SelectionKey对事件进行处理
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 如果是OP_ACCEPT事件，则进行连接获取和事件注册
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                    socketChannel.configureBlocking(false);
                    // 这里只注册了读事件，如果需要给客户端发送数据可以注册写事件
                    // 将socketChannel添加到内部的集合里
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                } else if (key.isReadable()) {  // 如果是OP_READ事件，则进行读取和打印
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(byteBuffer);
                    // 如果有数据，把数据打印出来
                    if (len > 0) {
                        System.out.println("接收到消息：" + new String(byteBuffer.array()));
                    } else if (len == -1) { // 如果客户端断开连接，关闭Socket
                        System.out.println("客户端断开连接");
                        socketChannel.close();
                    }
                }
                //从事件集合里删除本次处理的key，防止下次select重复处理
                iterator.remove();
            }
        }
    }
}
