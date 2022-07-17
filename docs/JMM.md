# CPU有缓存一致性协议(MESI)，为何还需要volatile

> 前言

- [并发编程从操作系统底层工作的整体认识开始](https://juejin.cn/post/6889076335369519118)
- [深入理解Java内存模型(JMM)及volatile关键字](https://juejin.cn/post/6893430262084927496)
- [CPU有缓存一致性协议(MESI)，为何还需要volatile](https://juejin.cn/post/6893792938824990734)

前面我们从操作系统底层了解了现代计算机结构模型中的CPU指令结构、CPU缓存结构、CPU运行调度以及操作系统内存管理，并且学习了Java内存模型(JMM)和 volatile 关键字的一些特性。本篇来深入理解CPU缓存一致性协议(MESI)，最后来讨论既然CPU有缓存一致性协议（MESI），为什么JMM还需要volatile关键字？

# CPU高速缓存（Cache Memory）

## CPU为何要有高速缓存

CPU在摩尔定律的指导下以每18个月翻一番的速度在发展，然而内存和硬盘的发展速度远远不及CPU。这就造成了高性能能的内存和硬盘价格及其昂贵。然而CPU的高度运算需要高速的数据。为了解决这个问题，CPU厂商在CPU中内置了少量的高速缓存以解决I\O速度和CPU运算速度之间的不匹配问题。

在CPU访问存储设备时，无论是存取数据抑或读取指令，都趋于聚集在一片连续的区域中，这就被称为局部性原理。

**时间局部性（Temporal Locality）**：如果一个信息项正在被访问，那么在近期它很可能还会被再次访问。

> 比如循环、递归、方法的反复调用等。

**空间局部性（Spatial Locality）**：如果一个存储器的位置被引用，那么将来他附近的位置也会被引用。

> 比如顺序执行的代码、连续创建的两个对象、数组等。

### 带有高速缓存的CPU执行计算的流程

1. 程序以及数据被加载到主内存
2. 指令和数据被加载到CPU的高速缓存
3. CPU执行指令，把结果写到高速缓存
4. 高速缓存中的数据写回主内存

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5d2c2c6de67f4ff982f920f3b36c1bd2~tplv-k3u1fbpfcp-watermark.awebp)

### 目前流行的多级缓存结构

由于CPU的运算速度超越了1级缓存的数据I/O能力，CPU厂商又引入了多级的缓存结构。多级缓存结构示意图如下： ![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8e9eb9d46f92403fa388b405a84a14de~tplv-k3u1fbpfcp-watermark.awebp)

## 多核CPU多级缓存一致性协议MESI

多核CPU的情况下有**多个一级缓存**，如果保证缓存内部数据的一致，不让系统数据混乱。这里就引出了一个**一致性的协议MESI**。



### Cache Line缓存行格式

#### 高速缓存的数据结构

![img](https://img-blog.csdnimg.cn/img_convert/530400b6be69f3b3f23db013327959ec.png)

高速缓存的底层数据结构其实是一个拉链散列表的结构，就是有很多的bucket，每个bucket挂了很多的cache entry，每个 cache entry 由三个部分组成： **tag 、 cache line 、 flag**

cache line ：缓存的数据，可以包含多个变量的值
tag ：指向了这个缓存数据在主内存的数据的地址
flag ：标识了缓存行的状态，具体状态划分见下边MESI协议

**怎么在高速缓存中定位到这个变量呢？**

在处理器读写高速缓存的时候，实际上会根据变量名执行一个内存地址解码的操作，解析出来三个东西。 **index , tag 和 offerset 。**

index ：用于定位到拉链散列表中的某个 bucket
tag ：用于定位 cache entry
offerset ：用于定位一个变量在 cache line 中的位置

参考文章：https://blog.csdn.net/m0_38017860/article/details/122988861

### MESI 协议缓存状态

MESI 是指4个状态的首字母。每个 `Cache line` 有4个状态，可用2个bit表示，它们分别是：

> **缓存行（Cache line）**：缓存存储数据的单元

![img](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a705ee0f942f49e394c59e8dfb76ec45~tplv-k3u1fbpfcp-watermark.awebp)

**注意**：对于M 和 E 状态而言是精确的，它们在和该缓存行的真正状态是一致的，而 S 状态可能是非一致的。

如果一个缓存将处于**S状态**的缓存行作废了，而另一个缓存实际上可能已经独享了该缓存行，**但是该缓存却不会将缓存行升迁为E状态，这是因为其他缓存不会广播它们作废掉该缓存行的通知，**同样由于缓存并没有保存该缓存行的copy数量，因此（即使有这种通知）也没有办法确定自己是否已经独享了该缓存行。

从上面的意义来看 **E状态** 是一种投机性的优化：如果一个CPU想修改一个处于 S状态 的缓存行，总线事务需要将所有该缓存行的 copy 变成 invalid 状态，而修改 E状态 的缓存不需要使用总线事务。

> 投机：抓住机会谋取私利

### MESI 状态转换

![img](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fe98bd1427704efd8b8bbcb880449e38~tplv-k3u1fbpfcp-watermark.awebp) 理解该图的前置说明：

1. **触发事件**

![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/eadbc968dab14fcfa4f5cdcd9e20083e~tplv-k3u1fbpfcp-watermark.awebp)

1. **cache分类**

- **前提**：所有的cache共同缓存了主内存中的某一条数据。
- **本地cache**：指当前cpu的cache。
- **触发cache**：触发读写事件的cache。
- **其他cache**：指既除了以上两种之外的cache。
- **注意**：本地的事件触发 本地cache和触发cache为相同。

上图的切换解释： ![img](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/03c7cc787d5f48b5ab2d0e4e3f22d598~tplv-k3u1fbpfcp-watermark.awebp)

下图示意了，当一个cache line的调整的状态的时候，另外一个cache line 需要调整的状态。

![img](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e7966485aad94956980af86135153193~tplv-k3u1fbpfcp-watermark.awebp)

举例子来说：假设 cache 1 中有一个变量 `x = 0` 的 cache line 处于 S状态（共享）。 那么其他拥有 x 变量的 cache 2 、cache 3 等 `x` 的cache line 调整为 S状态（共享）或者调整为 I状态（无效）。

### 多核缓存协同操作

假设有三个CPU A、B、C，对应三个缓存分别是cache a、b、 c。在主内存中定义了x的引用值为0。 ![image.jpeg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/787dc6704990429c876550e63138443d~tplv-k3u1fbpfcp-watermark.awebp)

#### 单核读取

那么执行流程是： CPU A 发出了一条指令，从主内存中读取x。

从主内存通过bus读取到缓存中（远端读取Remote read）,这是该 Cache line 修改为 **E状态（独享）**. ![image.jpeg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/d671913625c540bb9b874bb285051b25~tplv-k3u1fbpfcp-watermark.awebp)

#### 双核读取

那么执行流程是：

- CPU A 发出了一条指令，从主内存中读取x。
- CPU A 从主内存通过bus读取到 cache a 中并将该 cache line 设置为 **E状态**。
- CPU B 发出了一条指令，从主内存中读取x。
- CPU B 试图从主内存中读取x时，CPU A 检测到了地址冲突。这时CPU A对相关数据做出响应。此时x 存储于cache a和cache b中，x在chche a和cache b中都被设置为S状态(共享)。

![image.jpeg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ec70b7940b8b435ab73935fe715fad17~tplv-k3u1fbpfcp-watermark.awebp)

#### 修改数据

那么执行流程是：

- CPU A 计算完成后发指令需要修改x.
- CPU A 将x设置为 **M状态(修改)** 并通知缓存了x的CPU B, CPU B将本地cache b中的x设置为 **I状态(无效)**
- CPU A 对x进行赋值。

![image.jpeg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a4e25696c1ca40e3b16ab39041eaef37~tplv-k3u1fbpfcp-watermark.awebp)

#### 同步数据

那么执行流程是：

- CPU B 发出了要读取x的指令。
- CPU B 通知 CPU A，CPU A将修改后的数据同步到主内存时 cache a 修改为 **E（独享）**
- CPU A同步CPU B的x，将cache a和同步后cache b中的x设置为 **S状态（共享）**。

![image.jpeg](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ddd3b5b6da454d8480ba091059e6c4f6~tplv-k3u1fbpfcp-watermark.awebp)

## 缓存行伪共享

### 什么是伪共享？

CPU缓存系统中是以缓存行（cache line）为单位存储的。目前主流的CPU Cache 的 Cache Line 大小都是64Bytes。在多线程情况下，如果需要修改“**共享同一个缓存行的变量**”，就会无意中影响彼此的性能，这就是伪共享（False Sharing）。

**举个例子**: 现在有2个long 型变量 a 、b，如果有t1在访问a，t2在访问b，而a与b刚好在同一个cache line中，此时t1先修改a，将导致b被刷新！

### 怎么解决伪共享？

Java8中新增了一个注解： `@sun.misc.Contended` 。加上这个注解的类会自动补齐缓存行，需要注意的是此注解默认是无效的，需要在jvm启动时设置 `-XX:-RestrictContended` 才会生效。

```java
@sun.misc.Contended
public final static class VolatileLong {
    public volatile long value = 0L;
    //public long p1, p2, p3, p4, p5, p6;
}
复制代码
```



# MESI优化和他们引入的问题

缓存的一致性消息传递是要时间的，这就使其切换时会产生延迟。**当一个缓存被切换状态时其他缓存收到消息完成各自的切换并且发出回应消息这么一长串的时间中CPU都会等待所有缓存响应完成。**可能出现的阻塞都会导致各种各样的性能问题和稳定性问题。



## CPU切换状态阻塞解决-存储缓存（Store Bufferes）

比如你需要修改本地缓存中的一条信息，那么你必须将 **I（无效）状态**通知到其他拥有该缓存数据的CPU缓存中，并且等待确认。等待确认的过程会阻塞处理器，这会降低处理器的性能。应为这个等待远远比一个指令的执行时间长的多。



## Store Bufferes

为了避免这种CPU运算能力的浪费，`Store Bufferes` 被引入使用。处理器把它想要写入到主存的值写到缓存，然后继续去处理其他事情。当所有**失效确认**（Invalidate Acknowledge）都接收到时，数据才会最终被提交。但这么做有两个风险。



## Store Bufferes的风险

- 第一：就是处理器会尝试从存储缓存（Store buffer）中读取值，但它还没有进行提交。这个的解决方案称为 `Store Forwarding`，它使得加载的时候，如果存储缓存中存在，则进行返回。
- 第二：保存什么时候会完成，这个并没有任何保证。

```java
value = 3；
void exeToCPUA(){
  value = 10;
  isFinsh = true;
}
void exeToCPUB(){
  if(isFinsh){
    //value一定等于10？！
    assert value == 10;
  }
}
复制代码
```

试想一下开始执行时，CPU A 保存着 `isFinsh` 在 E(独享)状态，而 `value` 并没有保存在它的缓存中。（例如，Invalid）。在这种情况下，`value` 会比 `isFinsh` 更迟地抛弃存储缓存。完全有可能 CPU B 读取 `isFinsh` 的值为true，而value的值不等于10。**即isFinsh的赋值在value赋值之前**。

这种在可识别的行为中发生的变化称为**重排序（reordings）**。注意，这不意味着你的指令的位置被恶意（或者好意）地更改。 它只是意味着其他的CPU会读到跟程序中写入的顺序不一样的结果。



## 硬件内存模型

执行失效也不是一个简单的操作，它需要处理器去处理。另外，**存储缓存（Store Buffers）\**并不是无穷大的，所以处理器有时需要等待失效确认的返回。这两个操作都会使得性能大幅降低。为了应付这种情况，引入了\**失效队列(\**\*\*invalid queue\*\**\*)**。它们的约定如下：

- 对于所有的收到的Invalidate请求，Invalidate Acknowlege消息必须立刻发送
- Invalidate并不真正执行，而是被放在一个特殊的队列中，在方便的时候才会去执行。
- 处理器不会发送任何消息给所处理的缓存条目，直到它处理Invalidate。

即便是这样处理器已然不知道什么时候优化是允许的，而什么时候并不允许。

干脆处理器将这个任务丢给了写代码的人。这就是内存屏障（Memory Barriers）。

- 写屏障 Store Memory Barrier(a.k.a. ST, SMB, smp_wmb)是一条告诉处理器在执行这之后的指令之前，应用所有已经在存储缓存（store buffer）中的保存的指令。
- 读屏障Load Memory Barrier (a.k.a. LD, RMB, smp_rmb)是一条告诉处理器在执行任何的加载前，先应用所有已经在失效队列中的失效操作的指令。

```java
void executedOnCpu0() {
    value = 10;
    //在更新数据之前必须将所有存储缓存（store buffer）中的指令执行完毕。
    storeMemoryBarrier();
    finished = true;
}
void executedOnCpu1() {
    while(!finished);
    //在读取之前将所有失效队列中关于该数据的指令执行完毕。
    loadMemoryBarrier();
    assert value == 10;
}
复制代码
```

# 总结

## 既然CPU有缓存一致性协议（MESI），为什么JMM还需要volatile关键字？

**volatile**是java语言层面给出的保证，**MSEI协议**是多核cpu保证cache一致性的一种方法，中间隔的还很远，我们可以先来做几个假设：

1. 回到远古时候，那个时候cpu只有单核，或者是多核但是保证sequence consistency，当然也无所谓有没有MESI协议了。那这个时候，我们需要java语言层面的volatile的支持吗？

> 当然是需要的，因为在语言层面编译器和虚拟机为了做性能优化，可能会存在**指令重排**的可能，而volatile给我们提供了一种能力，我们可以告诉编译器，什么可以重排，什么不可以。

1. 那好，假设更进一步，假设java语言层面不会对指令做任何的优化重排，那在多核cpu的场景下，我们还需要volatile关键字吗？

> 答案仍然是需要的。**因为 MESI只是保证了多核cpu的独占cache之间的一致性**，但是cpu的并不是直接把数据写入L1 cache的，中间还可能有store buffer。有些arm和power架构的cpu还可能有load buffer或者invalid queue等等。因此，有MESI协议远远不够。

1. 再接着，让我们再做一个更大胆的假设。假设cpu中这类store buffer/invalid queue等等都不存在了，cpu是数据是直接写入cache的，读取也是直接从cache读的，那还需要volatile关键字吗？

> 你猜的没错，还需要的。原因就在这个“一致性”上。consistency和coherence都可以被翻译为一致性，**但是MSEI协议这里保证的仅仅coherence**而不是consistency。那consistency和cohence有什么区别呢？

> 下面取自wiki的一段话： Coherence deals with maintaining a global order in which writes to a single location or single variable are seen by all processors. Consistency deals with the ordering of operations to multiple locations with respect to all processors.

因此，**MESI协议最多只是保证了对于一个变量，在多个核上的读写顺序，对于多个变量而言是没有任何保证的**。很遗憾，还是需要volatile～～

1. 好的，到了现在这步，我们再来做最后一个假设，假设cpu写cache都是按照指令顺序fifo写的，那现在可以抛弃volatile了吧？你觉得呢？

> 那肯定不行啊！因为对于arm和power这个weak consistency的架构的cpu来说，它们只会保证指令之间有比如控制依赖，数据依赖，地址依赖等等依赖关系的指令间提交的先后顺序，而对于完全没有依赖关系的指令，比如x=1;y=2，它们是不会保证执行提交的顺序的，除非你使用了volatile，java把volatile编译成arm和power能够识别的barrier指令，这个时候才是按顺序的。



**借鉴地址为：https://juejin.cn/post/6893792938824990734**



## 什么是JMM模型？

​		**试图定义一种"java内存模型"来屏蔽各种硬件和操作系统的内存访问差异，以实现让java程序在各种平台下都能达到一致的内存访问效果，主要定义程序中各种变量的访问规则。**

​		Java 内存模型（Java Memory Model 简称JMM）是一种抽象的概念，来屏蔽各种硬件和操作系统的内存访问差异，已实现让java程序在各种平台下都能达到一致的内存访问差异。而他并不真实存在，它描述的一组规则或规范，通过这组规范定义了程序中各个变量（包括实例字段、静态字段和构成数组对象的元素）的访问方式。JVM运行程序的实体是线程，而每个线程创建时 JVM 都会为其创建一个工作内存（有些地方称为栈空间），用于存储线程私有的数据，而Java 内存模型中规定所有变量都存储在主内存，其主内存是共享内存区域，所有线程都可以访问，但线程对变量的操作（读取赋值等）必须在工作内存中进行，首先要将变量从主内存拷贝到增加的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，工作内存中存储这主内存中的变量副本拷贝，工作内存是每个线程的私有数据区域，因

此不同的线程间无法访问对方的工作内存，线程间的通信（传值）必须通过主内存来完成。

### JMM 不同于 JVM 内存区域模式

JMM 与 JVM 内存区域的划分是不同的概念层次，更恰当说 JMM 描述的是一组规则，通过这组规则控制各个变量在共享数据区域内和私有数据区域的访问方式，**JMM是围绕原子性、有序性、可见性展开**。JMM 与 Java 内存区域唯一相似点，都存在共享数据区域和私有数据区域，在 JMM 中主内存属于共享数据区域，从某个程度上讲应该包括了堆和方法区，而工作内存数据线程私有数据区域，从某个程度上讲则应该包括程序计数器、虚拟机栈以及本地方法栈。

线程、工作内存、主内存工作交互图（基于JMM规范），如下： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/50405aea3c03449d884a8ad4b8a9912e~tplv-k3u1fbpfcp-watermark.awebp)

### 主内存

主要存储的是Java实例对象，所有线程创建的实例对象都存放在主内存中，不管该**实例对象是成员变量还是方法中的本地变量**（也称局部变量），当然也包括了共享的类信息、常量、静态变量。由于是共享数据区域，多个线程同一个变量进行访问可能会发送线程安全问题。

### 工作内存

主要存储当前方法的所有本地变量信息（工作内存中存储着主内存中的变量副本拷贝），每个线程只能访问自己的工作内存，即线程中的本地变量对其他线程是不可见的，就算是两个线程执行的是同一段代码，它们也会在各自的工作内存中创建属于当前线程的本地变量，当然也包括了字节码行号指示器、相关Native方法的信息。注意由于工作内存是每个线程的私有数据，线程间无法相互访问工作内存，因此存储在工作内存的数据不存在线程安全问题。

根据 JVM 虚拟机规范主内存与工作内存的数据存储类型以及操作方式，对于一个实例对象中的成员方法而言，如果方法中包括本地变量是基本数据类型（boolean、type、short、char、int、long、float、double），将直接存储在工作内存的帧栈中，而对象实例将存储在主内存（共享数据区域，堆）中。但对于实例对象的成员变量，不管它是基本数据类型或者包装类型（Integer、Double等）还是引用类型，都会被存储到堆区。至于 static 变量以及类本身相关信息将会存储在主内存中。

**针对long和double型变量的特殊规则** 

​		Java内存模型要求lock、unlock、read、load、assign、use、store、write这八种操作都具有原子性， 但是对于64位的数据类型（long和double），在模型中特别定义了一条宽松的规定：允许虚拟机将没有 被volatile修饰的64位数据的读写操作划分为两次32位的操作来进行，即允许虚拟机实现自行选择是否 要保证64位数据类型的load、store、read和write这四个操作的原子性，这就是所谓的“long和double的非 原子性协定”（Non-Atomic Treatment of double and long Variables）。 

​		如果有多个线程共享一个并未声明为volatile的long或double类型的变量，并且同时对它们进行读取 和修改操作，那么某些线程可能会读取到一个既不是原值，也不是其他线程修改值的代表了“半个变 量”的数值。不过这种读取到“半个变量”的情况是非常罕见的，经过实际测试[1]，在目前主流平台下商 用的64位Java虚拟机中并不会出现非原子性访问行为，但是对于32位的Java虚拟机，譬如比较常用的32 位x86平台下的HotSpot虚拟机，对long类型的数据确实存在非原子性访问的风险。从JDK 9起， HotSpot增加了一个实验性的参数-XX：+AlwaysAtomicAccesses（这是JEP 188对Java内存模型更新的 一部分内容）来约束虚拟机对所有数据类型进行原子性的访问。而针对double类型，由于现代中央处 理器中一般都包含专门用于处理浮点数据的**浮点运算器（Floating Point Unit，FPU）**，用来专门处理 单、双精度的浮点数据，所以哪怕是32位虚拟机中通常也不会出现非原子性访问的问题，实际测试也证实了这一点。笔者的看法是，在实际开发中，除非该数据有明确可知的线程竞争，否则我们在编写代码时一般不需要因为这个原因刻意把用到的long和double变量专门声明为volatile。 



需要注意的是，在主内存中的实例对象可以被多线程共享，倘若两个线程同时调用类同一个对象的同一个方法，那么两个线程会将要操作的数据拷贝一份到直接的工作内存中，执行晚操作后才刷新到主内存。模型如下图所示： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8f1d9a661be646c0955733a2f9e917b6~tplv-k3u1fbpfcp-watermark.awebp)

### Java 内存模型与硬件内存架构的关系

通过对前面的硬件内存架构、Java内存模型以及Java多线程的实现原理的了解，我们应该已经意识到，多线程的执行最终都会映射到硬件处理器上进行执行，但Java内存模型和硬件内存架构并不完全一致。对于硬件内存来说只有寄存器、缓存内存、主内存的概念，并没有工作内存（线程私有数据区域）和主内存（堆内存）之分，也就是说 Java 内存模型对内存的划分对硬件内存并没有任何影响，因为 JMM 只是一种抽象的概念，是一组规则，并不实际存在，不管是工作内存的数据还是主内存的数据，对于计算机硬件来说都会存储在计算机主内存中，当然也有可能存储到 CPU 缓存或者寄存器中，因此总体上来说，Java 内存模型和计算机硬件内存架构是一个相互交叉的关系，是一种抽象概念划分与真实物理硬件的交叉。（注意对于Java内存区域划分也是同样的道理） ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a54a1afbbe8348b3b91fc2f08923bf05~tplv-k3u1fbpfcp-watermark.awebp)

### JMM 存在的必要性

在明白了 Java 内存区域划分、硬件内存架构、Java多线程的实现原理与Java内存模型的具体关系后，接着来谈谈Java内存模型存在的必要性。

由于JVM运行程序的实体是线程，而每个线程创建时 JVM 都会为其创建一个工作内存（有些地方称为栈空间），用于存储线程私有的数据，线程与主内存中的变量操作必须通过工作内存间接完成，主要过程是将变量从主内存拷贝的每个线程各自的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，如果存在两个线程同时对一个主内存中的实例对象的变量进行操作就有可能诱发线程安全问题。

假设主内存中存在一个共享变量 `x` ，现在有 A 和 B 两个线程分别对该变量 `x=1` 进行操作， A/B线程各自的工作内存中存在共享变量副本 `x` 。假设现在 A 线程想要修改 `x` 的值为 2，而 B 线程却想要读取 `x` 的值，那么 B 线程读取到的值是 A 线程更新后的值 2 还是更新钱的值 1 呢？

答案是：不确定。即 B 线程有可能读取到 A 线程更新钱的值 1，也有可能读取到 A 线程更新后的值 2，这是因为工作内存是每个线程私有的数据区域，而线程 A 操作变量 `x` 时，首先是将变量从主内存拷贝到 A 线程的工作内存中，然后对变量进行操作，操作完成后再将变量 `x` 写回主内存。而对于 B 线程的也是类似的，这样就有可能造成主内存与工作内存间数据存在一致性问题，假设直接的工作内存中，这样 B 线程读取到的值就是 `x=1` ，但是如果 A 线程已将 `x=2` 写回主内存后，B线程才开始读取的话，那么此时 B 线程读取到的就是 `x=2` ，但到达是那种情况先发送呢？

如下图所示案例： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8a31372ce7ac4287903fc0fa8b3dec48~tplv-k3u1fbpfcp-watermark.awebp) 以上关于主内存与工作内存直接的具体交互协议，即一个变量如何从主内存拷贝到工作内存，如何从工作内存同步到主内存之间的实现细节，Java内存模型定义来以下八种操作来完成。

### 数据同步八大原子操作

1. **lock**（锁定）：作用于主内存的变量，把一个变量标记为一个线程独占状态；
2. **unlock**（解锁）：作用于主内存的变量，把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定；
3. **read**（读取）：作用于主内存的变量，把一个变量值从主内存传输到线程的工作内存中，以后随后的load工作使用；
4. **load**（载入）：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量；
5. **use**（使用）：作用于工作内存的变量，把工作内存中的一个变量值传递给执行引擎；
6. **assign**（赋值）：作用于工作内存的变量，它把一个从执行引擎接收到的值赋给工作内存的变量；
7. **store**（存储）：作用于工作内存的变量，把工作内存中的一个变量的值传送到主内存中，以便随后的write的操作；
8. **wirte**（写入）：作用于工作内存的变量，它把store操作从工作内存中的一个变量值传送到主内存的变量中。

- 如果要把一个变量从主内存中复制到工作内存中，就需要按顺序地执行 read 和 load 操作；
- 如果把变量从工作内存中同步到主内存中，就需要按顺序地执行 store 和 write 操作。

但Java 内存模型只要求上述操作必须按顺序执行，而没有保证必须是连续执行。 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a1ba3d2c065043958876e2677024e59f~tplv-k3u1fbpfcp-watermark.awebp)

#### 同步规则分析

1. 不允许一个线程无原因地（没有发生任何 assign 操作）把数据从工作内存同步回主内存中；
2. 一个新的变量只能在主内存中诞生，不允许在工作内存中直接使用一个未被初始化（load 或者 assign）的变量。即就是对一个变量实施 use 和 store 操作之前，必须先自行 assign 和 load 操作；
3. 一个变量在同一时刻只允许一条线程对其进行 lock 操作，但 lock 操作可不被同一线程重复执行多次，多次执行 lock 后，只有执行相同次数 unlock 操作，变量才会被解锁。lock 和 unlock 必须成对出现；
4. 如果对一个变量执行 lock 操作，将会清空工作内存中此变量的值，在执行引擎使用变量之前需要重新执行 load 或 assign 操作初始化变量的值；
5. 如果一个变量事先没有被 lock 操作锁定，则不允许对它执行 unlock 操作；也不允许去 unlock 一个被其他线程锁定的变量；
6. 对一个变量执行 unlock 操作之前，必须先把此变量同步到主内存中（执行store 和 write 操作）。

### 并发编程的可见性、原子性与有序性问题

#### 原子性

**原子性**指的是一个操作不可中断，即使是在多线程环境下，一个操作一旦开始就不会被其他线程影响。

在Java中，对于基本数据类型的变量的读取和赋值操作是原子性操作需要注意的是：对于32位系统来说，long 类型数据和 double 类型数据（对于基本类型数据：byte、short、int、float、boolean、char 读写是原子操作），它们的读写并非原子性的，也就是说如果存在两条线程同时对 long 类型或者 double 类型的数据进行读写是存在相互干扰的，因为对于32位虚拟机来说，每次原子读写是32位，而 long 和 double 则是64位的存储单元，这样回导致一个线程在写时，操作完成前32位的原子操作后，轮到B线程读取时，恰好只读取来后32位的数据，这样可能回读取到一个即非原值又不是线程修改值的变量，它可能是“半个变量”的数值，即64位数据被两个线程分成了两次读取。但也不必太担心，因为读取到“半个变量”的情况比较少，至少在目前的商用虚拟机中，几乎都把64位的数据的读写操作作为原子操作来执行，因此对于这个问题不必太在意，知道怎么回事即可。

```java
X=10; //原子性(简单的读取、将数字赋值给变量) 
Y = x; //变量之间的相互赋值，不是原子操作
X++; //对变量进行计算操作
X=x+1;
复制代码
```

#### 可见性

理解了**指令重排**现象后，可见性容易理解了。可见性指的是当一个线程修改了某个共享变量的值，其他线程是否能够马上得知这个修改的值。对于串行程序来说，可见性是不存在的，因为我们在任何一个操作中修改了某个变量的值，后续的操作中都能读取到这个变量，并且是修改过的新值。

但在多线程环境中可就不一定了，前面我们分析过，由于线程对共享变量的操作都是线程拷贝到各自的工作内存进行操作后才写回到主内存中的，这就可能存在一个线程A修改了共享变量 `x` 的值，还未写回主内存时，另外一个线程B又对主内存中同一个共享变量 `x` 进行操作，但此时A线程工作内存中共享变量 `x` 对线程B来说并不可见，这种工作内存与主内存同步延迟现象就会造成可见性问题，另外指令重排以及编译器优化也可能回导致可见性问题，通过前面的分析，我们知道无论是编译器优化还是处理器优化的重排现象，在多线程环境下，确实回导致程序乱序执行的问题，从而也就导致可见性问题。

#### 有序性

有序性是指对于单线程的执行代码，我们总是认为代码的执行是按顺序依次执行的，这样的理解并没有毛病，比较对于单线程而言确实如此，但对于多线程环境，则可能出现乱序现象，因为程序编译称机器码指令后可能回出现指令重排现象，重排后的指令与原指令的顺序未必一致，要明白的是，在Java程序中，倘若在本线程内，所有操作都视为有序行为，如果是多线程环境下，一个线程中观察另外一个线程，所有操作都是无序的，前半句指的是单线程内保证串行语义执行的一致性，后半句则指令重排现象和工作内存与主内存同步延迟现象。

### JMM如何解决原子性、可见性和有序性问题

#### 原子性问题

除了 JVM 自身提供的对基本数据类型读写操作的原子性外，可以通过 **synchronized** 和 **Lock** 实现原子性。因为 synchronized 和 Lock 能够保证任一时刻只有一个线程访问该代码块。

#### 可见性问题

**volatile** 关键字可以保证可见性。当一个共享变量被 volatile 关键字修饰时，它会保证修改的值立即被其他的线程看到，即修改的值立即更新到主存中，当其他线程需要读取时，它会去内存中读取新值。synchronized 和 Lock 也可以保证可见性，因为它们可以保证任一时刻只有一个线程能访问共享资源，并在其释放锁之前将修改的变量刷新到内存中。

#### 有序性问题

在Java里面，可以通过 volatile 关键字来保证一定的“有序性”。另外可以通过 synchronized 和 Lock 来保证有序性，很显然，synchronized 和 Lock 保证每个时刻是只有一个线程执行同步代码，相当于是让线程顺序执行同步代码，自然就保证来有序性。

### Java内存模型

每个线程都有自己的工作内存，线程对变量的所有操作都必须在工作内存中进行，而不能直接对主内存进行操作。并且每个线程不能访问其他线程的工作内存。Java 内存模型具有一些先天的“有序性”，即不需要通过任何手段就能够得到保证的有序性，这个通常也称为 `happens-before` 原则。如果两个操作的执行次序无法从 `happens-before` 原则推导出来，那么它们就不能保证它们的有序性，虚拟机可以随意地对它们进行重排序。

### 指令重排序

Java语言规范规定 JVM 线程内部维持顺序化语义。即只要程序的最终结果与它顺序化情况的结果相等，那么指令的执行顺序可以与代码顺序不一致，此过程叫做指令的重排序。

指令重排序的意义是什么？JVM能根据处理特性（CPU多级缓存、多核处理器等）适当的对机器指令进行重排序，使机器指令更更符合CPU的执行特性，最大限度的发挥机器性能。

下图为从源码到最终执行的指令序列示意图： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a59b55828a194752a6a0a0950dacc857~tplv-k3u1fbpfcp-watermark.awebp)

#### as-if-serial 语义

`as-if-serial` 语义的意思是：不管怎么重排序（编译器和处理器为了提高并行度），（单线程）程序的执行结果不能被改变。编译器、runtime和处理器都必须遵守 as-if-serial 语义。

为了遵守 as-if-serial 语义，编译器和处理器不会对存在数据依赖关系的操作做重排序，因为这种重排序会改变执行结果。但是，如果操作之间不存在数据依赖关系，这些操作就可能被编译器和处理器重排序。

#### happens-before 原则

只靠 synchronized 和 volatile 关键字来保证原子性、可见性以及有序性，那么编写并发程序可能会显得十分麻烦，幸运的是，从JDK 5 开始，Java 使用新的 JSR-133 内存模型，提供了 `happens-before 原则` 来辅助保证程序执行的原子性、可见性和有序性的问题，它是判断数据十分存在竞争、线程十分安全的一句。happens-before 原则内容如下：

1. **程序顺序原则**，即在一个线程内必须保证语义串行，也就是说按照代码顺序执行。
2. **锁规则**，解锁（unlock）操作必然发生在后续的同一个锁的加锁（lock）之前，也就是说，如果对于一个锁解锁后，再加锁，那么加锁的动作必须在解锁动作之后（同一个锁）。
3. **volatile规则**， volatile变量的写，先发生于读，这保证了volatile变量的可见性，简单理解就是，volatile变量在每次被线程访问时，都强迫从主内存中读该变量的值，而当该变量发生变化时，又会强迫将最新的值刷新到主内存，任何时刻，不同的线程总是能够看到该变量的最新值。
4. **线程启动规则**，线程的 start() 方法先于它的每一个动作，即如果线程A在执行线程B的 start 方法之前修改了共享变量的值，那么当线程B执行start方法时，线程A对共享变量的修改对线程B可见。
5. **传递性**，A先于B，B先于C，那么A必然先于C。
6. **线程终止原则**，线程的所有操作先于线程的终结，Thread.join() 方法的作用是等待当前执行的线程终止。假设在线程B终止之前，修改了共享变量，线程A从线程B的join方法成功返回，线程B对共享变量的修改将对线程A可见。
7. **线程中断规则**，对线程 interrupt() 方法的调用先行发生于被中断线程的代码检查到中断事件的发生，可以通过 Thread.interrupted() 方法检测线程十分中断。
8. **对象终结规则**，对象的构造函数执行，结束先于 finalize() 方法。

> finalize()是Object中的方法，当垃圾回收器将要回收对象所占内存之前被调用，即当一个对象被虚拟机宣告死亡时会先调用它finalize()方法，让此对象处理它生前的最后事情（这个对象可以趁这个时机挣脱死亡的命运）。

## volatile 内存语义

volatile 是Java虚拟机提供的轻量级的同步机制。volatile 关键字有如下两个作用：

1. 保证被 volatile 修饰的共享变量对所有线程总是可见的，也就是当一个线程修改了被 volatile 修饰共享变量的值，新值总是可以被其他线程立即得知。
2. 紧张指令重排序优化。

### volatile 的可见性

关于 volatile 的可见性作用，我们必须意思到被 volatile 修饰的变量对所有线程总是立即可见的，对于 volatile 变量的所有写操作总是能立刻反应到其他线程中。

**案例**：线程A改变 initFlag 属性之后，线程B马上感知到

```java
package com.niuh.jmm;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*Jmm03_CodeVisibility.refresh
 * -Djava.compiler=NONE
 **/
@Slf4j
public class Jmm03_CodeVisibility {

    private static boolean initFlag = false;

    private volatile static int counter = 0;

    public static void refresh() {
        log.info("refresh data.......");
        initFlag = true;
        log.info("refresh data success.......");
    }

    public static void main(String[] args) {
        // 线程A
        Thread threadA = new Thread(() -> {
            while (!initFlag) {
                //System.out.println("runing");
                counter++;
            }
            log.info("线程：" + Thread.currentThread().getName()
                    + "当前线程嗅探到initFlag的状态的改变");
        }, "threadA");
        threadA.start();

        // 中间休眠500hs
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 线程B
        Thread threadB = new Thread(() -> {
            refresh();
        }, "threadB");
        threadB.start();
    }
}

复制代码
```

结合前面介绍的数据同步八大原子操作，我们来分析下：

**线程A启动后**：

- 第一步：执行read操作，**作用于主内存**，将变量`initFlag`从主内存拷贝一份，这时候还没有放到工作内存中，而是放在了总线里。如下图
- 第二步：执行load操作，**作用于工作内存**，将上一步拷贝的变量，放入工作内存中；
- 第三步：执行use（使用）操作，**作用于工作内存**，把工作内存中的变量传递给执行引擎，对于线程A来说，执行引擎会判断`initFlag = true`吗？不等于，循环一直进行

执行过程如下图： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5a230e6313b54cd3ba0d72b423ddda81~tplv-k3u1fbpfcp-watermark.awebp)

**线程B启动后**：

- 第一步：执行read操作，**作用于主内存**，从主内存拷贝`initFlag`变量，这时候拷贝的变量还没有放到工作内存中，这一步是为了load做准备；
- 第二步：执行load操作，**作用于工作内存**，将拷贝的变量放入到工作内存中；
- 第三步：执行use操作，**作用于工作内存**，将工作内存的变量传递给执行引擎，执行引擎判断`while(!initFlag)`,那么执行循环体；
- 第四步：执行assign操作，**作用于工作内存**，把从执行引擎接收的值赋值给工作内存的变量，即设置 `inifFlag = true` ;
- 第五步：执行store操作，**作用于工作内存**，将工作内存中的变量 `initFlag = true` 传递给主内存；
- 第六步：执行write操作，**作用于工作内存**，将变量写入到主内存中。

![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7cb28e17265f4be692cb47616a6ea75a~tplv-k3u1fbpfcp-watermark.awebp)

### volatile 无法保证原子性

```java
//示例
public class VolatileVisibility {
    public static volatile int i =0;
    public static void increase(){
        i++;
    }
}
复制代码
```

在并发场景下， `i` 变量的任何改变都会立马反应到其他线程中，但是如此存在多线程同时调用 increase() 方法的化，就会出现线程安全问题，毕竟 `i++` 操作并不具备原子性，该操作是先读取值，然后写回一个新值，相当于原来的值加上1，分两部完成。如果第二个线程在第一个线程读取旧值和写回新值期间读取 `i` 的值，那么第二个线程就会于第一个线程一起看到同一个值，并执行相同值的加1操作，这也就造成了线程安全失败，因此对于 increase 方法必须使用 synchronized 修饰，以便保证线程安全，需要注意的是一旦使用 synchronized 修饰方法后，由于 sunchronized 本身也具备于 volatile 相同的特性，即可见性，因此在这样的情况下就完全可以省去 volatile 修饰变量。

**案例**：起了10个线程，每个线程加到1000，10个线程，一共是10000

```java
package com.niuh.jmm;

/**
 * volatile可以保证可见性, 不能保证原子性
 */
public class Jmm04_CodeAtomic {

    private volatile static int counter = 0;
    static Object object = new Object();

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronized (object) {
                        counter++;//分三步- 读，自加，写回
                    }
                }
            });
            thread.start();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(counter);

    }
}
复制代码
```

而实际结果,不到10000, 原因是: 有并发操作.  这时候, 如果我在counter上加关键字volatile, 可以保证原子性么?

```java
private volatile static int counter = 0;
复制代码
```

我们发现, 依然不是10000, 这说明volatile不能保证原子性. 

每个线程, 只有一个操作, counter++, 为什么不能保证原子性呢? 

其实counter++不是一步完成的. 他是分为多步完成的. 我们用下面的图来解释 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ce37d6fdd2a7403a826e3dfd8a60637e~tplv-k3u1fbpfcp-watermark.awebp) 线程A通过read, load将变量加载到工作内存, 通过user将变量发送到执行引擎, 执行引擎执行counter++，这时线程B启动了, 通过read, load将变量加载到工作内存, 通过user将变量发送到执行引擎, 然后执行复制操作assign, stroe, write操作. 我们看到这是经过了n个步骤. 虽然看起来就是简单的一句话. 

当线程B执行store将数据回传到主内存的时候, 同时会通知线程A, 丢弃counter++, 而这时counter已经自加了1, 将自加后的counter丢掉, 就导致总数据少1. 

### volatile 禁止重排优化

volatile 关键字另一个作用就是禁止指令重排优化，从而避免多线程环境下程序出现乱序执行的现象，关于指令重排优化前面已经分析过，这里主要简单说明一下 volatile 是如何实现禁止指令重排优化的。先了解一个概念，**内存屏障**（Memory Barrier）

#### 硬件层的内存屏障

Intel 硬件提供了一系列的内存屏障，主要有：

1. lfence，是一种 Load Barrier 读屏障；
2. sfence，是一种 Store Barrier 写屏障；
3. mfence，是一种全能型的屏障，具备 lfence 和 sfence 的能力；
4. Lock 前缀，Lock 不是一种内存屏障，但是它能完成类似内存屏障的功能。**Lock 会对 CPU总线和高速缓存加锁**，可以理解为 CPU 指令级的一种锁。它后面可以跟 ADD、ADC、AND、BTC、BTR、BTS、CMPXCHG、CMPXCH8B、DEC、INC、NEG、NOT、OR、SBB、SUB、XOR、XADD、and XCHG 等指令。

#### JVM的内存屏障

不同硬件实现内存屏障的方式不同，Java 内存模型屏蔽了这些底层硬件平台的差异，由 JVM 来为不同平台生产相应的机器码。JVM中提供了四类内存屏障指令：

| **指令示例**             | **说明**                                                     |
| ------------------------ | ------------------------------------------------------------ |
| Load1;LoadLoad;Load2     | 保证load1的读取操作和load2及后续读取操作之前执行             |
| Store1;StoreStore;Store2 | 在store2及其后的写操作执行前，保证store1的写操作已刷新到主内存 |
| Load1;LoadStore;Store2   | 在store2及其后的写操作执行前，保证load1的读操作已经读取结束  |
| Store1;StoreLoad;Load2   | 在store1的写操作已刷新到主内存之后，load2及其后的读操作才能执行 |

内存屏障，又称**内存栅栏**，是一个CPU指令，它的作用有两个：

1. 一是保证特定操作的执行顺序；
2. 二是保证某些变量的内存可见性（利用该特性实现 volatile 的内存可见性）。

由于编译器和处理器都能执行指令重排优化。如果在指令间插入一条 `Memory Barrier` 则会高速编译器和CPU，不管什么指令都不能和这条 Memory Barrier 指令重排序，也就是说通过插入内存屏障禁止在内存屏障前后的指令执行重排序优化。

Memory Barrier 的另外一个作用是强制刷出各种 CPU 的缓存数据，因此任何 CPU 上的线程都能读取到这些数据的最新版本。

总之，volatile 变量正是通过内存屏障实现其内存中的语义，即可见性和禁止重排优化。

下面看一个非常典型的禁止重排优化的例子DCL，如下：

```java
public class DoubleCheckLock {
    private volatile static DoubleCheckLock instance;
    private DoubleCheckLock(){}
    public static DoubleCheckLock getInstance(){
        //第一次检测
        if (instance==null){
            //同步
            synchronized (DoubleCheckLock.class){
                if (instance == null){
                    //多线程环境下可能会出现问题的地方
                    instance = new  DoubleCheckLock();
                }
            }
        }
        return instance;
    }
}
复制代码
```

上述代码一个经典的单例的双重检测的代码，这段代码在单线程环境下并没什么问题，但如果在多线程环境下就可能会出现线程安全的问题。因为在于某一线程执行到第一次检测，读取到 instance 不为 null 时，instance 的引用对象可能还没有完成初始化。

> 关于 单例模式 可以查看[《设计模式系列 — 单例模式》](https://link.juejin.cn?target=https%3A%2F%2Fmp.weixin.qq.com%2Fs%2Ftj2YqoRUtRgjfFMzR7KyWA)

因为 `instance = new DoubleCheckLock();` 可以分为以下3步完成（伪代码）

```java
memory = allocate(); // 1.分配对象内存空间
instance(memory); // 2.初始化对象
instance = memory; // 3.设置instance指向刚分配的内存地址，此时instance != null
复制代码
```

由于步骤1 和步骤2 间可能会重排序，如下：

```java
memory=allocate();//1.分配对象内存空间
instance=memory;//3.设置instance指向刚分配的内存地址，此时instance！=null，但是对象还没有初始化完成！
instance(memory);//2.初始化对象
复制代码
```

由于步骤2和步骤3不存在数据依赖关系，而且无论重排前还是重排后程序的指向结果在单线程中并没有改变，因此这种重排优化是允许的。但是指令重排只会保证串行语义执行的一致性（单线程），但并不会关心多线程间的语义一致性。所以当一条线程访问 instance 不为 null 时，由于 instance 实例未必已经初始化完成，也就造成来线程安全问题。那么该如何解决呢，很简单，我们使用 volatile 禁止 instance 变量被执行指令重排优化即可。

```java
//禁止指令重排优化
private volatile static DoubleCheckLock instance;
复制代码
```

### volatile 内存语义的实现

前面提到过重排序分为**编译器重排序**和**处理器重排序**。为来实现 volatile 内存语义，JMM 会分别限制这两种类型的重排序类型。

下面是JMM针对编译器制定的 volatile 重排序规则表。

| 第一个操作 | 第二个操作：普通读写 | 第二个操作：volatile读 | 第二个操作：volatile写 |
| ---------- | -------------------- | ---------------------- | ---------------------- |
| 普通读写   | 可以重排             | 可以重排               | 不可以重排             |
| volatile读 | 不可以重排           | 不可以重排             | 不可以重排             |
| volatile写 | 可以重排             | 不可以重排             | 不可以重排             |

举例来说，第二行最后一个单元格的意思是：在程序中，当第一个操作为普通变量的读或者写时，如果第二个操作为 volatile 写，则编译器不能重排序这两个操作。

从上图可以看出：

- 当第二个操作是 volatile 写时，不管第一个操作是什么，都不能重排序。这个规则确保了 volatile 写之前的操作不会被编译器重排序到 volatile 写之后。
- 当第一个操作是 volatile 读时，不管第二个操作是什么，都不能重排序。这个规则确保了 volatile 读之后的操作不会被编译器重排序到 volatie 读之前。
- 当第一个操作是 volatile 写，第二个操作是 volatile 读或写时，不能重排序。

为了实现 volatile 的内存语义，编译在生成字节码时，会在指令序列中插入内存屏障来禁止特定类型的处理器重排序。对于编译器来说，发现一个最优布置来最小化插入屏障的总数几乎不可能。为此，JMM 采取保守策略。下面是基于保守策略的JMM内存屏障插入策略。

- 在每个volatile写操作的前面插入一个StoreStore屏障；
- 在每个volatile写操作的后面插入一个StoreLoad屏障；
- 在每个volatile读操作的后面插入一个LoadLoad屏障；
- 在每个volatile读操作的后面插入一个LoadStore屏障；

上述内存屏障插入策略非常保守，但它可以保证在任一处理器平台，任意的程序中都能得到正确的 volatile 内存语义。

下面是保守策略下，**volatile 写插入**内存屏障后生成的指令序列示意图 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/64c7ea5dd48c4acda794818c6098ec5d~tplv-k3u1fbpfcp-watermark.awebp) 上图中 StoreStore 屏障可以保证在volatile 写之前，其前面的所有普通写操作已经对任意处理器可见来。这是因为StoreStore屏障将保障上面所有的普通写在 volatile 写之前刷新到主内存。

这里比较有意思的是，volatile 写后面的 StoreLoad 屏障。此屏障的作用是避免 volatile 写与后面可能有的 volatile 读/写操作重排序。因为编译器常常无法准确判断在一个 volatile 写的后面十分需要插入一个 StoreLoad 屏障（比如，一个volatile写之后方法立即return）。为来保证能正确实现 volatile 的内存语义，JMM 在采取了保守策略：在每个 volatile 写的后面，或者每个 volatile 读的前面插入一个 StoreLoad 屏障。从整体执行效率的角度考虑，JMM最终选择了在每个 volatile 写的后面插入一个 StoreLoad 屏障，因为volatile写-读内存语义的常见使用模式是：一个写线程写 volatile 变量，多个线程读同一个 volatile 变量。当读线程的数量大大超过写线程时，选择在 volatile 写之后插入 StoreLoad 屏障将带来可观的执行效率的提升。从这里可以看到JMM在实现上的一个特点：首先确保正确性，然后再去追求执行效率。

下图是在保守策略下，**volatile 读插入**内存屏障后生成的指令序列示意图 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/4a0aeb2602834459b5de3dd4b04a61fc~tplv-k3u1fbpfcp-watermark.awebp) 上图中 LoadLoad 屏障用来禁止处理器把上面的 volatile读 与下面的普通读重排序。LoadStore 屏障用来禁止处理器把上面的volatile读与下面的普通写重排序。

上述 volatile写 和 volatile读的内存屏障插入策略非常保守。在实际执行时，只要不改变 volatile写-读的内存语义，编译器可以根据具体情况省略不必要的屏障。

下面通过具体的示例代码进行说明。

```java
class VolatileBarrierExample {
       int a;
       volatile int v1 = 1;
       volatile int v2 = 2;
       void readAndWrite() {
           int i = v1;　　    // 第一个volatile读
           int j = v2;    　  // 第二个volatile读
           a = i + j;         // 普通写
           v1 = i + 1;     　 // 第一个volatile写
           v2 = j * 2;    　  // 第二个 volatile写
       }
}
复制代码
```

针对 `readAndWrite()` 方法，编译器在生成字节码时可以做如下的优化。 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/030be50cd49346ceadbe7402e0a308ee~tplv-k3u1fbpfcp-watermark.awebp) 注意，最后的 StoreLoad 屏障不能省略。因为第二个 volatile 写之后，方法立即 return。此时编译器可能无法准确判断断定后面十分会有 volatile 读或写，为了安全起见，编译器通常会在这里插入一个 StoreLoad 屏障。

上面的优化针对任意处理器平台，由于不同的处理器有不同“松紧度”的处理器内存模型，内存屏障的插入还可以根据具体的处理器内存模型继续优化。以X86处理完为例，上图中除最后的 StoreLoad 屏障外，其他的屏障都会被省略。

前面保守策略下的 volatile 读和写，在 X86 处理器平台可以优化如下图所示。X86处理器仅会对读-写操作做重排序。X86 不会对读-读、读-写 和 写-写 做重排序，因此在 X86 处理器中会省略掉这3种操作类型对应的内存屏障。在 X86 中，JMM仅需在 volatile 写后面插入一个 StoreLoad 屏障即可正确实现 volatile写-读的内存语义，这意味着在 X86 处理器中，volatile写的开销比volatile读的开销会大很多（因为执行StoreLoad的屏障开销会比较大）。 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/560f433e63a04914aa214f385034cc1d~tplv-k3u1fbpfcp-watermark.awebp)

## 


链接：https://juejin.cn/post/6893430262084927496



# MESI

参考文章：https://zhuanlan.zhihu.com/p/84500221





# [Java中的native是如何实现的（JNI）](https://segmentfault.com/a/1190000022812099)

# CPU核心数

参考文章：https://zhuanlan.zhihu.com/p/86855590





**多处理器一般是采用基于总线监听机制的高速缓存一致性协议。在NUMA系统中，通常选择基于目录(directory-based)的方式来维护Cache的一致性。**