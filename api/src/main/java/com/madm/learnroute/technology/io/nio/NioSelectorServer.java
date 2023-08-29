package com.madm.learnroute.technology.io.nio;

import cn.hutool.core.net.URLDecoder;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 参考文章：https://note.youdao.com/ynoteshare/index.html?id=8ef33654f746921ad769ad9fe91a4c8f&type=note&_time=1660016387177
 *
 * @author madongming
 */
@Slf4j
public class NioSelectorServer {

    private static final ConcurrentMap<Integer, StringBuffer> MESSAGEHASHCONTEXT = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        // 创建NIO ServerSocketChannel
        //通道，被建立的一个应用程序和操作系统交互事件、传递内容的渠道（注意是连接到操作系统）。
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000), 1024);
        // 设置ServerSocketChannel为非阻塞
        serverSocket.configureBlocking(false);
        // 打开Selector处理Channel，即创建epoll epoll_create 命令创建
        /**
         * epfd
         * struct eventpoll {
         *     sys_epoll_wait用到的等待队列
         *     wait_queue_head_t wq; 唤醒就绪队列线程
         *     红黑树的根节点，这颗树中存储着所有添加到epoll中的需要监控的事件
         *     struct rb_root  rbr; 每次有新链接同步更新fd，并且绑定一个callback回掉函数
         *     双链表中则存放着将要通过 epoll_wait 返回给用户的满足条件的事件
         *     struct list_head rdlist;
         * };
         * 其中wq为等待队列链表，软中断数据就绪的时候会通过 wq 来找到阻塞在 epoll 对象上的用户进程。（epoll_wait若就绪队列中无数据，会将当前进程加入到等待队列中）
         *
         * int epoll_create(int size); // 内核中间加一个ep对象，把所有需要监听的 socket 都放到 ep 对象中
         * int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event); // epoll_ctl负责把socket增加、删除到内核红黑树
         * int epoll_wait(int epfd, struct epoll_event * events, int maxevents, int timeout);// epoll_wait 负责检测可读队列，没有可读socket则阻塞进程,收集发生的事件的连接
         * 而所有添加到epoll中的事件都会与设备(网卡)驱动程序建立回调关系，
         * 也就是说，当相应的事件发生时会调用这个回调方法。这个回调方法在内核中叫ep_poll_callback,它会将发生的事件epitem添加到rdlist双链表中。
         *
         */
        Selector selector = Selector.open();
        //把ServerSocketChannel注册到selector上，并且selector对客户端accept连接操作感兴趣
        //并没有把事件和selector绑定
        //它实际上是一个表示选择器在检查通道就绪状态时需要关心的操作的比特掩码。
        //比如一个选择器对通道的read和write操作感兴趣，那么选择器在检查该通道时，只会检查通道的read和write操作是否已经处在就绪状态。
        //this代表当前调用的对象 SO_REUSEPORT
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功");
        try {
            while (true) {
                // epoll的事件注册函数，epoll_ctl向epoll对象中添加、修改或者删除感兴趣的事件，返回0表示成功，否则返回–1，此时需要根据errno错误码判断错误类型。
                // 它不同与select()是在监听事件时告诉内核要监听什么类型的事件，而是在这里先注册要监听的事件类型。
                // epoll_wait方法返回的事件必然是通过epoll_ctl添加到epoll中的。
                // 阻塞等待需要处理的事件发生，epoll_ctl进行事件绑定。
                // epoll_wait当socket收到数据后，中断程序调用回调函数会给epoll实例的时间就绪列表 rdlist 里添加该socket引用（这块是操作系统实现linux的，当程序执行到epoll_wait时，
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
                if (selector.select(100) == 0) {
                    //================================================
                    //      这里视业务情况，可以做一些然并卵的事情
                    //================================================
                    continue;
                }
                // 获取selector中注册的全部事件的 SelectionKey 实例
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println("请求数：" + selectionKeys.size());
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // 遍历SelectionKey对事件进行处理
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    //从事件集合里删除本次处理的key，防止下次select重复处理
                    iterator.remove();
                    SelectableChannel selectableChannel = key.channel();
                    // 如果是OP_ACCEPT事件，则进行连接获取和事件注册
                    if (key.isValid() && key.isAcceptable()) {
                        log.info("======channel通道已经准备好=======");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
                        SocketChannel socketChannel = serverSocketChannel.accept();//如果设置为阻塞模式 configureBlocking 没有数据可读会阻塞在这里 不能处理其他的请求
                        //write() 如果设置为阻塞模式 configureBlocking 没有数据可读会阻塞在这里 不能处理其他的请求
                        registerSocketChannel(socketChannel, selector);
                    } else if (key.isValid() && key.isConnectable()) {
                        log.info("======socket channel 建立连接=======");
                    } else if (key.isValid() && key.isReadable()) {  // 如果是OP_READ事件，则进行读取和打印
                        log.info("======socket channel 数据准备完成，可以去读==读取=======");
                        readSocketChannel(key);
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        //缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存（其实就是数组）。
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                        int len = socketChannel.read(byteBuffer);//如果设置为阻塞模式 configureBlocking 没有数据可读会阻塞在这里 不能处理其他的请求
                        // 如果有数据，把数据打印出来
                        if (len > 0) {
                            System.out.println("接收到消息：" + new String(byteBuffer.array()));
                        } else if (len == -1) { // 如果客户端断开连接，关闭Socket
                            System.out.println("客户端断开连接");
                            socketChannel.close();
                        }
                    }
                }
                System.out.println("结束了");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }

    /**
     * 在server socket channel接收到/准备好 一个新的 TCP连接后。
     * 就会向程序返回一个新的socketChannel。<br>
     * 但是这个新的socket channel并没有在selector“选择器/代理器”中注册，
     * 所以程序还没法通过selector通知这个socket channel的事件。
     * 于是我们拿到新的socket channel后，要做的第一个事情就是到selector“选择器/代理器”中注册这个
     * socket channel感兴趣的事件
     *
     * @param socketChannel 新的socket channel
     * @param selector      selector“选择器/代理器”
     * @throws Exception
     */
    @SneakyThrows
    private static void registerSocketChannel(SocketChannel socketChannel, Selector selector) {
        socketChannel.configureBlocking(false);
        //socket通道可以且只可以注册三种事件SelectionKey.OP_READ | SelectionKey.OP_WRITE | SelectionKey.OP_CONNECT
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(2048));
    }

    /**
     * 这个方法用于读取从客户端传来的信息。
     * 并且观察从客户端过来的socket channel在经过多次传输后，是否完成传输。
     * 如果传输完成，则返回一个true的标记。
     *
     * @param selectionKey
     * @throws Exception
     */
    private static void readSocketChannel(SelectionKey selectionKey) throws Exception {
        SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
        //获取客户端使用的端口
        InetSocketAddress sourceSocketAddress = (InetSocketAddress) clientSocketChannel.getRemoteAddress();
        Integer resourcePort = sourceSocketAddress.getPort();

        //拿到这个socket channel使用的缓存区，准备读取数据
        //在后文，将详细讲解缓存区的用法概念，实际上重要的就是三个元素capacity,position和limit。
        ByteBuffer contextBytes = (ByteBuffer) selectionKey.attachment();
        //将通道的数据写入到缓存区，注意是写入到缓存区。
        //由于之前设置了ByteBuffer的大小为2048 byte，所以可以存在写入不完的情况
        //没关系，我们后面来调整代码。这里我们暂时理解为一次接受可以完成
        int realLen = -1;
        try {
            realLen = clientSocketChannel.read(contextBytes);
        } catch (Exception e) {
            //这里抛出了异常，一般就是客户端因为某种原因终止了。所以关闭channel就行了
            log.error(e.getMessage());
            clientSocketChannel.close();
            return;
        }
        //如果缓存区中没有任何数据(但实际上这个不太可能，否则就不会触发OP_READ事件了)
        if (realLen == -1) {
            log.warn("====缓存区没有数据? ====");
            return;
        }

        //将缓存区从写状态切换为读状态(实际上这个方法是读写模式互切换)。
        //这时java nio框架中的这个socket channel的写请求将全部等待。
        contextBytes.flip();
        //注意中文乱码的问题，我个人喜好是使用 URLDecoder/URLEncoder，进行解编码。
        //当然java nio框架本身也提供编解码方式，看个人咯
        byte[] messageBytes = contextBytes.array();
        String messageEncode = new String(messageBytes, "UTF-8");
        String message = URLDecoder.decode(messageEncode, CharsetUtil.UTF_8);

        //如果发现本次接收的信息中有over关键字，说明信息接收完了
        if (URLDecoder.decode(message, CharsetUtil.UTF_8).indexOf("over") != -1) {
            //则从messageHashContext中，取出之前已经收到的信息，组合成完整的信息
            Integer channelUUID = clientSocketChannel.hashCode();
            log.info("端口:" + resourcePort + "客户端发来的信息======message : " + message);
            StringBuffer completeMessage = new StringBuffer();
            //清空MESSAGEHASHCONTEXT中的历史记录
            StringBuffer historyMessage = MESSAGEHASHCONTEXT.remove(channelUUID);
            if (historyMessage == null) {
                completeMessage.append(message);
            } else {
                completeMessage = historyMessage.append(message);
            }
            log.info("端口:" + resourcePort + "客户端发来的完整信息======completeMessage : " + URLDecoder.decode(completeMessage.toString(), CharsetUtil.UTF_8));

            //======================================================
            //          当然接受完成后，可以在这里正式处理业务了
            //======================================================

            //回发数据，并关闭channel
            ByteBuffer sendBuffer = ByteBuffer.wrap(URLEncoder.encode("回发处理结果", "UTF-8").getBytes());
            clientSocketChannel.write(sendBuffer);
            clientSocketChannel.close();
        } else {
            //如果没有发现有“over”关键字，说明还没有接受完，则将本次接受到的信息存入messageHashContext
            log.info("端口:" + resourcePort + "客户端信息还未接受完，继续接受======message : " + URLDecoder.decode(message, CharsetUtil.UTF_8));
            //每一个channel对象都是独立的，所以可以使用对象的hash值，作为唯一标示
            Integer channelUUID = clientSocketChannel.hashCode();

            //然后获取这个channel下以前已经达到的message信息
            StringBuffer historyMessage = MESSAGEHASHCONTEXT.get(channelUUID);
            if (historyMessage == null) {
                historyMessage = new StringBuffer();
                MESSAGEHASHCONTEXT.put(channelUUID, historyMessage.append(message));
            }
        }
    }
}
