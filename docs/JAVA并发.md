Java线程池实现原理

# 

## ThreadPoolExecutor类继承关系

![image-20211119112015735](noteImg/image-20211119112015735.png)

## ScheduledThreadPoolExecutor类继承关系

![image-20211119112153576](noteImg/image-20211119112153576.png)

## 类主要定义

**interface Executor类**

```java
void execute(Runnable command);
```

**abstract class AbstractExecutorService类**

```java
public<T> Future<T> submit(Callable<T> task){
        if(task==null)throw new NullPointerException();
        RunnableFuture<T> ftask=newTaskFor(task);
        execute(ftask);
        return ftask;
        }
```

**Worker**

```java
/** Thread this worker is running in.  Null if factory fails. */
final Thread thread;
/** Initial task to run.  Possibly null. */
        Runnable firstTask;
/** Per-thread task counter */
volatile long completedTasks;
```

ThreadPoolExecutor实现的顶层接口是**Executor**，顶层接口Executor提供了一种思想：**将任务提交和任务执行进行解耦。**
用户无需关注如何创建线程，如何调度线程来执行任务，用户只需提供Runnable对象，将任务的运行逻辑提交到执行器(Executor)
中，由Executor框架完成线程的调配和任务的执行部分。**ExecutorService接口增加了一些能力：（1）扩充执行任务的能力，补充可以为一个或一批异步任务生成Future的方法；
**（2）提供了管控线程池的方法，比如停止线程池的运行。*
*
AbstractExecutorService则是上层的抽象类，将执行任务的流程串联了起来，保证下层的实现只需关注一个执行任务的方法即可。最下层的实现类ThreadPoolExecutor实现最复杂的运行部分，ThreadPoolExecutor将会一方面维护自身的生命周期，另一方面同时管理线程和任务，使两者良好的结合从而执行并行任务。
**

**ThreadPoolExecutor的运行状态有5种，分别为：**

![img](https://p0.meituan.net/travelcube/62853fa44bfa47d63143babe3b5a4c6e82532.png)

## Worker线程管理

**线程池为了掌握线程的状态并维护线程的生命周期**，设计了线程池内的工作线程Worker。我们来看一下它的部分代码：

```Java
private final class Worker extends AbstractQueuedSynchronizer implements Runnable {
    final Thread thread;//Worker持有的线程
    Runnable firstTask;//初始化的任务，可以为null
}
```

Worker这个工作线程，实现了Runnable接口，并持有一个线程thread，一个初始化的任务firstTask。thread是在调用构造方法时通过ThreadFactory来创建的线程，可以用来执行任务；firstTask用它来保存传入的第一个任务，这个任务可以有也可以为null。如果这个值是非空的，那么线程就会在启动初期立即执行这个任务，
**也就对应核心线程创建时的情况**；如果这个值是null，那么就需要创建一个线程去执行任务列表（workQueue）中的任务，**也就是非核心线程的创建。
**

### 线程池的生命周期

**RUNNING：**接受新任务并处理排队任务

**SHUTDOWN：**不接受新任务，但处理排队任务

**STOP：**不接受新任务，不处理排队任务，并中断正在进行的任务

**TIDYING：**所有任务都已终止，workerCount 为零，转换到状态 TIDYING 的线程将运行 terminated() 挂钩方法

**TERMINATED：**terminated() 已完成 这些值之间的数字顺序很重要，以允许有序比较。

**参考文章：**https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html

## Thread的run和start的区别？

### run方法运行使用的是方法调用，线程是main线程

![image-20211119143751822](noteImg/image-20211119143751822.png)

### start方法运行使用的是线程调用，线程是新建的线程

![image-20211119143915735](noteImg/image-20211119143915735.png)

![image-20211119143928425](noteImg/image-20211119143928425.png)

### java如何创建thread线程，jvm回掉run方法

**从C/C++方法中调用的一些Java方法主要有：**

（1）Java主类中的main()方法；

（2）Java主类装载时，调用JavaCalls::call()函数执行checkAndLoadMain()方法；

（3）类的**初始化过程**中，调用JavaCalls::call()函数执行的Java类初始化方法<clinit>，可以查看JavaCalls::
call_default_constructor()函数，有对<clinit>方法的调用逻辑；

（4）我们先省略main方法的执行流程（其实main方法的执行也是先启动一个JavaMain线程，套路都是一样的），单看某个JavaThread的启动过程。JavaThread的启动最终都要通过一个native方法java.lang.Thread#start0()
完成的，这个方法经过解释器的native_entry入口，调用到了JVM_StartThread()函数。其中的static void thread_entry(JavaThread*
thread, TRAPS)函数中会调用JavaCalls::call_virtual()函数。JavaThread最终会通过JavaCalls::call_virtual()
函数来调用字节码中的run()方法；

（5）在SystemDictionary::load_instance_class()
这个能体现双亲委派的函数中，如果类加载器对象不为空，则会调用这个类加载器的loadClass()函数（通过call_virtual()函数来调用）来加载类。

## 线程设置统一异常

`Thread`类的`setDefaultUncaughtExceptionHandler()`方法设置当线程由于未捕获的异常而突然终止时调用的默认处理程序，并且没有为该线程定义其他处理程序。
**入口是由JVM调用**。

调用过程 **dispatchUncaughtException**() -> **uncaughtException**()
，每个main线程new出来的线程在没有指定子线程的threadgroup的时候用的都是main线程，这个时候**Thread.currentThread()
.getThreadGroup().getName()**会是main的名字，而main线程的**parent.getThreadGroup().getName()**是system

```java
private void dispatchUncaughtException(Throwable e){
        getUncaughtExceptionHandler().uncaughtException(this,e);
        }
public void uncaughtException(Thread t,Throwable e){
        if(parent!=null){
        parent.uncaughtException(t,e);
        }else{
        Thread.UncaughtExceptionHandler ueh=
        Thread.getDefaultUncaughtExceptionHandler();
        if(ueh!=null){
        ueh.uncaughtException(t,e);
        }else if(!(e instanceof ThreadDeath)){
        System.err.print("Exception in thread \""
        +t.getName()+"\" ");
        e.printStackTrace(System.err);
        }
        }
        }
```

```java
ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1,20,10L,TimeUnit.SECONDS,new LinkedBlockingQueue(),(runnable)->{
        //1.实现一个自己的线程池工厂
        //创建一个线程
        Thread thread=new Thread(runnable);
        //给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
        thread.setDefaultUncaughtExceptionHandler((Thread t1,Throwable e)->System.out.println("线程工厂设置的exceptionHandler"+e.getMessage()));
        return thread;
        }){
@Override
protected void beforeExecute(Thread t,Runnable r){

        }

@Override
protected void afterExecute(Runnable r,Throwable t){

        }

@Override
protected void terminated(){

        }
        };
```

## Thread和Runnable的关系？

![image-20211119144917262](noteImg/image-20211119144917262.png)

![image-20211119144924285](noteImg/image-20211119144924285.png)

## java8中规范的四大函数式接口（transmit）

```java
   1、Consumer<T>  :消费型接口 void accept(T t);

        2、Supplier<T>   :供给型接口 T get();

        3、Function<T, R>  :函数型接口 R apply(T t);

        4、Predicate<T>  :断言型接口 boolean test(T t);
```

## Executors提供四种创建线程池的弊端

```java
FixedThreadPool(LinkedBlockingQueue)和 SingleThreadPool(LinkedBlockingQueue):
//允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。 

        CachedThreadPool 和 ScheduledThreadPool:
//允许的创建最大线程数为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM。 
```

![image-20211123000009816](noteImg/image-20211123000009816.png)

![image-20211123000535170](noteImg/image-20211123000535170.png)

JAVA1.8时间

**LocalDate**、**LocalTime**、**Instant**、**Duration**、**Period**

![image-20211123202933236](noteImg/image-20211123202933236.png)

## 3O定义

OOA（analyse）：面向对象分析，分析里面有几个对象，以及对象之间的关系

OOD（design）：面向对象设计，设计对象之间发生了什么关系

OOP（programme）：面向对象编程，最后在进行编程

## FutureTask

Future接口代表一个异步执行的任务。FutureTask类代表一个可取消的异步计算任务。

![image-20211123210912275](noteImg/image-20211123210912275.png)

### RejectedExecutionHandler拒绝策略

```java
AbortPolicy
        CallerRunsPolicy
        DiscardOldestPolicy
        DiscardPolicy
```

## 进程

程序由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至
CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备，在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理
IO 的 。操作系统会以进程为单位，分配系统资源(CPU时间片、内存等资源)，进程是 资源分配的最小单位。

## 线程

线程是进程中的实体，一个进程可以拥有多个线程，一个线程必须有一个父进程。 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给
CPU 执行 。 线程，有时被称为轻量级进程(Lightweight Process，LWP)，是操作系统调度(CPU调度)执行的最小单位。

## 进程与线程的区别

进程基本上**相互独立**的，而**线程存在于进程内**，是进程的一个**子集**，进程拥有共享的资源，如内存空间等，供其内部的线程共享
进程间通信较为复杂。**同一台计算机的进程通信称为 IPC(Inter-process communication)**不同计算机之间的进程通信，需要通过网络，并遵守共同的协议，例如
HTTP 线程通信相对简单，因为它们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量线程更轻量，线程上下文切换成本一般上要比进程上下文切换低

## 进程间通信的方式

1. 管道(pipe)及有名管道(named pipe):管道可用于具有亲缘关系的父子进程间的通信，有名管道除了具有管道所具有的功能外，它还允许无亲缘关系进程间的通信。
   **管道是[linux](https://so.csdn.net/so/search?from=pc_blog_highlight&q=linux)
   系统最常见的进程间通信方式之一，它在两个进程之间实现一个数据流通的通道，数据以一种数据流的方式在进程间流动。它把一个程序的输出直接连到另一个程序的输入。
   **

2. 信号(signal):信号是在软件层次上对中断机制的一种模拟，它是比较复杂的通信方式，用于通知进程有某事件发生，一个进程收到一个信号与处理器收到一个中断请求效果
   上可以说是一致的。

3. 消息队列(message queue):消息队列是消息的链接表，它克服了上两种通信方式 中信号量有限的缺点，具有写权限得进程可以按照一定得规则向消息队列中添加新信息;
   对消息队列有读权限得进程则可以从消息队列中读取信息。

4. 共享内存(shared memory):可以说这是最有用的进程间通信方式。它使得多个进程可以访问同一块内存空间，不同进程可以及时看到对方进程中对共享内存中数据得更新。这种方式需要依靠某种同步操作，如互斥锁和信号量等。

5. 信号量(semaphore):主要作为进程之间及同一种进程的不同线程之间得同步和互 斥手段。

6. 套接字(socket):这是一种更为一般得进程间通信机制，它可用于网络中不同机器之 间的进程间通信，应用非常广泛。

## 线程时间片一般是10ms～100ms不等，上下文切换时间大约为5ms～10ms

1. 上下文切换只能在内核模式下发生。
2. 上下文切换是多任务操作系统的一个基本特性

## 内核模式(Kernel Mode)vs 用户模式(User Mode)

![image-20211119171822365](noteImg/image-20211119171822365.png?lastModify=1637759459)

![image-20211119172038780](noteImg/image-20211119172038780.png?lastModify=1637759459)

应用程序一般会在以下几种情况下切换到内核模式：

1． 系统调用。

2． 异常事件。当发生某些预先不可知的异常时，就会切换到内核态，以执行相关的异常事件。

3． 设备中断。在使用外围设备时，如外围设备完成了用户请求，就会向CPU发送一个中断信号，此时，CPU就会暂停执行原本的下一条指令，转去处理中断事件。此时，如果原来在用户态，则自然就会切换到内核态。

**！Linux 创建线程（pthread_create 库）**

## 操作系统层面线程生命周期（五态模型）

初始状态，可运行状态，运行状态，休眠状态，终止状态

![image-20211119174100771](noteImg/image-20211119174100771.png?lastModify=1637759459)

很多人会把操作系统层面的线程状态与java线程状态混淆，所以导致有的文章中把java线程状态写成是5种，在此我们说清楚一个问题，java线程状态是6个，操作系统层面的线程状态是5种。如下图所示：

### **操作系统线程状态**

下面分别介绍一下这5种状态：

1）**new ：**一个新的线程被创建，等待该线程被调用执行；

2）**ready ：**表示线程已经被创建，正在等待系统调度分配CPU使用权，或者时间片已用完，此线程被强制暂停，等待下一个属于它的时间片到来。

3）**running ：**表示线程获得了CPU使用权，正在占用时间片，正在进行运算中；

4）**waiting ：**表示线程等待（或者说挂起），等待某一事件(如IO或另一个线程)执行完，让出CPU资源给其他线程使用；

5）**terminated ：**一个线程完成任务或者其他终止条件发生，该线程终止进入退出状态，退出状态释放该线程所分配的资源。

需要注意的是，操作系统中的线程除去new 和terminated 状态，一个线程真实存在的状态是ready 、running、waiting 。

## java线程定义(六种)

```java
//尚未启动的线程的线程状态
NEW，表示线程被创建出来还没真正启动的状态，可以认为它是个 Java 内部状态。
        //可运行线程的线程状态。 处于可运行状态的线程正在 Java 虚拟机中执行，但它可能正在等待来自操作系统的其他资源，例如处理器
RUNNABLE，表示该线程已经在 JVM 中执行，当然由于执行需要计算资源，它可能是正在运行，也可能还在等待系统分配给它 CPU 片段，在就绪队列里面排队。
//线程阻塞等待监视器锁的线程状态。 处于阻塞状态的线程正在等待监视器锁进入同步块/方法或在调用Object.wait后重新进入同步块/方法。针对Synchronized在其他一些分析中，会额外区分一种状态 RUNNING，但是从 Java API 的角度，并不能表示出来。
BLOCKED，这个状态和我们前面两讲介绍的同步非常相关，阻塞表示线程在等待 Monitor lock。比如，线程试图通过 synchronized 去获取某个锁，但是其他线程已经独占了，那么当前线程就会处于阻塞状态。
//等待线程的线程状态。 由于调用以下方法之一，线程处于等待状态：
//Object.wait没有超时
//Thread.join没有超时
//LockSupport.park
//正在等待另一个对象的唤醒例如Object.notify()或者Object.notifyAll()
WAITING，表示正在等待其他线程采取某些操作。一个常见的场景是类似生产者消费者模式，发现任务条件尚未满足，就让当前消费者线程等待（wait），另外的生产者线程去准备任务数据，然后通过类似 notify 等动作，通知消费线程可以继续工作了。Thread.join()也会令线程进入等待状态。
        //具有指定等待时间的等待线程的线程状态。 由于使用指定的正等待时间调用以下方法之一，线程处于定时等待状态：
//Thread.sleep睡眠
//Object.wait超时
//Thread.join超时
//LockSupport.parkNanos
//LockSupport.parkUntil
TIMED_WAITING，其进入条件和等待状态类似，但是调用的是存在超时条件的方法，比如 wait 或 join 等方法的指定超时版本，如下面示例：
//终止线程的线程状态。 线程已完成执行
TERMINATED，不管是意外退出还是正常执行结束，线程已经完成使命，终止运行，也有人把这个状态叫作死亡。
```

### JVM层面和OS系统层面也会有不同的表示

## Java中实现线程有几种方式?

### 方式1:使用 Thread类或继承Thread类

```
Thread thread = new Thread(){
    @Override
    public void run() {
        System.out.println("线程创建完成");
    }
};
thread.start();
```

### 方式2:实现 Runnable 接口配合Thread

```
Thread thread = new Thread(() -> {
    System.out.println("利用runnbale方式创建线程");
});
//新建线程执行
thread.start();
//main线程执行 调用方法
thread.run();
```

### 方式3:使用有返回值的 Callable

![image-20211119184416387](noteImg/image-20211119184416387.png?lastModify=1637759459)

Future.java

![image-20211119184654846](noteImg/image-20211119184654846.png?lastModify=1637759459)

java thread >> jvm javathread >> os thread

## 协程（coroutines）

是一种基于线程之上，但又比线程更加轻量级的存在，协程不是被操作系统内核所管理，而完全是由程序所控制（也就是在用户态执行），具有对内核来说不可见的特性。

## ThreadLocal

## 四种引用类型

### 强引用

java默认的引用类型，例如 Object a = new Object();其中 a 为强引用，new Object()为一个具体的对象。一个对象从根路径能找到强引用指向它，jvm虚拟机就不会回收。

### 软引用(SoftReference)

进行年轻代的垃圾回收不会触发SoftReference所指向对象的回收；但如果触发Full
GC，那SoftReference所指向的对象将被回收。备注：是除了软引用之外没有其他强引用引用的情况下。

### 弱引用(WeakReference)

如果对象除了有弱引用指向它后没有其他强引用关联它，当进行年轻代垃圾回收时，该引用指向的对象就会被垃圾回收器回收。

### 虚引用(PhantomeReference)

该引用指向的对象，无法对垃圾收集器收集对象时产生任何影响，但在执行垃圾回收后垃圾收集器会通过注册在PhantomeReference上的队列来通知应用程序对象被回收。

## Lock接口

在Lock接口出现之前，java程序是靠synchronized关键字实现锁功能的，而java
SE5之后，并发包中新增了Lock接口（以及相关实现类）用来实现锁功能，它提供了与synvhronized关键字类似的同步功能，只是在使用时需要显示地获取和释放锁。虽然它却少了（通过synchronized块或者方法所提供的）隐式获取
**释放锁的便捷性**，但是却拥有了锁获取与释放的**可操作性**、可中断的获取锁以及超时获取所等多种synchronized关键字所不具备的同步特性。

## 队列同步器

AbstractQueueSynchronizer（同步器）用来构建锁或者其他同步组件的基础框架。它使用了一个int成员变量表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作。同步器的主要方式是继承，子类通过继承同步器并实现它的抽象方法来管理同步状态。这个同步器本身没有实现任何同步接口，它仅仅是定义了若干同步状态获取和释放的方法来供自定义同步组件使用。

重写同步器制定的方法时，需要使用同步器提供的三个方法来访问或者修改同步状态。

getState():

setState(int newState): 设置当前同步状态

compareAndSetState(int expect,int update): 使用CAS设置当前状态，该方法能够保证状态的原子性 。

### JAVA阻塞队列

```
ArrayBolockingQueue：一个由数组结构组成的有界阻塞队列 用的是同一把 ReentrankLock 锁
 
LinkedBlockingQueue：一个由链表结构组成的无界阻塞队列 用的是两把锁takeLock和putLock，一个是用来take的，一个是用来put的

PriorityBlockingQueue：一个支持优先级排序的无界阻塞队列

DelayQueue：一个使用优先级队列实现的无界阻塞队列

SynchronousQueue：一个不存在元素的阻塞队列

LinkedTransferQueue：一个由链表结构组成的无界阻塞队列

LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列
```

**地址：**https://www.processon.com/view/link/618ce3941e0853689b0818e2

**阻塞队列插入和移除操作的4种处理方式**

| 方法/处理方式 | 抛出异常      | 返回特殊值   | 超时退出               | 一直阻塞   |
|---------|-----------|---------|--------------------|--------|
| 插入方法    | add（e）    | offer（） | offer（e，time，unit） | put（e） |
| 移除方法    | remove（）  | poll（）  | poll（time，unit）    | take（） |
| 检查方法    | element（） | peek（）  | 不可用                | 不可用    |

### 原子操作类

#### 原子更新基本类型类

```shell
AtomicBoolean

AtomicInteger

AtomicLong
```

#### 原子更新数组

```shell
AtomicIntegerArray

AtomicLongArray

AtomicReferenceArray

AtomicIntegerArray
```

#### 原子更新引用类型

```shell
AtomicReference

AtomicReferenceFieldUpdater

AtomicMarkableReference
```

#### 原子更新字段类

```shell
AtomicIntegerFieldUpdater

AtomicLongFieldUpdater

AtomicStampedReference
```

## 栈的使用场景

### 栈在函数调用中的应用

### 栈在表达式求值中的应用

实际上，编译器就是通过两个栈来实现的。其中一个保存操作数的栈，另一个是保存运算符的栈。我们从左向右遍历表达式，当遇到数字，我们就直接压入操作数栈；当遇到运算符，就与运算符栈的栈顶元素进行比较。如果比运算符栈顶元素的优先级高，就将当前运算符压入栈；如果比运算符栈顶元素的优先级低或者相同，从运算符栈中取栈顶运算符，从操作数栈的栈顶取
2 个操作数，然后进行计算，再把计算完的结果压入操作数栈，继续比较。

![img](https://static001.geekbang.org/resource/image/bc/00/bc77c8d33375750f1700eb7778551600.jpg)

### 栈在括号匹配中的应用

![img](https://static001.geekbang.org/resource/image/fb/cd/fb8394a588b12ff6695cfd664afb17cd.jpg)

哈希算法 MD5 SHA CRC

MD5 Message-Digest Algorithm，MD5 消息摘要算法

SHA Secure Hash Algorithm，安全散列算法

HASH算法的基本要求

- 从哈希值不能反向推导出原始数据（所以哈希算法也叫单向哈希算法）；

- 对输入数据非常敏感，哪怕原始数据只修改了一个 Bit，最后得到的哈希值也大不相同；

- 散列冲突的概率要很小，对于不同的原始数据，哈希值相同的概率非常小；

- 哈希算法的执行效率要尽量高效，针对较长的文本，也能快速地计算出哈希值。

**鸽巢原理**（也叫抽屉原理）。这个原理本身很简单，它是说，如果有 10 个鸽巢，有 11 只鸽子，那肯定有 1 个鸽巢中的鸽子数量多于 1
个，换句话说就是，肯定有 2 只鸽子在 1 个鸽巢内。

应用一：安全加密

应用二：唯一标识

应用三：数据校验

应用四：散列函数

应用五：负载均衡

应用六：数据分片

应用七：分布式存储

![img](https://static001.geekbang.org/resource/image/ab/16/ab103822e75b5b15c615b68560cb2416.jpg)

![img](https://static001.geekbang.org/resource/image/29/2c/299c615bc2e00dc32225f4d9e3490e2c.jpg)

# HashMap版本问题

## HashMap1.7问题

1. **多线程环境操作下，在resize扩容的过程中，由于采用的是头插法，所以会导致环形链表的产生 **
2. **get方法的不可见性；（即上一秒put完值，下一秒get值，所get到的值不是最新值。）**
3. **put方法值如果index一致，多线程环境下可能会造成数据丢失，导致后一个插入的覆盖前一个的值。**

参考文章：https://segmentfault.com/a/1190000024510131

![image-20220705171706167](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220705171706167.png)

第一轮

```java
 //next= 7   e = 3
 Entry<K, V> next=e.next;
        // i=3
        int i=indexFor(e.hash,newCapacity);
        //e.next = null ，刚初始化时新数组的元素为null
        e.next=newTable[i];
        //给新数组i位置 赋值 3
        newTable[i]=e;
        // e = 7
        e=next;
```

第二轮

```java
 //next= 5   e = 7
 Entry<K, V> next=e.next;
        // i=3
        int i=indexFor(e.hash,newCapacity);
        //e.next = 3 ，此时相同位置上已经有key=3的值了，将该值赋值给当前元素的next
        e.next=newTable[i];
        //给新数组i位置 赋值 7
        newTable[i]=e;
        // e = 5
        e=next;
```

**头插法**

## HashMap1.8ConcurrentModificationException问题

```java
// new EntryIterator() extends HashIterator 构造方法就会设置 expectedModCount = modCount;
Iterator<Map.Entry<String, Integer>>iterator=prices.entrySet().iterator();
```

**参考文章：**https://blog.csdn.net/weixin_30587025/article/details/96339354

## hashmap流程图

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200331123933241.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI3Njg2Nzc5,size_16,color_FFFFFF,t_70)

### 为什么hashmap要h>>>16

```java
static final int hash(Object key){
        int h;
        return(key==null)?0:(h=key.hashCode())^(h>>>16);

```

因为大多数时候map的数组length不是很大 不会超过65536 这样在hashcode有效位数很多的情况下，由于数组length过小，就会导致整体的散列性差

### 为什么使用 ^ 不用&或者｜

因为&和|都会使得结果偏向0或者1 ,并不是均匀的概念,所以用^。

**参考文章：**https://blog.csdn.net/qq_42034205/article/details/90384772

### lock 与 lockInterruptibly比较区别在于：

lock 优先考虑获取锁，待获取锁成功后，才响应中断。
lockInterruptibly 优先考虑响应中断，而不是响应锁的普通获取或重入获取。

![img.png](noteImg/img.png)

## 线程的历史

- 单进程人工切换

  -纸袋机

- 多进程批处理

  -多个任务批量执行

- 多进程并行处理

  -把程序写在不同的内存位置来回切换

- 多线程

  -一个程序内部不同任务的来回切换

- 纤程/协程

  -绿色线程，用户管理的(而不是OS管理的)线程

线程等待与计算时间估算工具 profiler 性能分析工具 jprofiler
线上可以使用 arthas

线程池线程计算方式
![img.png](noteImg/img11.png)

魔数 3
心理学认为人对事物的印象通常不能超过3 这个魔法数， 三屏是人类短期记忆的极限，而80 行在一般显示器上是两屏半的代码量。

# ThreadLocal、InheritableThreadLocal、TransmittableThreadLocal 三者之间区别

参考文章：https://blog.csdn.net/weixin_43954303/article/details/113837928?spm=1001.2014.3001.5501

# Syhchronized

**Syhchronized vs ReentrantLock**

- 等待可中断

- 公平锁

- 锁绑定多个条件

**为什么synchronized与ReentrantLock都可满足需要时优先使用synchronized？**

- synchronized在语法上清晰，简单易用

- Lock对于释放锁还是得由程序员自己保证，synchronized的话则可以由java虚拟机来确保即使出现异常，所也能被自动释放。

- 对于长远来看，synchronized优化的空间的可能性更大，因为锁是记录在对象头里面

## 锁优化

**自旋锁：**为了能让多处理器并行执行多线程，我们就可以让后面请求锁的那个线程"稍等一会"
，但不放弃处理器的执行时间，看看吃有所得线程是否很快就会释放锁。自旋锁在jdk1.4.2就已经引入，默认是关闭的，可以使用-XX:
+Spinning参数来开启，**在JDK6中就已经改为默认开启了**。自旋次数默认是十次，用户可通过使用参数-XX:PreBlockSpin来自行修改。

**自适应自旋锁**：**JDK6对自旋锁的优化。根据前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定的**
，如果再同一个锁对象上，自旋等待刚刚成功获得过锁，并且持有锁的线程正在运行中，那么虚拟机就会认为这次自旋也很有可能再次成功。

**锁消除：**锁消除是指虚拟机即使编译器在运行时检测到某段需要同步的代码根本不可能存在共享数据竞争而实施的一种对锁进行消除的优化策略。

**锁粗化：**如果一系列的连续操作都对同一个对象反复加锁和解锁，设置加锁操作是出现在循环体中，如果虚拟机检探测到有这样一串零碎的操作都对同一个对象加锁，将会把枷锁同步的范围扩展（粗化）到整个操作序列的外部。

**轻量级锁：**在代码即将进入同步块的时候，如果此同步对象没有被锁定（锁标志位为'01'
状态），虚拟机首先将在当前线程的栈帧中建立一个名为锁记录(Lock Record)的空间，用于存储锁对象目前的mark
work的拷贝（官方为这份拷贝加了一个Displaced前缀，即Displaced Mark Work）。

​        **轻量级锁所适应的场景是线程交替执行同步块的场合，如果存在同一时间多个线程访问同一把锁的场合，就会导致轻量级锁膨胀为重量级锁。
**

然后，虚拟机将使用CAS操作尝试把对象的Mark Word更新为指向Lock Record的指针。如果这个更新动作成功了，即代表该线程拥有了这个对象的锁，并且对象Mark
Word的锁标志位（Mark Word的最后两个比特）将转变为“00”，表示此对象处于轻量级锁定状态。

如果这个更新操作失败了，那就意味着至少存在一条线程与当前线程竞争获取该对象的锁。虚拟机首先会检查对象的Mark
Word是否指向当前线程的栈帧，如果是，说明当前线程已经拥有了这个对象的锁，那直接进入同步块继续执行就可以了，否则就说明这个锁对象已经被其他线程抢占了。如果出现两条以上的线程争用同一个锁的情况，那轻量级锁就不再有效，必须要膨胀为重量级锁，锁标志的状态值变为“10”，此时Mark
Word中存储的就是指向重量级锁（互斥量）的指针，后面等待锁的线程也必须进入阻塞状态。

**在执行类的初始化期间，JVM会去获取一个锁，这个锁可以同步多个线程对同一个类的初始化。**

java语言规范规定，对于每一个类或接口C，都有一个唯一的初始化锁LC与之对应。

```java
public static Instance getInstance2(){
        return InstanceHolder.instance;
        }

private static class InstanceHolder {
    public static Instance instance = new Instance();
}
```

### Markword布局

![image-20220711125639043](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220711125639043.png)

**参考文章：**https://www.cnblogs.com/hongdada/p/14087177.html

## 偏向锁

**它的目的是消除数据在无竞争情况下的同步原语，进一步提高程序的运行性能。**

*

*注意⚠️：当一个对象已经计算过一致性哈希码后，它就再也无法进入偏向所状态了：而当一个对象当前正处于偏向所状态，又收到需要计算其一致性哈希码请求时，它的偏向状态会立即撤销，并且锁会膨胀为重量级锁。偏向锁状态执行obj.notify()
会升级为轻量级锁，调用obj.wait(timeout) 会升级为重量级锁。**

**+UseCompressedOops**：开启之后会使用32-bit的offset来代表java object的引用。

**-XX:+UseCompressedClassPointers（默认开启）**：指的是kclass
pointer指针，对象头的另外一部分是klass类型指针，即对象指向它的类元数据的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例。

**偏向锁撤销需要等待到全局安全点（在这个时间点上没有正在执行的字节码）。**

采用了一种等到竞争出现才释放锁的机制。

![image-20220705210448594](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220705210448594.png)

偏向锁在Java 6和Java 7里是默认启用的，但是它在应用程序启动几秒钟之后才激活，如有必要可以使用JVM参数来关闭延迟：-XX:
BiasedLockingStartupDelay=0。如果你确定应用程序里所有的锁通常情况下处于竞争状态，可以通过JVM参数关闭偏向锁：-XX:-
UseBiasedLocking=false，那么程序默认会进入轻量级锁状态。

### **偏向锁批量重偏向&批量撤销**

从偏向锁的加锁解锁过程中可看出，当只有一个线程反复进入同步块时，偏向锁带来的性能开销基本可以忽略，但是当有其他线程尝试获得锁时，就需要等到safe
point时，再将偏向锁撤销为无锁状态或升级为轻量级，会消耗一定的性能，所以在多线程竞争频繁的情况下，偏向锁不仅不能提高性能，还会导致性能下降。于是，就有了批量重偏向与批量撤销的机制。

**原理**

以class为单位，为每个class维护一个偏向锁撤销计数器，每一次该class的对象发生偏向撤销操作时，该计数器+1，当这个值达到重偏向阈值（默认20）时，JVM就认为该class的偏向锁有问题，因此会进行批量重偏向。

每个class对象会有一个对应的epoch字段，每个处于偏向锁状态对象的Mark
Word中也有该字段，其初始值为创建该对象时class中的epoch的值。每次发生重偏向时，就将该值+1，同时遍历JVM中所有线程的栈，找到该class所有正处于加锁状态的偏向锁，将其epoch字段改为新值。下次获得锁时，发现当前对象的epoch值和class的epoch不相等，那就算当前已经偏向了其他线程，也不会执行撤销操作，而是直接通过CAS操作将其Mark
Word的Thread Id 改成当前线程Id。

当达到重偏向阈值（默认20）后，假设该class计数器继续增长，当其达到批量撤销的阈值后（默认40），JVM就认为该class的使用场景存在多线程竞争，会标记该class为不可偏向，之后，对于该class的锁，直接走轻量级锁的逻辑。

**应用场景**

### 批量重偏向

**批量重偏向（bulk rebias）机制是为了解决**
：一个线程创建了大量对象并执行了初始的同步操作，后来另一个线程也来将这些对象作为锁对象进行操作，这样会导致大量的锁撤销升级为轻量级锁。为了避免大量偏向锁升级为轻量级锁

### 批量撤销

批量撤销（bulk revoke）机制是为了解决：在明显多线程竞争剧烈的场景下使用偏向锁是不合适的。

![preview](https://pic4.zhimg.com/v2-b7da59b4bc2a8957ac86a8eed7290da7_r.jpg)

**参考文章：**https://segmentfault.com/a/1190000023665056

## 轻量级锁

**轻量级锁所适应的场景是线程交替执行同步块的场合，如果存在同一时间多个线程访问同一把锁的场合，就会导致轻量级锁膨胀为重量级锁。
**

轻量级锁在执行同步块之前，JVM会现在当前线程的栈桢中创建用于存储所记录的空间，并将对象头中的MarkWord复制到锁记录中，官方称为Displaced
mark word。**然后线程尝试使用CAS将对象头中的Mark Word替换指向锁记录的指针。如果成功，当前线程获得锁，如果失败，表示其他线程争锁，当前线程便尝试使用自旋锁来获取锁。
**

**解锁**

轻量级解锁时，会使用原子的CAS操作将Displaced Mark word替换回到对象头，如果成功，则表示没有竞争发生。如果失败，表示当前锁存在竞争，锁就会膨胀成重量级锁。

![img](https://img-blog.csdnimg.cn/2020122121571259.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0xlb25fSmluaGFpX1N1bg==,size_16,color_FFFFFF,t_70)

## 对象内置锁（ObjectMonitor）

参考文章：https://www.cnblogs.com/hongdada/p/14513036.html

```c++
//结构体如下
ObjectMonitor::ObjectMonitor() {  
  _header       = NULL;  
  _count       = 0;  
  _waiters      = 0,  
  _recursions   = 0;       //线程的重入次数
  _object       = NULL;  
  _owner        = NULL;    //标识拥有该monitor的线程
  _WaitSet      = NULL;    //等待线程组成的双向循环链表，_WaitSet是第一个节点
  _WaitSetLock  = 0 ;  
  _Responsible  = NULL ;  
  _succ         = NULL ;  
  _cxq          = NULL ;    //多线程竞争锁进入时的单向链表
  _FreeNext     = NULL ;  
  _EntryList    = NULL ;    //_owner从该双向循环链表中唤醒线程结点，_EntryList是第一个节点
  _SpinFreq     = 0 ;  
  _SpinClock    = 0 ;  
  OwnerIsThread = 0 ;  
}  
```

# java位移

**<< >>左（右）移位数大于等于32位操作时，会先求余（%）后再进行左（右）移操作（同理，long是取余64）**

```markdown

>>>而无符号右移运算符是补上0，也就是说，对于正数移位来说等同于：>>，负数通过此移位运算符能移位成正数。以-733183670>>>8为例来画一下图
```

## 负数二进制表示形式

1 求原码：为 10000101 即把-5的绝对值5转换为二进制为
2 求反码：为 11111010（符号位不参加运算）
3 求补码：为 11111011

# CyclicBarrier和CountdownLatch的区别？

![img](https://ask.qcloudimg.com/http-save/7256485/3uwx6izita.png?imageView2/2/w/1620)

# 线程通信

1、使用volatile关键字

2、使用Object类的wait() / notify() （需要结合Synchronized使用）

3、使用JUC工具类CountDownLatch

4、使用ReetrantLock结合Condition

5、基本LockSupport实现线程间的阻塞和唤醒

其中2、4在线程A唤醒B的时候，线程B不是立马获取到锁，只是通知可以参与锁竞争了

# Thread.sleep、Object.wait、LockSupport.park 区别

### Thread.sleep() 方法

**通过sleep方法进入休眠的线程不会释放持有的锁，因此，在持有锁的时候调用该方法需要谨慎。**

### LockSupport.park() 方法

不会释放锁，会响应中断

### Object.wait() 方法

如果不和synchronized一起使用有可能出现方法乱序执行的情况，先执行了notify()之后在执行了wait()，后执行的wait()
由于没有额外的notify()操作，所以该线程一直阻塞下去。 该线程会释放该对象的锁。
synchronized可以保证一个代码块的原子性，中间没有其他线程的插入，从头到尾执行。

使用synchronized的情况下就保证了当前操作的是同一个对象。**由于上的锁未执行到wait之前所以是不会释放锁的。**

```java
// 线程 A 的代码
synchronized(obj_A)
        {
        while(!condition){
        obj_A.wait();
        }
        // do something 
        }
// 线程 B 的代码
synchronized(obj_A)
        {
        if(!condition){
        // do something ...
        condition=true;
        obj_A.notify();
        }
        }
```

**为什么需要搭配synchronized使用：**https://blog.csdn.net/lengxiao1993/article/details/52296220

## 内置锁(ObjectMonitor)

**参考文章：**https://www.cnblogs.com/hongdada/p/14513036.html

Monitor可以理解为一个同步工具或一种同步机制，通常被描述为一个对象。**每一个Java对象就有一把看不见的锁，称为内部锁或者Monitor锁。
**

通常所说的对象的内置锁，**是对象头Mark Word中的重量级锁指针指向的monitor对象，该对象是在HotSpot底层C++语言编写的(
openjdk里面看)，简单看一下代码：**

```.cpp
//结构体如下
ObjectMonitor::ObjectMonitor() {  
  _header       = NULL;  
  _count       = 0;  
  _waiters      = 0,  
  _recursions   = 0;       //线程的重入次数
  _object       = NULL;  
_owner        = NULL;    //标识拥有该monitor的线程
  _WaitSet      = NULL;    //等待线程组成的双向循环链表，_WaitSet是第一个节点
  _WaitSetLock  = 0 ;  
  _Responsible  = NULL ;  
  _succ         = NULL ;  
  _cxq          = NULL ;    //多线程竞争锁进入时的单向链表
  _FreeNext     = NULL ;  
  _EntryList    = NULL ;    //_owner从该双向循环链表中唤醒线程结点，_EntryList是第一个节点
  _SpinFreq     = 0 ;  
  _SpinClock    = 0 ;  
  OwnerIsThread = 0 ;  
}  
```

**特别重要的两个属性：**

监控区（Entry Set）：锁已被其他线程获取，期待获取锁的线程就进入Monitor对象的监控区

待授权区（Wait Set）：曾经获取到锁，但是调用了wait方法，线程进入待授权区

# ReentrantLock为什么非公平比公平

在激烈竞争的情况下，非公平锁的性能高于公平锁的性能的一个原因是：**
在恢复一个被挂起的线程与该线程真正开始运行之间存在着严重的延迟。**
假设线程A持有一个锁，并且线程B请求这个锁。由于这个锁已被线程A持有，因此B将被挂起。当A释放锁时，B将被唤醒，因此会再次尝试获取锁。与此同时，如果C也请求这个锁，那么C很可能会在B被完全唤醒之前获得、使用以及释放这个锁。这样的情况是一个"
双赢"的局面：B获得锁的时刻并没有推迟，C更早地获得了锁，并且吞吐量也获得了提高。

# ReentrantReadWriteLock

- `firstReader` 是获取读锁的第一个线程。如果只有一个线程获取读锁，很明显，使用这样一个变量速度更快。
- `firstReaderHoldCount`是 `firstReader`的计数器。同上。
- `cachedHoldCounter`是最后一个获取到读锁的线程计数器，每当有新的线程获取到读锁，这个变量都会更新。这个变量的目的是：*
  *当最后一个获取读锁的线程重复获取读锁，或者释放读锁，就会直接使用这个变量，速度更快，相当于缓存。**

# Thread的Run运行过程

JavaThread的启动最终都要通过一个native方法java.lang.Thread#start0()
完成的，这个方法经过解释器的native_entry入口，调用到了JVM_StartThread()函数。其中的static void thread_entry(JavaThread*
thread, TRAPS)函数中会调用JavaCalls::call_virtual()函数。JavaThread最终会通过JavaCalls::call_virtual()
函数来调用字节码中的run()方法。

# 线程的两种状态

线程有两种状态，在任何一个时间点上，线程是可结合的（joinable），或者是分离的（detached）。 一个可结合的线程能够被其他线程收回其资源和杀死；
相反，一个分离的线程是不能被其他线程回收或杀死的，它的存储器资源在它终止时由系统自动释放。

# Java线程池KeepAliveTime原理

其实只是在线程从工作队列 poll 任务时，加上了超时限制，如果线程在 keepAliveTime 的时间内 poll
不到任务，那我就认为这条线程没事做，可以干掉了 。

# AbstractQueuedSynchronizer

锁是用来控制多个线程访问共享资源的方式，一般来说，一个锁能够防止多个线程同时访问共享资源（但是有些锁可以允许多个线程并发的访问共享资源，比如读写锁）。在Lock接口出现之前，java程序是靠synchronized关键字实现锁功能的，而
**java SE 5**
之后，并发包中新增了Lock接口（以及相关实现类）用来实现锁功能，它提供了与synchronized关键字类似的同步功能，只是在使用时需要显示地获取和释放锁。虽然它缺少了（通过synchronized块或者方法所提供的）隐式获取释放锁的便携性，但是却拥有了锁获取与释放的可操作性、可中断的获取锁以及超时获取锁等多种synchronized关键字所不具备的同步特性。**AbstractQueuedSynchronizer（以下简称同步器），是用来构建锁或者其他同步组建的基础框架，他是用了一个int成员变成表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作。临界资源是一次仅允许一个进程使用的共享资源。每个进程中访问临界资源的那段代码称为临界区。**

## ReentrankLock

![img](https://p0.meituan.net/travelcube/412d294ff5535bbcddc0d979b2a339e6102264.png)

根据上面的讨论，ReentrantLock在功能上是synchronized的超集，在性能上又至少不会弱于synchronized，那synchronized修饰符是否应该被直接抛弃，不再使用了呢？当然不是，基于以下理由，**笔者仍然推荐在synchronized与ReentrantLock都可满足需要时优先使用synchronized**： 

- **synchronized是在Java语法层面的同步，足够清晰，也足够简单。**每个Java程序员都熟悉synchronized，但J.U.C中的Lock接口则并非如此。因此在只需要基础的同步功能时，更推荐synchronized。 
- **Lock应该确保在finally块中释放锁，否则一旦受同步保护的代码块中抛出异常，则有可能永远不会释放持有的锁。**这一点必须由程序员自己来保证，而使用synchronized的话则可以由Java虚拟机来确保即使出现异常，锁也能被自动释放。 
- 尽管在JDK 5时代ReentrantLock曾经在性能上领先过synchronized，但这已经是十多年之前的胜利了。从长远来看，**Java虚拟机更容易针对synchronized来进行优化，因为Java虚拟机可以在线程和对象的元数据中记录synchronized中锁的相关信息，**而使用J.U.C中的Lock的话，Java虚拟机是很难得知具体哪些锁对象是由特定线程锁持有的。

## 公平非公平锁区别jdk1.8

### FairSync

```java
        final void lock(){
        acquire(1);
        }
protected final boolean tryAcquire(int acquires){
final Thread current=Thread.currentThread();
        int c=getState();
        if(c==0){
        if(!hasQueuedPredecessors()&&
        compareAndSetState(0,acquires)){
        setExclusiveOwnerThread(current);
        return true;
        }
        }
        else if(current==getExclusiveOwnerThread()){
        int nextc=c+acquires;
        if(nextc< 0)
        throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
        }
        return false;
        }
```

### NonfairSync

```java
        final void lock(){
        if(compareAndSetState(0,1))
        setExclusiveOwnerThread(Thread.currentThread());
        else
        acquire(1);
        }
final boolean nonfairTryAcquire(int acquires){
final Thread current=Thread.currentThread();
        int c=getState();
        if(c==0){
        if(compareAndSetState(0,acquires)){
        setExclusiveOwnerThread(current);
        return true;
        }
        }
        else if(current==getExclusiveOwnerThread()){
        int nextc=c+acquires;
        if(nextc< 0) // overflow
        throw new Error("Maximum lock count exceeded");
        setState(nextc);
        return true;
        }
        return false;
        }
```

```java
public final void acquire(int arg){
        if(!tryAcquire(arg)&&
        acquireQueued(addWaiter(Node.EXCLUSIVE),arg))
        selfInterrupt();
        }
```

**核心方法**

```java
static final class Node {
    /** Marker to indicate a node is waiting in shared mode */
    static final Node SHARED = new Node();
    /** 用于指示节点正在独占模式下等待的标记 */
    static final Node EXCLUSIVE = null;
    /** waitStatus值，表示线程已取消 该节点由于超时或中断而被取消。节点永远不会离开这个状态。特别是，具有取消节点的线程永远不会再次阻塞。*/
    static final int CANCELLED = 1;
    /** waitStatus值，指示后续线程需要取消标记 */
    static final int SIGNAL = -1;
    /** waitStatus值，指示线程正在等待条件，此节点当前位于条件队列中。*/
    static final int CONDITION = -2;
    /** waitStatus值，指示下一个acquireShared应无条件传播*/
    static final int PROPAGATE = -3;

    /**
     * Status field, taking on only the values:
     *   SIGNAL:     The successor of this node is (or will soon be)
     *               blocked (via park), so the current node must
     *               unpark its successor when it releases or
     *               cancels. To avoid races, acquire methods must
     *               first indicate they need a signal,
     *               then retry the atomic acquire, and then,
     *               on failure, block.
     *   CANCELLED:  This node is cancelled due to timeout or interrupt.
     *               Nodes never leave this state. In particular,
     *               a thread with cancelled node never again blocks.
     *   CONDITION:  This node is currently on a condition queue.
     *               It will not be used as a sync queue node
     *               until transferred, at which time the status
     *               will be set to 0. (Use of this value here has
     *               nothing to do with the other uses of the
     *               field, but simplifies mechanics.)
     *   PROPAGATE:  A releaseShared should be propagated to other
     *               nodes. This is set (for head node only) in
     *               doReleaseShared to ensure propagation
     *               continues, even if other operations have
     *               since intervened.
     *   0:          None of the above
     *
     * The values are arranged numerically to simplify use.
     * Non-negative values mean that a node doesn't need to
     * signal. So, most code doesn't need to check for particular
     * values, just for sign.
     *
     * The field is initialized to 0 for normal sync nodes, and
     * CONDITION for condition nodes.  It is modified using CAS
     * (or when possible, unconditional volatile writes).
     */
    volatile int waitStatus;
}

    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
```

# 面试题

## 为什么局部方法内访问变量要用final修饰自己猜测？

个人理解的好处有：**（不变性），纯编译时信息，到Class文件里就已经没有踪迹了，修饰为final是为了解决正确性、合理性、严谨性。用来提醒自己以及其他人，这里的参数变量是真的不能被修改，并让Java编译器去检查到底有没有被乱改。**

**归根究底是由于内存模型与OOP之间的原则不一致**。几乎j.u.c包中的每个方法都采用了这样一种策略：当一个值会被多次使用时，就将这个字段读出来赋值给局部变量。虽然这种做法不雅观，但检查起来会更直观。final字段也会做这样处理，可能有些令人不解。这是因为JVM并不足够智能，不能充分利用JMM已经提供了安全保证的可优化点，比如可以不用重新加载final值到缓存。相比过去，JVM在这方面有很大进步，但仍不够智能。

1. 访问局部变量要比访问成员变量要快。
2. 使用final修饰可以避免变量被重新赋值（引用赋值）

**参考文章：**https://developer.aliyun.com/article/976686

# final关键字

#### 写final域重排序规则

写final域的重排序规则禁止对**final域的写重排序到构造函数之外**，这个规则的实现主要包含了两个方面：

- JMM禁止编译器把final域的写重排序到构造函数之外；
- 编译器会在final域写之后，构造函数return之前，插入一个storestore屏障。这个屏障可以禁止处理器把final域的写重排序到构造函数之外。

因此，写final域的重排序规则可以确保：在对象引用为任意线程可见之前，对象的final域已经被正确初始化过了，而普通域就不具有这个保障。比如在上例，线程B有可能就是一个未正确初始化的对象finalDemo。

#### 读final域重排序规则

读final域重排序规则为：在一个线程中，**初次读对象引用和初次读该对象包含的final域，JMM会禁止这两个操作的重排序**。(
注意，这个规则仅仅是针对处理器)
，处理器会在读final域操作的前面插入一个LoadLoad屏障。实际上，读对象的引用和读该对象的final域存在间接依赖性，一般处理器不会重排序这两个操作。但是有一些处理器会重排序，因此，这条禁止重排序规则就是针对这些处理器而设定的。

读final域的重排序规则可以确保：**在读一个对象的final域之前，一定会先读这个包含这个final域的对象的引用。**

#### 对final修饰的对象的成员域写操作

针对引用数据类型，final域写针对编译器和处理器重排序增加了这样的约束：在构造函数内对一个final修饰的对象的成员域的写入，与随后在构造函数之外把这个被构造的对象的引用赋给一个引用变量，这两个操作是不能被重排序的。注意这里的是“增加”也就说前面对final基本数据类型的重排序规则在这里还是使用。

### 关于final重排序的总结

按照final修饰的数据类型分类：

- 基本数据类型:
  - `final域写`：禁止final域写与构造方法重排序，即禁止final域写重排序到构造方法之外，从而保证该对象对所有线程可见时，该对象的final域全部已经初始化过。
  - `final域读`：禁止初次读对象的引用与读该对象包含的final域的重排序。
- 引用数据类型：
  - `额外增加约束`：禁止在构造函数对一个final修饰的对象的成员域的写入与随后将这个被构造的对象的引用赋值给引用变量 重排序

### final的实现原理

上面我们提到过，写final域会要求编译器在final域写之后，构造函数返回前插入一个StoreStore屏障。读final域的重排序规则会要求编译器在读final域的操作前插入一个LoadLoad屏障。

很有意思的是，如果以X86处理为例，X86不会对写-写重排序，所以StoreStore屏障可以省略。由于不会对有间接依赖性的操作重排序，所以在X86处理器中，读final域需要的LoadLoad屏障也会被省略掉。也就是说，以X86为例的话，对final域的读/写的内存屏障都会被省略！具体是否插入还是得看是什么处理器

# CLH队列锁

参考文章：https://www.baiyp.ren/CLH%E9%98%9F%E5%88%97%E9%94%81.html

CLH锁是有由Craig, Landin, and Hagersten这三个人发明的锁，取了三个人名字的首字母，所以叫 CLH
Lock。CLH锁是一个自旋锁。能确保无饥饿性。提供先来先服务的公平性。**CLH队列锁也是一种基于链表的可扩展、高性能、公平的自旋锁，申请线程仅仅在本地变量上自旋，它不断轮询前驱的状态，假设发现前驱释放了锁就结束自旋。**

# CPU访问内存方式

#### SMP(Symmetric Multi-Processor)

![img](https://www.baiyp.ren/images/thread/clh/clh07.png)

对称多处理器结构，指服务器中多个CPU对称工作，每个CPU访问内存地址所需时间相同。其主要特征是共享，包含对CPU，内存，I/O等进行共享。

SMP能够保证内存一致性，但这些共享的资源很可能成为性能瓶颈，随着CPU数量的增加，每个CPU都要访问相同的内存资源，**可能导致内存访问冲突，
**

可能会导致CPU资源的浪费。常用的PC机就属于这种。

#### NUMA(Non-Uniform Memory Access)

![img](https://www.baiyp.ren/images/thread/clh/clh08.png)

非一致存储访问，将CPU分为CPU模块，每个CPU模块由多个CPU组成，并且具有独立的本地内存、I/O槽口等，模块之间可以通过**互联模块**
相互访问，

访问本地内存的速度将远远高于访问远地内存(系统内其它节点的内存)的速度，这也是非一致存储访问的由来。NUMA较好地解决SMP的扩展问题，

当CPU数量增加时，因为访问远地内存的延时远远超过本地内存，系统性能无法线性增加。

# java并发编程常用工具类

**CountDownLatch、CyclicBarrier、Semaphore、Exchanger、Phaser**

**CountDownLatch**，**其场景主要应用在一个任务等待其他任务执行 N 次后才执行**。假设我们定义了两个线程类，这两个线程类中当执行完本线程的任务将计数减
1，再定义一个等待线程，当计数为 0 时执行本线程的任务。

**CyclicBarrier**，其场景主要为有一组线程，当所有线程执行到某一个时刻，这组线程才能继续向下执行。CyclicBarrier 构造时可以传入一个
Runnable，即当一组线程执行到某一时刻点时这个 Runnable 随这一组线程一起执行。

**Semaphore**，**其场景主要可用于对某一资源有使用数量的限制**
。我们假设有一个复杂的计算任务会被很多线程调用，而该计算方法是很资源的，我们希望同时能执行的计算在一个数值内，当多个线程调用时，超过则要排队，而计算完毕后排队中的任务可以接着执行。

**Exchanger**，**其应用场景主要是两个线程进行数据交换。**这里我们假设一个生产者消费者的情形，消费者如果手里没商品呢就把自己的容器给生产者，生产者呢生产好商品把填满的容器再给回消费者。

**Phaser是一个阶段协调器，它适用于一种任务可以分为多个阶段，现希望多个线程去处理批任务，对于每个阶段，多个线程可以并发执行，但是希望保证只有前面一个阶段的任务完成之后才能开始后面的任务。**主要方法：register()、arriveAndDeregister()、arriveAndAwaitAdvance()、onAdvance()等。

**CountDownLatch 与 CyclicBarrier 的区别**

CountDownLatch 主要由外部线程来控制线程是否往下执行，而 CyclicBarrier是由一组线程自身来控制。比如，我们有一个计算任务，必须等到前置的若干个计算完成后才能启动，这时候就可以用 CountDownLatch来实现。又比如，我们要测试一个服务的瞬间响应能力，希望启动一批线程，当线程全部准备好后，同时执行调用该服务，就可以用CyclicBarrier 来实现。

**参考文章：**https://xie.infoq.cn/article/96d4f3c1f8308f3d2adaad798

# 循环删除List元素

本文总结了 8 种循环删除 List 元素的方法：

- 普通 for 循环删除（不可靠）
- 普通 for 循环提取变量删除（抛异常）
- **普通 for 循环倒序删除（可靠）**
- 增强 for 循环删除（抛异常）
- **迭代器循环迭代器删除（可靠）**
- 迭代器循环集合删除（抛异常）
- 集合 forEach 方法循环删除（抛异常）
- **stream filter 过滤（可靠）**

# 枚举

**参考文章：**https://zhuanlan.zhihu.com/p/353868533

**使用方式**

1. 编码枚举 **字段值用枚举**
2. 属性枚举 **根据不同业务做枚举映射**
3. 业务枚举 **根据不同key返回不同的value**
4. 枚举工厂 **根据key返回不同的bean进行调用**
5. 枚举回调 **枚举value存储断言工厂 返回boolean类似，value存储业务处理方法**

**底层实现**

枚举类通过静态代码块初始化，类首先继承Enum，静态代码块生成对应数量的静态常量字段的值，还生成了还生成了$VALUES字段，用于保存枚举类定义的枚举常量。所以表面上，只是加了一个enum关键字定义枚举，但是底层一旦确认是枚举类，则会由编译器对枚举类进行特殊处理，通过静态代码块初始化枚举，只要是枚举就一定会提供values()
方法。

```java
Season extends java.lang.Enum<Season>
Season SPRING=new Season1();
        Season SUMMER=new Season2();
        Season AUTUMN=new Season3();
        Season WINTER=new Season4();
        Season[]$VALUES=new Season[4];
        $VALUES[0]=SPRING;
        $VALUES[1]=SUMMER;
        $VALUES[2]=AUTUMN;
        $VALUES[3]=WINTER;
```

第三个，关于values()
方法，这是一个静态方法，作用是返回该枚举类的数组，底层实现原理，其实是这样的。其实是将静态代码块初始化的$VALUES数组克隆一份，然后强转成Season[]
返回。

```java
public static Season[]values(){
        return(Season[])$VALUES.clone();
        }
```

# JDK8特性流

**reduce**：对流中的数据按照你指定的计算方式计算出一个结果

```java
T result=identity；
        for(T element:this stream){
        result=accumulate.apply(result,element);
        }
        return result;
```

# ServiceLoader

```java
ClassLoader.getSystemResources(fullName);
        loader.getResources(fullName);
```

# InputStream结构

![image-20230321022425838](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230321022425838.png)

线程负载的CPU使用率 = 线程增量CPU的运行时间/采样线程间隔时间 * 100%

# CompletableFuture

参考文章：https://www.toutiao.com/article/7255939768826184229/?log_from=4a83d0f87fa51_1692508870262

# 散列冲突解决方案

### 1. 开放寻址法

开放寻址法的核心思想是，如果出现了散列冲突，我们就重新探测一个空闲的位置。

开放寻址法解决方案有线性探测法、二次探测、双重散列等方案：

**线性探测法（Linear Probing）：**往后依次寻找找到第一个不为空的。

**二次探测（Quadratic probing）：**线性探测每次探测的步长为1，即在数组中一个一个探测，而二次探测的步长变为原来的平方。

**双重散列（Double hashing）：**使用一组散列函数，直到找到空闲位置为止。

### 2. 链表法

散列表中，每个“桶（bucket）”都会对应一个条链表，在查找时先听过hash(key)找到位置，然后遍历链表找到对应元素

# 泛型擦除

javac Java的泛型擦除是指在编译期间，Java编译器会将泛型类型信息擦除掉，生成的字节码中不再包含具体的泛型类型参数信息。这种擦除机制是为了保持Java的向后兼容性，因为Java泛型是在JDK5之后引入的。
泛型擦除的实现原理是通过类型擦除来实现的。在编译过程中，编译器会将泛型类型参数替换为它们的上限边界（或者Object类型，如果没有明确的上限边界）。例如，对于`List<Integer>`，编译器会将其擦除为`List<Object>`。对于泛型方法，编译器也会进行类似的处理。在擦除c之后，编译器会插入必要的类型转换操作来保证类型安全。这些转换操作会在字节码层面进行，以确保程序在运行时正确处理泛型类型。
虽然在运行时泛型类型信息被擦除，但反射机制可以通过特殊的方法来获取泛型类型信息。此外，Java编译器还会在编译期间进行类型检查来确保类型的一致性，以避免出现类型错误。需要注意的是，泛型擦除可能会导致一些类型转换的问题和运行时异常，因此在使用泛型时需要特别注意类型安全性和正确性。

# java并发

竞争条件指多个线程或者进程在读写一个[共享数据](https://baike.baidu.com/item/共享数据/19845680?fromModule=lemma_inlink)时结果依赖于它们执行的相对[时间](https://baike.baidu.com/item/时间/25651?fromModule=lemma_inlink)的情形。

竞争条件发生在当多个进程或者线程在读写数据时，其最终的的结果依赖于多个进程的指令执行顺序。

例如：考虑下面的例子

假设两个进程P1和P2共享了[变量](https://baike.baidu.com/item/变量?fromModule=lemma_inlink)
a。在某一执行时刻，P1更新a为1，在另一时刻，P2更新a为2。

因此两个任务竞争地写变量a。在这个例子中，竞争的“[失败者](https://baike.baidu.com/item/失败者?fromModule=lemma_inlink)”(最后更新的进程）决定了变量a的最终值。

**多个进程并发访问和操作同一数据且执行结果与访问的特定顺序有关，称为竞争条件。**

# ThreadPoolExecutor可以被子类重写的方法

```java
finalize()
beforeExecute()
afterExecute()
terminated()
```

# Java如何选择合适的阻塞队列？

1. **功能层面**：是否需要延迟，优先级排队等，DelayQueue和PriorityBlockingQueue
2. **容量：**看是否有存储的需求，ArrayBlockingQueue是指定的，LinkedBlockingQueue默认Integer最大值，DealyQueue容量就是固定的Integer.MAX_VALUE。
3. **能否扩容：**如果需要动态扩容的话就不能选择ArrayBlockingQueue，因为它的容量在创建的时候就确定了，无法扩容。相反，PriorityBlockingQueue即使在指定了初始化容量之后，后续如果有需要，也可以自动扩容。
4. **内存结构：**ArrayBlockingQueue采用的是数组的方式，而LinkedBlockingQueue内部是采用链表。ArrayBlockingQueue没有链表所需要的节点对象，所以空间利用率高，连续 碎片 省空间。
5. **性能：**LinkedBlockingQueue内部使用了两把锁，它的锁力度细，在并发高的时候相对于只有一把锁的ArrayBlockingQueue性能会更好，另外，SynchronousQueue性能会更好，因为它不需要消息存储，只是一个传递的作用，如果没有储存消息的需求，完全可以用它。

# HashMap和HashTable的区别？

1、HashTable是线程安全的，每个方法都用synchronized来修饰 全局锁，hashmap是线程不安全的。

2、性能方法，hashmap更好一些 没有锁，hashtable安全但是锁力度很大。

3、内部实现：

1. HashMap 数组+链表+红黑树（数组单个节点>8个 并且 >=64个的时候采用红黑树），HashTable采用的是数组+链表
2. HashMap初始化容量是16 Hashtable默认初始化容量11
3. HashMap可以存储nullkey 用0来表示，hashtable不可以

4、通过key计算索引下表的方式不同，hashmap采用二次散列的方式（hashcode ^ (hashcode >>> 16)），而hashtable直接是用hashcode对数组长度取模。从而导致key的分布不均匀导致的查询性能。



“零和”是[博弈论](https://baike.baidu.com/item/博弈论/81545?fromModule=lemma_inlink)的一个概念，意思是双方博弈，一方得利必然意味着另一方吃亏，一方得益多少，另一方就吃亏多少，双方得失相抵，总数为零，所以称为“零和”。

# Collection实现类

![image-20230907144214144](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20230907144214144.png)



# 保证线程安全的方式

1、互斥同步 

2、非阻塞同步 

3、无同步方案 方法内部使用 未逃逸 线程独享

# 奇数位移是向上还是向下取整

**11>> 1是5 对于常数来说是向下取整，但是对于数组来说是向上取整，因为数组下标是从0开始的。**

# Java 集合 Stream

Java 中可以使用 java.util.Stream 对一个集合（实现了java.util.Collection接口的类）做各种操作，例如：求和、过滤、排序等等。

这些操作可能是中间操作——返回一个 Stream 流，或者是终端操作——返回一个结果。

流操作并不会影响原来的集合，可以简单认为，流操作是把集合中的一个元素逐个复制放到一个首尾相接的流动的水槽中。



# ForkJoinPool

### 核心思想: work-stealing(工作窃取)算法

work-stealing(工作窃取)算法: 线程池内的所有工作线程都尝试找到并执行已经提交的任务，或者是被其他活动任务创建的子任务(如果不存在就阻塞等待)。这种特性使得 ForkJoinPool 在运行多个可以产生子任务的任务，或者是提交的许多小任务时效率更高。尤其是构建异步模型的 ForkJoinPool 时，对不需要合并(join)的事件类型任务也非常适用。

在 ForkJoinPool 中，线程池中每个工作线程(ForkJoinWorkerThread)都对应一个任务队列(WorkQueue)，工作线程优先处理来自自身队列的任务(LIFO或FIFO顺序，参数 mode 决定)，然后以FIFO的顺序随机窃取其他队列中的任务。

具体思路如下:

- 每个线程都有自己的一个WorkQueue，该工作队列是一个双端队列。
- 队列支持三个功能push、pop、poll
- push/pop只能被队列的所有者线程调用，而poll可以被其他线程调用。
- 划分的子任务调用fork时，都会被push到自己的队列中。
- 默认情况下，工作线程从自己的双端队列获出任务并执行。
- 当自己的队列为空时，线程随机从另一个线程的队列末尾调用poll方法窃取任务。

![img](https://pdai.tech/images/thread/java-thread-x-forkjoin-3.png)

![img](https://pdai.tech/images/thread/java-thread-x-forkjoin-5.png)