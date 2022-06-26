1.压缩命令：

　　命令格式：tar -zcvf  压缩文件名.tar.gz  被压缩文件名

​		命令格式：gzip 路径 解压

 

2.解压缩命令：

　　命令格式：tar -zxvf  压缩文件名.tar.gz

​		命令格式：gunzip 路径 解压



### Linux学习：文件描述符表

参考文章：https://blog.csdn.net/weixin_42374938/article/details/118821715

一个进程会对应一个[文件描述符](https://so.csdn.net/so/search?q=文件描述符&spm=1001.2101.3001.7020)表，每打开一个文件会占用一个位置。一个文件描述表本质是一个数组，最多可以容纳1024（编号：0-1023）个文件描述符

**住：前 3 个（0-2）默认是打开状态的（被占用），分别是标准输入、标准输出、标准错误。**





### Linux下的五种I/O模型：

1. 阻塞I/O（blocking I/O）
2. 非阻塞I/O （nonblocking I/O）
3. I/O复用（select、poll和epoll） （I/O multiplexing）
4. 信号驱动I/O （signal driven I/O （SIGIO））
5. 异步I/O （asynchronous I/O）



### 进程间通信的6种方式：

1. 信号处理器
2. 信号量
3. 共享内存
4. 套接字 UNIX Domain socket
5. 消息队列
6. 管道
   1. 命令管道
   2. 匿名管道
   3. 流管道



### 中断

任何操作系统内核的核心任务，都包含有对连接到计算机上的硬件设备进行有效管理，如硬盘、蓝光碟机、键盘、鼠标、3D处理器，以及无线电等。

操作系统提供的一种机制，让外接设备和处理器之间能更有效的进行工作。



mysql的客户端和服务器在同一台的情况下用的就是进程间通信的unix domain socket的方式，进程间通信，mysql.sock。