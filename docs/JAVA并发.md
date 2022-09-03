

## ThreadPoolExecutor类继承关系

![image-20211119112015735](noteImg/image-20211119112015735.png)



## ScheduledThreadPoolExecutor类继承关系



![image-20211119112153576](noteImg/image-20211119112153576.png)

## 

## interface Executor类

```java
void execute(Runnable command);
```



## abstract class AbstractExecutorService类

```java
public <T> Future<T> submit(Callable<T> task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<T> ftask = newTaskFor(task);
    execute(ftask);
    return ftask;
}
```



## Worker对象结构

```java
/** Thread this worker is running in.  Null if factory fails. */
final Thread thread;
/** Initial task to run.  Possibly null. */
Runnable firstTask;
/** Per-thread task counter */
volatile long completedTasks;
```



## Thread的run和start的区别？

### run方法运行使用的是方法调用，线程是main线程

![image-20211119143751822](noteImg/image-20211119143751822.png)



### start方法运行使用的是线程调用，线程是新建的线程

![image-20211119143915735](noteImg/image-20211119143915735.png)

![image-20211119143928425](noteImg/image-20211119143928425.png)



## Thread和Runnable的关系？

![image-20211119144917262](noteImg/image-20211119144917262.png)

![image-20211119144924285](noteImg/image-20211119144924285.png)



##    java8中规范的四大函数式接口（transmit）

```java
   1、Consumer<T>  :消费型接口  void accept(T t);

   2、Supplier<T>   :供给型接口  T get();

   3、Function<T,R>  :函数型接口  R apply(T t);

   4、Predicate<T>  :断言型接口  boolean test(T t);
```



## Executors提供四种创建线程池的弊端

```java
FixedThreadPool(LinkedBlockingQueue) 和 SingleThreadPool(LinkedBlockingQueue):
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

Future接口代表一个异步执行的任务。

FutureTask类代表一个可取消的异步计算任务。

![image-20211123210912275](noteImg/image-20211123210912275.png)



### RejectedExecutionHandler拒绝策略

```java
AbortPolicy
CallerRunsPolicy
DiscardOldestPolicy
DiscardPolicy
```



 

## 线程时间片一般是10ms～100ms不等，上下文切换时间大约为5ms～10ms

## 进程

程序由指令和数据组成，但这些指令要运行，数据要读写，就必须将指令加载至 CPU，数据加载至内存。在指令运行过程中还需要用到磁盘、网络等设备，在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令、管理内存、管理 IO 的 。操作系统会以进程为单位，分配系统资源(CPU时间片、内存等资源)，进程是 资源分配的最小单位。



## 线程

线程是进程中的实体，一个进程可以拥有多个线程，一个线程必须有一个父进程。 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给 CPU 执行 。 线程，有时被称为轻量级进程(Lightweight Process，LWP)，是操作系统调度(CPU调度)执行的最小单位。



## 进程与线程的区别

进程基本上**相互独立**的，而**线程存在于进程内**，是进程的一个**子集**，进程拥有共享的资源，如内存空间等，供其内部的线程共享 进程间通信较为复杂。**同一台计算机的进程通信称为 IPC(Inter-process communication)**不同计算机之间的进程通信，需要通过网络，并遵守共同的协议，例如 HTTP 线程通信相对简单，因为它们共享进程内的内存，一个例子是多个线程可以访问同一个共享变量线程更轻量，线程上下文切换成本一般上要比进程上下文切换低



## 进程间通信的方式

1. 管道(pipe)及有名管道(named pipe):管道可用于具有亲缘关系的父子进程间的通信，有名管道除了具有管道所具有的功能外，它还允许无亲缘关系进程间的通信。**管道是[linux](https://so.csdn.net/so/search?from=pc_blog_highlight&q=linux)系统最常见的进程间通信方式之一，它在两个进程之间实现一个数据流通的通道，数据以一种数据流的方式在进程间流动。它把一个程序的输出直接连到另一个程序的输入。**

2. 信号(signal):信号是在软件层次上对中断机制的一种模拟，它是比较复杂的通信方式，用于通知进程有某事件发生，一个进程收到一个信号与处理器收到一个中断请求效果 上可以说是一致的。

3. 消息队列(message queue):消息队列是消息的链接表，它克服了上两种通信方式 中信号量有限的缺点，具有写权限得进程可以按照一定得规则向消息队列中添加新信息; 对消息队列有读权限得进程则可以从消息队列中读取信息。

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

## java线程定义(六种)

```
//尚未启动的线程的线程状态
NEW
//可运行线程的线程状态。 处于可运行状态的线程正在 Java 虚拟机中执行，但它可能正在等待来自操作系统的其他资源，例如处理器
RUNNABLE
//线程阻塞等待监视器锁的线程状态。 处于阻塞状态的线程正在等待监视器锁进入同步块/方法或在调用Object.wait后重新进入同步块/方法。针对Synchronized
BLOCKED
//等待线程的线程状态。 由于调用以下方法之一，线程处于等待状态：
//Object.wait没有超时
//Thread.join没有超时
//LockSupport.park
//正在等待另一个对象的唤醒例如Object.notify()或者Object.notifyAll()
WAITING
//具有指定等待时间的等待线程的线程状态。 由于使用指定的正等待时间调用以下方法之一，线程处于定时等待状态：
//Thread.sleep
//Object.wait超时
//Thread.join超时
//LockSupport.parkNanos
//LockSupport.parkUntil
TIMED_WAITING
//终止线程的线程状态。 线程已完成执行
TERMINATED
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

强引用：java默认的引用类型，例如 Object a = new Object();其中 a 为强引用，new Object()为一个具体的对象。一个对象从根路径能找到强引用指向它，jvm虚拟机就不会回收。

软引用(SoftReference)：进行年轻代的垃圾回收不会触发SoftReference所指向对象的回收；但如果触发Full GC，那SoftReference所指向的对象将被回收。备注：是除了软引用之外没有其他强引用引用的情况下。

弱引用(WeakReference) :如果对象除了有弱引用指向它后没有其他强引用关联它，当进行年轻代垃圾回收时，该引用指向的对象就会被垃圾回收器回收。

虚引用(PhantomeReference) 该引用指向的对象，无法对垃圾收集器收集对象时产生任何影响，但在执行垃圾回收后垃圾收集器会通过注册在PhantomeReference上的队列来通知应用程序对象被回收。



## Lock接口

在Lock接口出现之前，java程序是靠synchronized关键字实现锁功能的，而java SE5之后，并发包中新增了Lock接口（以及相关实现类）用来实现锁功能，它提供了与synvhronized关键字类似的同步功能，只是在使用时需要显示地获取和释放锁。虽然它却少了（通过synchronized块或者方法所提供的）隐式获取**释放锁的便捷性**，但是却拥有了锁获取与释放的**可操作性**、可中断的获取锁以及超时获取所等多种synchronized关键字所不具备的同步特性。



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

### 原子操作类

#### 原子更新基本类型类

```
AtomicBoolean

AtomicInteger

AtomicLong
```



#### 原子更新数组

```
AtomicIntegerArray

AtomicLongArray

AtomicReferenceArray

AtomicIntegerArray
```



#### 原子更新应用类型

```
AtomicReference

AtomicReferenceFieldUpdater

AtomicMarkableReference
```



#### 原子更新字段类

```
AtomicIntegerFieldUpdater

AtomicLongFieldUpdater

AtomicStampedReference
```





## 栈的使用场景

### 栈在函数调用中的应用

### 栈在表达式求值中的应用

实际上，编译器就是通过两个栈来实现的。其中一个保存操作数的栈，另一个是保存运算符的栈。我们从左向右遍历表达式，当遇到数字，我们就直接压入操作数栈；当遇到运算符，就与运算符栈的栈顶元素进行比较。如果比运算符栈顶元素的优先级高，就将当前运算符压入栈；如果比运算符栈顶元素的优先级低或者相同，从运算符栈中取栈顶运算符，从操作数栈的栈顶取 2 个操作数，然后进行计算，再把计算完的结果压入操作数栈，继续比较。



![img](https://static001.geekbang.org/resource/image/bc/00/bc77c8d33375750f1700eb7778551600.jpg)

### 栈在括号匹配中的应用



![img](https://static001.geekbang.org/resource/image/fb/cd/fb8394a588b12ff6695cfd664afb17cd.jpg)



哈希算法 MD5 SHA CRC



MD5 Message-Digest Algorithm，MD5 消息摘要算法

SHA  Secure Hash Algorithm，安全散列算法

HASH算法的基本要求

- 从哈希值不能反向推导出原始数据（所以哈希算法也叫单向哈希算法）；

- 对输入数据非常敏感，哪怕原始数据只修改了一个 Bit，最后得到的哈希值也大不相同；

- 散列冲突的概率要很小，对于不同的原始数据，哈希值相同的概率非常小；

- 哈希算法的执行效率要尽量高效，针对较长的文本，也能快速地计算出哈希值。

  

**鸽巢原理**（也叫抽屉原理）。这个原理本身很简单，它是说，如果有 10 个鸽巢，有 11 只鸽子，那肯定有 1 个鸽巢中的鸽子数量多于 1 个，换句话说就是，肯定有 2 只鸽子在 1 个鸽巢内。

应用一：安全加密

应用二：唯一标识

应用三：数据校验

应用四：散列函数

应用五：负载均衡

应用六：数据分片

应用七：分布式存储



![img](https://static001.geekbang.org/resource/image/ab/16/ab103822e75b5b15c615b68560cb2416.jpg)

![img](https://static001.geekbang.org/resource/image/29/2c/299c615bc2e00dc32225f4d9e3490e2c.jpg)



左旋（rotate left）、右旋（rotate right）。左旋全称其实是叫围绕某个节点的左旋，那右旋的全称估计你已经猜到了，就叫围绕某个节点的右旋。

![img](https://static001.geekbang.org/resource/image/0e/1e/0e37e597737012593a93105ebbf4591e.jpg)



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
 Entry<K,V> next = e.next;
 // i=3
 int i = indexFor(e.hash, newCapacity);
 //e.next = null ，刚初始化时新数组的元素为null
 e.next = newTable[i];
 //给新数组i位置 赋值 3
 newTable[i] = e;
 // e = 7
 e = next;
```

第二轮

```java
 //next= 5   e = 7
 Entry<K,V> next = e.next;
 // i=3
 int i = indexFor(e.hash, newCapacity);
 //e.next = 3 ，此时相同位置上已经有key=3的值了，将该值赋值给当前元素的next
 e.next = newTable[i];
 //给新数组i位置 赋值 7
 newTable[i] = e;
 // e = 5
 e = next;
```

**头插法**

## HashMap1.8ConcurrentModificationException问题

https://blog.csdn.net/weixin_30587025/article/details/96339354

## hashmap流程图

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200331123933241.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI3Njg2Nzc5,size_16,color_FFFFFF,t_70)

### 为什么hashmap要h>>>16

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

```

​		因为大多数时候map的数组length不是很大 不会超过65536 这样在hashcode有效位数很多的情况下，由于数组length过小，就会导致整体的散列性差

### 为什么使用 ^ 不用&或者｜

​		因为&和|都会使得结果偏向0或者1 ,并不是均匀的概念,所以用^。	

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

## Syhchronized和ReentrantLock的区别

1、等待可中断**

**2、公平锁**

**3、锁绑定多个条件**

## 为什么synchronized与ReentrantLock都可满足需要时优先使用synchronized？

1、synchronized在语法上清晰，简单易用

2、Lock对于释放锁还是得由程序员自己保证，synchronized的话则可以由java虚拟机来确保即使出现异常，所也能被自动释放。

3、对于长远来看，synchronized优化的空间的可能性更大，因为锁是记录在对象头里面

### Synchronized原理

![preview](https://pic4.zhimg.com/v2-b7da59b4bc2a8957ac86a8eed7290da7_r.jpg)



### Markword布局

![image-20220711125639043](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220711125639043.png)

## 锁优化

**自适应自旋锁**：根据前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定的，如果再同一个锁对象上，自旋等待刚刚成功获得过锁，并且持有锁的线程正在运行中，那么虚拟机就会认为这次自旋也很有可能再次成功。

**锁消除：**锁消除是指虚拟机即使编译器在运行时检测到某段需要同步的代码根本不可能存在共享数据竞争而实施的一种对锁进行消除的优化策略。

**锁粗化：**如果一系列的连续操作都对同一个对象反复加锁和解锁，设置加锁操作是出现在循环体中，如果虚拟机检探测到有这样一串零碎的操作都对同一个对象加锁，将会把枷锁同步的范围扩展（粗化）到整个操作序列的外部。

**轻量级锁：**在代码即将进入同步块的时候，如果此同步对象没有被锁定（锁标志位为'01'状态），虚拟机首先将在当前线程的栈帧中建立一个名为锁记录(Lock Record)的空间，用于存储锁对象目前的mark work的拷贝（官方为这份拷贝加了一个Displaced前缀，即Displaced Mark Work）。

​		然后，虚拟机将使用CAS操作尝试把对象的Mark Word更新为指向Lock Record的指针。如果这个更新动作成功了，即代表该线程拥有了这个对象的锁，并且对象Mark Word的锁标志位（Mark Word的最后两个比特）将转变为“00”，表示此对象处于轻量级锁定状态。

​		如果这个更新操作失败了，那就意味着至少存在一条线程与当前线程竞争获取该对象的锁。虚拟机首先会检查对象的Mark Word是否指向当前线程的栈帧，如果是，说明当前线程已经拥有了这个对象的锁，那直接进入同步块继续执行就可以了，否则就说明这个锁对象已经被其他线程抢占了。如果出现两条以上的线程争用同一个锁的情况，那轻量级锁就不再有效，必须要膨胀为重量级锁，锁标志的状态值变为“10”，此时Mark Word中存储的就是指向重量级锁（互斥量）的指针，后面等待锁的线程也必须进入阻塞状态。

**偏向锁：**

​		它的目的是消除数据在无竞争情况下的同步原语，进一步提高程序的运行性能。

​		**当一个对象已经计算过抑制性哈希码后，他就再也无法进入偏向锁状态了，因为储存了hashcode，**

​		**而当一个对象当前正处于偏向锁状态，又收到需要计算其一致性哈希码请求时，他的偏向状态会被立即撤销，并且锁会膨胀为重量级锁。**

采用了一种等到竞争出现才释放锁的机制。

![image-20220705210448594](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20220705210448594.png)

​		偏向锁在Java 6和Java 7里是默认启用的，但是它在应用程序启动几秒钟之后才激活，如有必要可以使用JVM参数来关闭延迟：-XX:BiasedLockingStartupDelay=0。如果你确定应用程序里所有的锁通常情况下处于竞争状态，可以通过JVM参数关闭偏向锁：-XX:- UseBiasedLocking=false，那么程序默认会进入轻量级锁状态。 



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
  FreeNext      = NULL ;  
  _EntryList    = NULL ;    //_owner从该双向循环链表中唤醒线程结点，_EntryList是第一个节点
  _SpinFreq     = 0 ;  
  _SpinClock    = 0 ;  
  OwnerIsThread = 0 ;  
}  
```







# java位移

## << >>

**左（右）移位数大于等于32位操作时，会先求余（%）后再进行左（右）移操作（同理，long是取余64）**

```markdown
>>>
而无符号右移运算符是补上0，也就是说，对于正数移位来说等同于：>>，负数通过此移位运算符能移位成正数。以-733183670>>>8为例来画一下图
```



## 二进制的三种表达形式

```text
负数
<< 左移 补位0
>> 右移 补位1
```

原码：负数绝对值的二进制（符号为1）

反码：原码取反（符号为不参与）

补码：取反之后 + 1

-8

原码：1000 1000

反码：1111 0111

补码：1111 1000（程序里存的二进制就是这个）



都是先移位在减一，然后取反得到数。

-8 >> 2

1、1111 1110 -1 = 1111 1101

2、1111 1101 取反 1000 0010（符号为不参与）

3、1000 0010  = -2



-8 << 2

1、1110 0000 -1 = 1101 1111 (先移位再减)

2、1101 1111取反 1010 0000 （符号为不参与）

3、1010 0000 = -32





# CycliBarriar和CountdownLatch的区别？

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

### Object.wait() 方法

会释放锁，需要搭配synchronized使用

**为什么需要搭配synchronized使用：**https://blog.csdn.net/lengxiao1993/article/details/52296220

### LockSupport.park() 方法

不会释放锁，会响应中断  



# ReentrantLock为什么非公平比公平快

在激烈竞争的情况下，非公平锁的性能高于公平锁的性能的一个原因是：在恢复一个被挂起的线程与该线程真正开始运行之间存在着严重的延迟。假设线程A持有一个锁，并且线程B请求这个锁。由于这个锁已被线程A持有，因此B将被挂起。当A释放锁时，B将被唤醒，因此会再次尝试获取锁。与此同时，如果C也请求这个锁，那么C很可能会在B被完全唤醒之前获得、使用以及释放这个锁。这样的情况是一个"双赢"的局面：B获得锁的时刻并没有推迟，C更早地获得了锁，并且吞吐量也获得了提高。



# Thread的Run运行过程

​		JavaThread的启动最终都要通过一个native方法java.lang.Thread#start0()完成的，这个方法经过解释器的native_entry入口，调用到了JVM_StartThread()函数。其中的static void thread_entry(JavaThread* thread, TRAPS)函数中会调用JavaCalls::call_virtual()函数。JavaThread最终会通过JavaCalls::call_virtual()函数来调用字节码中的run()方法。

# 线程的两种状态

线程有两种状态，在任何一个时间点上，线程是可结合的（joinable），或者是分离的（detached）。 一个可结合的线程能够被其他线程收回其资源和杀死； 相反，一个分离的线程是不能被其他线程回收或杀死的，它的存储器资源在它终止时由系统自动释放。

