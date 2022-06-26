### 简单介绍Netty？

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

ChannelPipeline负责将多个Channelhandler链接在一起

ChannelHandler针对IO数据的处理器，数据接收后，通过指定的Handler进行处理。

ChannelHandlerContext用来保存ChannelHandler的上下文信息







EventExecutor NioEventLoop

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

前面一小节，零拷贝相关的知识点回顾，我们介绍了**虚拟内存**，可以把内核空间和用户空间的虚拟地址映射到同一个物理地址，从而减少数据拷贝次数！mmap就是用了虚拟内存这个特点，它将内核中的读缓冲区与用户空间的缓冲区进行映射，所有的IO都在内核中完成。

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

