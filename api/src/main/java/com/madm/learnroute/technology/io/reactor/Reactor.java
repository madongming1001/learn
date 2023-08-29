package com.madm.learnroute.technology.io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author dongming.ma
 * @date 2023/8/29 21:03
 */
public class Reactor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        serverSocket = ServerSocketChannel.open();  // 创建服务端的ServerSocketChannel
        serverSocket.configureBlocking(false);  // 设置为非阻塞模式
        selector = Selector.open();  // 创建一个Selector多路复用器
        SelectionKey key = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        serverSocket.bind(new InetSocketAddress(port));  // 绑定服务端端口
        key.attach(new Acceptor(serverSocket));  // 为服务端Channel绑定一个Acceptor
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();  // 服务端使用一个线程不断等待客户端的连接到达
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());  // 监听到客户端连接事件后将其分发给Acceptor
                    iterator.remove();
                }

                selector.selectNow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey key) throws IOException {
        // 这里的attachement也即前面为服务端Channel绑定的Acceptor，调用其run()方法进行
        // 客户端连接的获取，并且进行分发
        Runnable attachment = (Runnable) key.attachment();
        attachment.run();
    }
}