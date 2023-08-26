# 简单介绍Netty？

第一：Netty 是一个 基于 NIO 模型的高性能网络通信框架，其实可以认为它是对NIO网络模型的封装，提供了简单易用的API，我们可以利用这些封装好的API快速开发自己的网络程序。

第二：Netty在NIO的基础上做了很多优化，比如零拷贝机制、高性能无锁队列、PooledByteBuf内存池等，因此性能会比NIO更高。

第三：Netty可以支持多种通信协议，如Http、WebSocket等，并且针对数据通信的拆包黏包问题，Netty内置了拆包策略。

**Netty高并发高性能架构设计精髓**

- 主从Reactor线程模型
- NIO多路复用非阻塞
- **无锁串行化**设计思想
- 支持高性能序列化协议
- [零拷贝](https://blog.csdn.net/prestigeding/article/details/121195080?spm=1001.2014.3001.5501)(直接内存的使用)
- ByteBuf内存池设计
- 灵活的TCP参数配置能力
- 并发优化



![https://note.youdao.com/yws/public/resource/bbc5cfef81b2951d769807ed748343b9/xmlnote/C48453E100EB42049B7349168EA17EC1/85277](https://note.youdao.com/yws/public/resource/bbc5cfef81b2951d769807ed748343b9/xmlnote/C48453E100EB42049B7349168EA17EC1/85277)

**模型解释：**

1) Netty 抽象出两组线程池BossGroup和WorkerGroup，BossGroup专门负责接收客户端的连接, WorkerGroup专门负责网络的读写

2) BossGroup和WorkerGroup类型都是NioEventLoopGroup

3) NioEventLoopGroup 相当于一个事件循环**线程组**, 这个组中含有多个事件循环线程 ， 每一个事件循环线程是NioEventLoop

4) 每个NioEventLoop都有一个selector , 用于监听注册在其上的socketChannel的网络通讯

5) 每个Boss  NioEventLoop线程内部循环执行的步骤有 3 步

- 处理accept事件 , 与client 建立连接 , 生成 NioSocketChannel 
- 将NioSocketChannel注册到某个worker  NIOEventLoop上的selector
- 处理任务队列的任务 ， 即runAllTasks

6) 每个worker  NIOEventLoop线程循环执行的步骤

- 轮询注册到自己selector上的所有NioSocketChannel 的read, write事件
- 处理 I/O 事件， 即read , write 事件， 在对应NioSocketChannel 处理业务
- runAllTasks处理任务队列TaskQueue的任务 ，一些耗时的业务处理一般可以放入TaskQueue中慢慢处理，这样不影响数据在 pipeline 中的流动处理

7) 每个worker NIOEventLoop处理NioSocketChannel业务时，会使用 pipeline (管道)，管道中维护了很多 handler 处理器用来处理 channel 中的数据







![https://note.youdao.com/yws/public/resource/ab45dc97644411c44fbd27ee95d8244e/xmlnote/9C889DD8449F4254A3E2881690435DA6/106808](https://note.youdao.com/yws/public/resource/ab45dc97644411c44fbd27ee95d8244e/xmlnote/9C889DD8449F4254A3E2881690435DA6/106808)

**Netty高并发高性能架构设计精髓**

- 主从Reactor线程模型
- NIO多路复用非阻塞
- **无锁串行化**设计思想
- 支持高性能序列化协议
- 零拷贝(直接内存的使用)
- ByteBuf内存池设计
- 灵活的TCP参数配置能力
- 并发优化



**Netty学习之旅------第4篇---ByteBuf的扩容、缩容和类继承关系https://blog.csdn.net/qq157538651/article/details/93537187**









![https://note.youdao.com/yws/public/resource/916f44987d1fe0e35ec935bf5391d762/xmlnote/E7B425DD0F1142E28C7174E865FF0A05/106573](https://note.youdao.com/yws/public/resource/916f44987d1fe0e35ec935bf5391d762/xmlnote/E7B425DD0F1142E28C7174E865FF0A05/106573)





```java
EPollSelectorImpl.java
  
// opcodes
private static final int EPOLL_CTL_ADD      = 1;
private static final int EPOLL_CTL_DEL      = 2;
private static final int EPOLL_CTL_MOD      = 3;  
  
protected void implRegister(SelectionKeyImpl ski) {
    if (closed)
        throw new ClosedSelectorException();
    SelChImpl ch = ski.channel;
    int fd = Integer.valueOf(ch.getFDVal());
    fdToKey.put(fd, ski);
    pollWra
        pper.add(fd);
    keys.add(ski);
}

int poll(long timeout) throws IOException {
        updateRegistra
        tions();
        updated = epollWait(pollArrayAddress, NUM_EPOLLEVENTS, timeout, epfd);
        for (int i=0; i<updated; i++) {
            if (getDescriptor(i) == incomingInterruptFD) {
                interruptedIndex = i;
                interrupted = true;
                break;
            }
        }
        return updated;
    }

    /**
     * Update the pending registrations.
     */
    private void updateRegistrations() {
        synchronized (updateLock) {
            int j = 0;
            while (j < updateCount) {
                int fd = updateDescriptors[j];
                short events = getUpdateEvents(fd);
                boolean isRegistered = registered.get(fd);
                int opcode = 0;

                if (events != KILLED) {
                    if (isRegistered) {
                        opcode = (events != 0) ? EPOLL_CTL_MOD : EPOLL_CTL_DEL;
                    } else {
                        opcode = (events != 0) ? EPOLL_CTL_ADD : 0;
                    }
                    if (opcode != 0) {
                        epollCtl(epfd, opcode, fd, events);
                        if (opcode == EPOLL_CTL_ADD) {
                            registered.set(fd);
                        } else if (opcode == EPOLL_CTL_DEL) {
                            registered.clear(fd);
                        }
                    }
                }
                j++;
            }
            updateCount = 0;
        }
    }
```



```java
EPollArrayWrapper.java
// maximum size of updatesLow
private static final int MAX_UPDATE_ARRAY_SIZE = AccessController.doPrivileged(
  new GetIntegerAction("sun.nio.ch.maxUpdateArraySize", Math.min(OPEN_MAX, 64*1024)));
private final byte[] eventsLow = new byte[MAX_UPDATE_ARRAY_SIZE];。//65536
private Map<Integer,Byte> eventsHigh;

/**
 * Add a file descriptor
 */
void add(int fd) {
    // force the initial update events to 0 as it may be KILLED by a
    // previous registration.
    synchronized (updateLock) {
        assert !registered.get(fd);
        setUpdateEvents(fd, (byte)0, true);
    }
}


/**
     * Sets the pending update events for the given file descriptor. This
     * method has no effect if the update events is already set to KILLED,
     * unless {@code force} is {@code true}.
     */
private void setUpdateEvents(int fd, byte events, boolean force) {
  if (fd < MAX_UPDATE_ARRAY_SIZE) {
    if ((eventsLow[fd] != KILLED) || force) {
      eventsLow[fd] = events;
    }
  } else {
    Integer key = Integer.valueOf(fd);
    if (!isEventsHighKilled(key) || force) {
      eventsHigh.put(key, Byte.valueOf(events));
    }
  }
}
```

**I/O多路复用底层主要用的Linux 内核·函数（select，poll，epoll）来实现，windows不支持epoll实现，windows底层是基于winsock2的select函数实现的(不开源)，**

|              | **select**                               | **poll**                                 | **epoll(jdk 1.5及以上)**                                     |
| ------------ | ---------------------------------------- | ---------------------------------------- | ------------------------------------------------------------ |
| **操作方式** | 遍历                                     | 遍历                                     | 回调                                                         |
| **底层实现** | 数组                                     | 链表                                     | 哈希表                                                       |
| **IO效率**   | 每次调用都进行线性遍历，时间复杂度为O(n) | 每次调用都进行线性遍历，时间复杂度为O(n) | 事件通知方式，每当有IO事件就绪，系统注册的回调函数就会被调用，时间复杂度O(1) |
| **最大连接** | 有上限                                   | 无上限                                   | 无上限                                                       |

**NIO底层在JDK1.4版本是用linux的内核函数select()或poll()来实现，跟上面的NioServer代码类似，selector每次都会轮询所有的sockchannel看下哪个channel有读写事件，有的话就处理，没有就继续遍历，JDK1.5开始引入了epoll基于事件响应机制来优化NIO。**

### BIO、NIO、AIO对比

![https://note.youdao.com/yws/public/resource/916f44987d1fe0e35ec935bf5391d762/xmlnote/17DCC73717114E569D317F28E1D27261/84315](https://note.youdao.com/yws/public/resource/916f44987d1fe0e35ec935bf5391d762/xmlnote/17DCC73717114E569D317F28E1D27261/84315)

**为什么Netty使用NIO而不是AIO？**

在Linux系统上，**AIO的底层实现仍使用Epoll**，没有很好实现AIO，因此在性能上没有明显的优势，而且被JDK封装了一层不容易深度优化，Linux上AIO还不够成熟。Netty是**异步非阻塞**框架，Netty在NIO上做了很多异步的封装。

**为什么BIO不结合1:1线程来使用？**

1. **线程的创建和销毁成本很高**，在Linux这样的操作系统中，线程本质上就是一个进程。创建和销毁都是重量级的系统函数。
2. **线程本身占用较大内存**，像Java的线程栈，一般至少分配512K～1M的空间，如果系统中的线程数过千，恐怕整个JVM的内存都会被吃掉一半。 
3. **线程的切换成本是很高的。**操作系统发生线程切换的时候，需要保留线程的上下文，然后执行系统调用。如果线程数过高，可能执行线程切换的时间甚至会大于线程执行的时间，这时候带来的表现往往是系统load偏高、CPU sy使用率特别高（超过20%以上)，导致系统几乎陷入不可用的状态。
4. **容易造成锯齿状的系统负载。**因为系统负载是用活动线程数或CPU核心数，一旦线程数量高但外部网络环境不是很稳定，就很容易造成大量请求的结果同时返回，激活大量阻塞线程从而使系统负载压力过大。



### Netty 核心组件了解吗？分别有什么作用？

Netty由三层结构构成：**网络通信层**、**事件调度器**与**服务编排层**

在网络通信层有三个核心组件：Bootstrap、ServerBootStrap、Channel

Bootstrap负责客户端启动并用来链接远程netty server

ServerBootStrap负责服务端监听，用来监听指定端口，

Channel是负责网络通信的载体

事件调度器有两个核心组件：EventLoopGroup与EventLoop

EventLoopGroup本质上是一个线程池，主要负责接收I/O请求，并分配线程执行处理请求。

EventLoop。相当于线程池中的线程

在服务编排层有三个核心组件ChannelPipeline、ChannelHandler、ChannelHandlerContext

![https://note.youdao.com/yws/public/resource/b8970e44473486a48178193d68929008/xmlnote/B401EBCE53ED4A959BDD4CC702D70B6E/85302](https://note.youdao.com/yws/public/resource/b8970e44473486a48178193d68929008/xmlnote/B401EBCE53ED4A959BDD4CC702D70B6E/85302)

ChannelPipeline负责将多个Channelhandler链接在一起，数据结构上是一个双向链表

![image-20221119162810671](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221119162810671.png)

ChannelHandler针对IO数据的处理器，数据接收后，通过指定的Handler进行处理。

ChannelHandlerContext用来保存ChannelHandler的上下文信息



**EventExecutor NioEventLoop**

```java
DefaultEventExecutorChooserFactory.java
//后续从这个对象里面拿去对应的任务
@Override
  public EventExecutorChooser newChooser(EventExecutor[] executors) {
    if (isPowerOfTwo(executors.length)) {
      return new PowerOfTwoEventExecutorChooser(executors);
    } else {
      return new GenericEventExecutorChooser(executors);
    }
}

private static boolean isPowerOfTwo(int val) {
  	return (val & -val) == val;
}
```



```java
public static final int OP_READ = 1 << 0;
public static final int OP_WRITE = 1 << 2;
public static final int OP_CONNECT = 1 << 3;
public static final int OP_ACCEPT = 1 << 4;
```



## **解决粘包半包**

1. LineBasedFrameDecoder 换行符
2. DelimiterBasedFrameDecoder 自定义分隔符
3. FixedLengthFrameDecoder 定长
4. LengthFieldBasedFrameDecoder LengthFieldPrepender字段标注大小

## 序列化

**如何选择序列化框架**

1. 是否需要跨语言支持
2. 空间：编码后占用空间
3. 时间：编解码速度
4. 是否追求可读性

**参考文章：**（**几种Java常用序列化框架的选型与对比**）https://developer.aliyun.com/article/783611?spm=5176.21213303.J_6704733920.7.34fe53c9BlcfJj&scm=20140722.S_community%40%40%E6%96%87%E7%AB%A0%40%40783611._.ID_community%40%40%E6%96%87%E7%AB%A0%40%40783611-RL_%E5%87%A0%E7%A7%8Djava%E5%B8%B8%E7%94%A8%E5%BA%8F%E5%88%97%E5%8C%96%E6%A1%86%E6%9E%B6%E7%9A%84%E9%80%89%E5%9E%8B%E4%B8%8E%E5%AF%B9%E6%AF%94-LOC_main-OR_ser-V_2-P0_0

## SSL和TLS

SSL 是指安全套接字层，简而言之，它是一项标准技术，可确保互联网连接安全，保护两个系统之间发送的任何敏感数据，防止网络犯罪分子读取和修改任何传输信息，包括个人资料。两个系统可能是指服务器和客户端（例如，浏览器和购物网站），或两个服务器之间（例如，含个人身份信息或工资单信息的应用程序）。

此举可确保在用户和站点之间，或两个系统之间传输的数据无法被读取。它使用加密算法打乱传输中的数据，防止数据通过连接传输时被黑客读取。这里所说的数据是指任何敏感或个人信息，例如信用卡号和其他财务信息、个人姓名和住址等。

TLS（传输层安全）是更为安全的升级版 SSL。由于 SSL 这一术语更为常用，因此我们仍然将我们的安全证书称作 SSL。但当您从DigiCert[购买 SSL](https://www.websecurity.digicert.com/zh/cn/ssl-certificate?inid=infoctr_buylink_sslhome) 时，您真正购买的是最新的 TLS 证书，有[ ECC、RSA 或 DSA 三种加密方式](https://www.websecurity.digicert.com/zh/cn/security-topics/how-ssl-works)可以选择。

```java
//添加编码解码handler
HttpClientCodec
HttpServerCodec

HttpResponseEncoder
HttpRequestDecoder

SSLContext
SSLEngine
```

## 零拷贝

**零拷贝是指计算机执行IO操作时，CPU不需要将数据从一个存储区域复制到另一个存储区域，从而可以减少上下文切换以及CPU的拷贝时间。它是一种`I/O`操作优化技术。**

#### 传统IO的执行流程

前端请求过来，服务端的任务就是：将服务端主机磁盘中的文件从已连接的socket发出去。关键实现代码如下：

```java
while ((n = read(diskfd, buf, BUF_SIZE)) > 0)
  write(sockfd, buf , n);
```

传统的IO流程，包括read和write的过程。

- `read`：把数据从磁盘读取到内核缓冲区，再拷贝到用户缓冲区
- `write`：先把数据写入到socket缓冲区，最后写入网卡设备。

![图片](https://mmbiz.qpic.cn/mmbiz_png/PoF8jo1Pmpz58G4cfxZPyw8z2FARXj6o3I9C4muOOJOvTAibTzOohJp3NSoUxduhQVMJjibOnQGlmiaCG2ic3vNwVQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

- 用户应用进程调用read函数，向操作系统发起IO调用，**上下文从用户态转为内核态（切换1）**

- DMA控制器把数据从磁盘中，读取到内核缓冲区。

- CPU把内核缓冲区数据，拷贝到用户应用缓冲区，**上下文从内核态转为用户态（切换2）**，read函数返回

- 用户应用进程通过write函数，发起IO调用，**上下文从用户态转为内核态（切换3）**

- CPU将用户缓冲区中的数据，拷贝到socket缓冲区

- DMA控制器把数据从socket缓冲区，拷贝到网卡设备，**上下文从内核态切换回用户态（切换4）**，write函数返回

  

**内核空间是操作系统内核访问的区域，是受保护的内存空间，而用户空间是用户应用程序访问的内存区域。** 以32位操作系统为例，它会为每一个进程都分配了**4G**(2的32次方)的内存空间。

- 内核空间：主要提供进程调度、内存分配、连接硬件资源等功能

- 用户空间：提供给各个程序进程的空间，它不具有访问内核空间资源的权限，如果应用程序需要使用到内核空间的资源，则需要通过系统调用来完成。进程从用户空间切换到内核空间，完成相关操作后，再从内核空间切换回用户空间。

  

### 3.3 什么是上下文切换

- 什么是CPU上下文？

> CPU 寄存器，是CPU内置的容量小、但速度极快的内存。而程序计数器，则是用来存储 CPU 正在执行的指令位置、或者即将执行的下一条指令位置。它们都是 CPU 在运行任何任务前，必须的依赖环境，因此叫做CPU上下文。

- 什么是**CPU上下文切换**？

> 它是指，先把前一个任务的CPU上下文（也就是CPU寄存器和程序计数器）保存起来，然后加载新任务的上下文到这些寄存器和程序计数器，最后再跳转到程序计数器所指的新位置，运行新任务。

一般我们说的**上下文切换**，就是指内核（操作系统的核心）在CPU上对进程或者线程进行切换。进程从用户态到内核态的转变，需要通过**系统调用**来完成。系统调用的过程，会发生**CPU上下文的切换**。

> CPU 寄存器里原来用户态的指令位置，需要先保存起来。接着，为了执行内核态代码，CPU 寄存器需要更新为内核态指令的新位置。最后才是跳转到内核态运行内核任务。



### 3.4 虚拟内存

现代操作系统使用虚拟内存，即虚拟地址取代物理地址，使用虚拟内存可以有2个好处：

- 虚拟内存空间可以远远大于物理内存空间
- 多个虚拟内存可以指向同一个物理地址

正是**多个虚拟内存可以指向同一个物理地址**，可以把内核空间和用户空间的虚拟地址映射到同一个物理地址，这样的话，就可以减少IO的数据拷贝次数啦，示意图如下

![图片](https://mmbiz.qpic.cn/mmbiz_png/PoF8jo1Pmpz58G4cfxZPyw8z2FARXj6oiaVKVOFNK24ZS4R8dicVTGcdVLS9086PKwF7n7RhabLIeia39GpaaeoFA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)



### 3.5 DMA技术

DMA，英文全称是**Direct Memory Access**，即直接内存访问。**DMA**本质上是一块主板上独立的芯片，允许外设设备和内存存储器之间直接进行IO数据传输，其过程**不需要CPU的参与**。

我们一起来看下IO流程，DMA帮忙做了什么事情.

![图片](https://mmbiz.qpic.cn/mmbiz_png/PoF8jo1Pmpz58G4cfxZPyw8z2FARXj6oCM6k6klXcDfnR5MQtAIWgPOt85gPq8t2XmQtiaA0y5I2Sth670xHggg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

- 用户应用进程调用read函数，向操作系统发起IO调用，进入阻塞状态，等待数据返回。
- CPU收到指令后，对DMA控制器发起指令调度。
- DMA收到IO请求后，将请求发送给磁盘；
- 磁盘将数据放入磁盘控制缓冲区，并通知DMA
- DMA将数据从磁盘控制器缓冲区拷贝到内核缓冲区。
- DMA向CPU发出数据读完的信号，把工作交换给CPU，由CPU负责将数据从内核缓冲区拷贝到用户缓冲区。
- 用户应用进程由内核态切换回用户态，解除阻塞状态

可以发现，DMA做的事情很清晰啦，它主要就是**帮忙CPU转发一下IO请求，以及拷贝数据**。为什么需要它的？

> 主要就是效率，它帮忙CPU做事情，这时候，CPU就可以闲下来去做别的事情，提高了CPU的利用效率。大白话解释就是，CPU老哥太忙太累啦，所以他找了个小弟（名叫DMA） ，替他完成一部分的拷贝工作，这样CPU老哥就能着手去做其他事情。



## 4. 零拷贝实现的几种方式

零拷贝并不是没有拷贝数据，而是减少用户态/内核态的切换次数以及CPU拷贝的次数。零拷贝实现有多种方式，分别是

- mmap+write
- sendfile
- 带有DMA收集拷贝功能的sendfile

### 4.1 mmap+write实现的零拷贝

mmap 的函数原型如下：

```
void *mmap(void *addr, size_t length, int prot, int flags, int fd, off_t offset);
```

- addr：指定映射的虚拟内存地址
- length：映射的长度
- prot：映射内存的保护模式
- flags：指定映射的类型
- fd:进行映射的文件句柄
- offset:文件偏移量

前面一小节，零拷贝相关的知识点回顾，我们介绍了**虚拟内存，可以把内核空间和用户空间的虚拟地址映射到同一个物理地址，从而减少数据拷贝次数**！mmap就是用了虚拟内存这个特点，它将内核中的读缓冲区与用户空间的缓冲区进行映射，所有的IO都在内核中完成。

`mmap+write`实现的零拷贝流程如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/PoF8jo1Pmpz58G4cfxZPyw8z2FARXj6o5oibnrtzgibe8FykghnvT2PA2ntIEyJA3O5hAErF4qfwW7DzyMeuHmMA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

- 用户进程通过`mmap方法`向操作系统内核发起IO调用，**上下文从用户态切换为内核态**。
- CPU利用DMA控制器，把数据从硬盘中拷贝到内核缓冲区。
- **上下文从内核态切换回用户态**，mmap方法返回。
- 用户进程通过`write`方法向操作系统内核发起IO调用，**上下文从用户态切换为内核态**。
- CPU将内核缓冲区的数据拷贝到的socket缓冲区。
- CPU利用DMA控制器，把数据从socket缓冲区拷贝到网卡，**上下文从内核态切换回用户态**，write调用返回。

可以发现，`mmap+write`实现的零拷贝，I/O发生了**4**次用户空间与内核空间的上下文切换，以及3次数据拷贝。其中3次数据拷贝中，包括了**2次DMA拷贝和1次CPU拷贝**。

`mmap`是将读缓冲区的地址和用户缓冲区的地址进行映射，内核缓冲区和应用缓冲区共享，所以节省了一次CPU拷贝‘’并且用户进程内存是**虚拟的**，只是**映射**到内核的读缓冲区，可以节省一半的内存空间。

### 4.2 sendfile实现的零拷贝

`sendfile`是Linux2.1内核版本后引入的一个系统调用函数，API如下：

```
ssize_t sendfile(int out_fd, int in_fd, off_t *offset, size_t count);
```

- out_fd:为待写入内容的文件描述符，一个socket描述符。，
- in_fd:为待读出内容的文件描述符，必须是真实的文件，不能是socket和管道。
- offset：指定从读入文件的哪个位置开始读，如果为NULL，表示文件的默认起始位置。
- count：指定在fdout和fdin之间传输的字节数。

sendfile表示在两个文件描述符之间传输数据，它是在**操作系统内核**中操作的，**避免了数据从内核缓冲区和用户缓冲区之间的拷贝操作**，因此可以使用它来实现零拷贝。

sendfile实现的零拷贝流程如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/PoF8jo1Pmpz58G4cfxZPyw8z2FARXj6oXYND4BKk0MrXdbgay85wGVI2kwEiaYzd4HEKFjedoJUVE9nk8fvMd9w/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)sendfile实现的零拷贝

1. 用户进程发起sendfile系统调用，**上下文（切换1）从用户态转向内核态**
2. DMA控制器，把数据从硬盘中拷贝到内核缓冲区。
3. CPU将读缓冲区中数据拷贝到socket缓冲区
4. DMA控制器，异步把数据从socket缓冲区拷贝到网卡，
5. **上下文（切换2）从内核态切换回用户态**，sendfile调用返回。

可以发现，`sendfile`实现的零拷贝，I/O发生了**2**次用户空间与内核空间的上下文切换，以及3次数据拷贝。其中3次数据拷贝中，包括了**2次DMA拷贝和1次CPU拷贝**。那能不能把CPU拷贝的次数减少到0次呢？有的，即`带有DMA收集拷贝功能的sendfile`！

### 4.3 sendfile+DMA scatter/gather实现的零拷贝

linux 2.4版本之后，对`sendfile`做了优化升级，引入SG-DMA技术，其实就是对DMA拷贝加入了`scatter/gather`操作，它可以直接从内核空间缓冲区中将数据读取到网卡。使用这个特点搞零拷贝，即还可以多省去**一次CPU拷贝**。

sendfile+DMA scatter/gather实现的零拷贝流程如下：

![图片](https://mmbiz.qpic.cn/mmbiz_png/PoF8jo1Pmpz58G4cfxZPyw8z2FARXj6ogddOFo9rS4qgBS28vz7XibGicQYyw5B7ob8qrFEfEhF0TvaeNibzJClRg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

1. 用户进程发起sendfile系统调用，**上下文（切换1）从用户态转向内核态**
2. DMA控制器，把数据从硬盘中拷贝到内核缓冲区。
3. CPU把内核缓冲区中的**文件描述符信息**（包括内核缓冲区的内存地址和偏移量）发送到socket缓冲区
4. DMA控制器根据文件描述符信息，直接把数据从内核缓冲区拷贝到网卡
5. **上下文（切换2）从内核态切换回用户态**，sendfile调用返回。

可以发现，`sendfile+DMA scatter/gather`实现的零拷贝，I/O发生了**2**次用户空间与内核空间的上下文切换，以及2次数据拷贝。其中2次数据拷贝都是包**DMA拷贝**。这就是真正的 **零拷贝（Zero-copy)** 技术，全程都没有通过CPU来搬运数据，所有的数据都是通过DMA来进行传输的。

## 5. java提供的零拷贝方式

- Java NIO对mmap的支持
- Java NIO对sendfile的支持

### 5.1 Java NIO对mmap的支持

Java NIO有一个`MappedByteBuffer`的类，可以用来实现内存映射。它的底层是调用了Linux内核的**mmap**的API。

**mmap的小demo**如下：

```java
public class MmapTest {

    public static void main(String[] args) {
        try {
            FileChannel readChannel = FileChannel.open(Paths.get("./jay.txt"), StandardOpenOption.READ);
            MappedByteBuffer data = readChannel.map(FileChannel.MapMode.READ_ONLY, 0, 1024 * 1024 * 40);
            FileChannel writeChannel = FileChannel.open(Paths.get("./siting.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            //数据传输
            writeChannel.write(data);
            readChannel.close();
            writeChannel.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
```

### 5.2 Java NIO对sendfile的支持

FileChannel的`transferTo()/transferFrom()`，底层就是sendfile() 系统调用函数。Kafka 这个开源项目就用到它，平时面试的时候，回答面试官为什么这么快，就可以提到零拷贝`sendfile`这个点。

```java
@Override
public long transferFrom(FileChannel fileChannel, long position, long count) throws IOException {
   return fileChannel.transferTo(position, count, socketChannel);
}
```

**sendfile的小demo**如下：

```java
public class SendFileTest {
    public static void main(String[] args) {
        try {
            FileChannel readChannel = FileChannel.open(Paths.get("./jay.txt"), StandardOpenOption.READ);
            long len = readChannel.size();
            long position = readChannel.position();
            
            FileChannel writeChannel = FileChannel.open(Paths.get("./siting.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            //数据传输
            readChannel.transferTo(position, len, writeChannel);
            readChannel.close();
            writeChannel.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```

## Zookeeper服务端整合netty启动参数

```java
-Dzookeeper.serverCnxnFactory=org.apache.zookeeper.server.NettyServerCnxnFactory
```

zookeeper一开始leader选举有三种状态

一、leading

二、following

三、looking

客户端和服务端之间采用的是nio的通信模式

服务端主备模式下节点通信采用的是bio bio适用架构固定 连接数较少 实现起来也比较简单

nio通过多路复用的方式可以同事处理多请求 并且经过多reactor模型的形式可以做到接受请求和处理请求通过不同的线程组，来减少请求压力，提高吞吐量



### Select、poll、epoll的比较

1、支持的最大连接数的区别

2、连接的效率问题

3、消息传递的方式问题



epoll_ctl添加或删除所要监听的socket

epoll_create会创建一个eventpoll实例

![image-20220626170912982](/Users/madongming/notes/noteImg/image-20220626170912982.png)

### 基于Netty的知名项目

数据库：Cassandra

大数据处理：Spark、Hadoop

消息中间件：RocketMQ

检索：Elasticsearch

框架：GRPC，Apache Dubbo、Spring5 WebFlux

分布式协调器： Zookeeper





### Netty核心组件初步了解

Bootstrap、EventLoop（Group）、Channel

事件和ChannelHandler、ChannelPipeline

ChannelFuture



# 传统接受发送数据几次上下文切换

![image-20220811184639244](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220811184639244.png)

**4次拷贝，4次上下文切换**

## MMAP内存映射

![image-20220811185002681](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220811185002681.png)

**3次拷贝，4次上下文切换**

## sendfile（linux 2.1）

![image-20220811185055927](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220811185055927.png)

**3（2）次拷贝，2次上下文切换**

DMA支持的话可以文件读取缓存区传输地址位置给套接字发送缓冲区（SO_SNDBUF）

## slice（2.6.17）

![image-20220811185350092](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220811185350092.png)

**2次拷贝，2次上下文切换**，通过管道共享数据

buffer = File.read

Socket.send(buffer)

select 是以数组形势保存的

poll 以链表形式保存的文件描述符

一个进程能够同时打开的文件描述符是1024个



# javaNIO

## **NIO三大核心组件**

NIO有三大核心组件：Selector选择器、Channel管道、buffer缓冲区。

**Selector**

Selector的英文含义是“选择器”，自称为时间注册器，通道可以注册感兴趣的事件。

**Channels**

和操作系统之间进行时间交互、传输内容的通道，可同时读写，双工机制。

**Buffer**

channels读取数据需要通过buffer，buffer缓冲区，buffer可以读写切换，也可以进行数据指针移动。

## Reactor模型类型

**Reactor模式称为反应器模式或应答者模式，是基于事件驱动的设计模式，拥有一个或多个并发输入源，有一个服务处理器和多个请求处理器，服务处理器会同步的将输入的请求事件以多路复用的方式分发给相应的请求处理器。**

Reactor设计模式是一种为处理并发服务请求，并将请求提交到一个或多个服务处理程序的事件设计模式。当客户端请求抵达后，服务处理程序使用多路分配策略，由一个非阻塞的线程来接收所有请求，然后将请求派发到相关的工作线程并进行处理的过程。

在事件驱动的应用中，将一个或多个客户端的请求分离和调度给应用程序，同步有序地接收并处理多个服务请求。对于高并发系统经常会使用到Reactor模式，用来替代常用的多线程处理方式以节省系统资源并提高系统的吞吐量。

### **单线程Reactor模式流程**

​		服务器端的Reactor是一个线程对象，该线程会启动时间循环，并使用Selector（选择器）来实现IO的多路复用。注册一个Acceptor事件处理器到Reactor中，Acceptor事件处理器所关注的时间是ACCEPT事件，这样Reactor会监听客户端向服务端发起的连接请求事件（ACCEPT）

​	 客户端向服务器端发起一个连接请求，Reactor监听到了该ACCEPT事件的发生并将该ACCEPT事件派发给相应的Acceptor处理器来进行处理。Acceptor处理器通过accept()方法得到与这个客户端对应的连接(SocketChannel)，然后将该连接所关注的READ事件以及对应的READ事件处理器注册到Reactor中，这样一来Reactor就会监听该连接的READ事件了。

​	 当Reactor监听到有读或者写事件发生时，将相关的事件派发给对应的处理器进行处理。比如，读处理器会通过SocketChannel的read()方法读取数据，此时read()操作可以直接读取到数据，而不会堵塞与等待可读的数据到来。

​	 每当处理完所有就绪的感兴趣的I/O事件后，Reactor线程会再次执行select()阻塞等待新的事件就绪并将其分派给对应处理器进行处理。

注意，Reactor的单线程模式的单线程主要是针对于I/O操作而言，也就是所有的I/O的accept()、read()、write()以及connect()操作都在一个线程上完成的。

但在目前的单线程Reactor模式中，不仅I/O操作在该Reactor线程上，**连非I/O的业务操作也在该线程上进行处理了**，这可能会大大延迟I/O请求的响应。所以我们应该将非I/O的业务逻辑操作从Reactor线程上卸载，以此来加速Reactor线程对I/O请求的响应。

![https://note.youdao.com/yws/public/resource/8ef33654f746921ad769ad9fe91a4c8f/xmlnote/OFFICED5F6435B59444264B2E0B3F4A9FE3468/10075](https://note.youdao.com/yws/public/resource/8ef33654f746921ad769ad9fe91a4c8f/xmlnote/OFFICED5F6435B59444264B2E0B3F4A9FE3468/10075)

### 单线程Reactor，工作者线程池模式

业务处理剥离出线程模型中，交给工作者线程池处理

![https://note.youdao.com/yws/public/resource/8ef33654f746921ad769ad9fe91a4c8f/xmlnote/OFFICE4A2F9D2C8A8F4220BB69B68DC0E3AFCD/10076](https://note.youdao.com/yws/public/resource/8ef33654f746921ad769ad9fe91a4c8f/xmlnote/OFFICE4A2F9D2C8A8F4220BB69B68DC0E3AFCD/10076)

### 多线程主从Reactor模式

主Reactor只负责接收客户端请求，acceptor注册事件之后得到的SocketChannel交给subReactor，然后subReactor进行数据的read和send，其中的非IO操作部分交给工作者线程池。

![https://note.youdao.com/yws/public/resource/8ef33654f746921ad769ad9fe91a4c8f/xmlnote/OFFICE051C61BA01BD49428FE89212C4FABA34/10077](https://note.youdao.com/yws/public/resource/8ef33654f746921ad769ad9fe91a4c8f/xmlnote/OFFICE051C61BA01BD49428FE89212C4FABA34/10077)

清理脏数据、无用连接的释放、大 key 的删除

## IO 多路复用

**IO 多路复用是一种同步IO模型，实现一个线程可以监视多个文件句柄； 一旦某个文件句柄就绪，就能够通知应用程序进行相应的读写操作； 没有文件句柄就绪就会阻塞应用程序，交出CPU。**

### 文件描述符

文件描述符（File descriptor）是计算机科学中的一个术语，**是一个用于表述指向文件引用的抽象化概念。 文件描述符在形式上是一个非负整数。**实际上，它是一个索引值，指向内核为每一个进程所维护的该进程打开文件的记录表。当程序打开一个现有文件或者创建一个新文件时，内核向进程返回一个文件描述符。在程序设计中，一些涉及底层的程序编写往往会围绕着文件描述符展开。但是文件描述符这一概念往往只适用于UNIX、Linux这样的操作系统。

# 操作类型SelectionKey

![image-20221030211317251](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221030211317251.png)

# NIO三个核心组件

## Channel

​		通道表示与实体的开放连接，例如硬件设备、文件、网络套接字或能够执行一个或多个不同 I/O 操作（例如读取或写入）的程序组件。fw

​		通道，被建立的一个应用程序和操作系统交互事件、传递内容的渠道(注意是连接到操作系统)。一个通道会有一个专属的文件状态描述符。那么既然是和操作系统进行内容的传递，那么说明应用程序可以通过通道读取数据，也可以通过通道向操作系统写数据。

## Buffer

数据缓存区: 在JAVA NIO 框架中，为了保证每个通道的数据读写速度JAVA NIO 框架为每一种需要支持数据读写的通道集成了Buffer的支持。

## Selector

Selector的英文含义是“选择器”，不过根据我们详细介绍的Selector的岗位职责，您可以把它称之为“轮询代理器”、“事件订阅器”、“channel容器管理机”都行。

# IOCP

**输入输出完成端口**（Input/Output Completion Port，IOCP）, 是支持多个同时发生的异步I/O操作的应用程序编程接口，由于Linux下没有Windows下的IOCP技术提供真正的 异步IO 支持，所以Linux下使用epoll模拟异步IO。

## 典型的多路复用IO实现

目前流程的多路复用IO实现主要包括四种: `select`、`poll`、`epoll`、`kqueue`。下表是他们的一些重要特性的比较:

![image-20221031102529463](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221031102529463.png)

1. **select**

   select通过设置或检查存放fd标志位的数据结构进行下一步处理。

   **缺点**

   - 单个进程可监视的fd熟练被限制，即能监听端口的数量有限。当然可以进行修改，但是受限制，一般32位机器是1024个，64位机器是2048个。
   - 对socket是线性扫描，即轮询，效率较低： 仅知道有I/O事件发生，却不知哪几个流，只会无差异轮询所有流，找出能读/写数据的流进行操作。同时处理的流越多，无差别轮询时间越长 - O(n)。

2. **poll**

   和select类似，只是描述fd集合的方式不同，poll使用`pollfd`结构而非select的`fd_set`结构。管理多个描述符也是进行轮询，根据描述符的状态进行处理，但**poll无最大文件描述符数量的限制**。

   无最大连接数限制，因其基于链表存储，缺点：

   - 大量fd数组被整体复制于用户态和内核地址空间间，而不管是否有意义
   - 若报告了fd后，没有被处理，则下次poll时会再次报告该fd

3. **epoll（基于Linux2.4.5）**

   epoll模型修改主动轮询为被动通知，当有事件发生时，被动接收通知。所以epoll模型注册套接字后，主程序可做其他事情，当事件发生时，接收到通知后再去处理。

# 计算机收包过程

![image-20221031120909782](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221031120909782.png)

**参考文章：**https://pdai.tech/md/db/nosql-redis/db-redis-y-mt-1.html

**网卡接收数据并被用户所接受使用的全过程？**

1. **硬件接收：**当网络中的数据包到达网卡时，网卡硬件会检测到这些数据，并将其存储在**接收缓冲区**中。
2. **硬中断处理：**网卡通过发送硬中断信号来通知操作系统有数据可用。操作系统中的中断处理程序被触发，根据中断控制器的配置，选择相应的中断处理函数。
3. **驱动程序处理：**中断处理函数**会调用网卡驱动程序中的中断处理函数**。该函数会从接收缓冲区中读取数据，并进行解析、处理和存储等操作。
4. **网络协议处理：**驱动程序会将接收到的数据传递给操作系统的网络协议栈。网络协议栈负责对数据进行进一步的处理，例如根据协议类型（如TCP/IP）进行解析、检查校验和、分配内存等。
5. **用户接口处理：**经过网络协议处理后，数据将被传递给应用程序。应用程序可以使用套接字或其他网络编程接口来接收和处理数据。
6. **数据处理：**应用程序可以根据自己的需求对接收到的数据进行处理，例如解码、解析、存储等。
   需要注意的是，整个过程可能涉及到多个层次的协议和组件，例如物理层、链路层、网络层和传输层等。每个层次的协议都有不同的功能和责任，以确保数据的可靠传输和正确处理。
   此外，网络中的数据包在经过网络传输时还可能经历路由、转发和再次封装等过程，这些步骤不在网卡接收数据和用户接受使用的范围内，但对于数据的完整传递也起到重要的作用。
   综上所述，网卡接收数据并被用户所接受使用的全过程涉及到硬件接收、硬中断处理、驱动程序处理、网络协议处理和用户接口处理等多个步骤。每个步骤都有其特定的功能和责任，以确保数据的正确接收和处理。

网卡接收数据的硬中断处理程序一般由网络驱动程序提供。 网络驱动程序是操作系统中**用于管理和控制网络设备（如网卡）的软件模块**
。它负责与硬件设备进行通信，并提供与操作系统交互的接口。 当网卡接收到数据时，它会触发一个硬件中断信号，通知操作系统有数据可用。操作系统通过硬件抽象层（HAL）或驱动程序接收该中断信号，
**并调用相应的网络驱动程序中的中断处理函数**。
中断处理函数是由网络驱动程序开发人员编写的，用于处理接收到的数据。该函数会从网卡的接收缓冲区读取数据，进行解析、处理和存储等操作。然后，它会将数据传递给操作系统的网络协议栈，供上层应用程序使用。
需要注意的是，*
*网络驱动程序一般由操作系统开发人员或硬件厂商开发人员编写。对于常见的网卡设备，操作系统通常会提供通用的网络驱动程序。而对于某些特殊的网卡设备，例如高性能网络接口卡（NIC）或定制化的网卡设备，硬件厂商可能会提供特定的网络驱动程序，以充分发挥设备的性能和功能
**。 综上所述，网卡接收数据的硬中断处理程序一般由网络驱动程序提供，该驱动程序可以由**操作系统开发人员或硬件厂商开发人员**
编写。