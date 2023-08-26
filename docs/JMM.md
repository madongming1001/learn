# 什么是JMM模型？

​		**《Java虚拟机规范》中曾试图定义一种"java内存模型"来屏蔽各种硬件和操作系统的内存访问差异，以实现让java程序在各种平台下都能达到一致的内存访问效果，主要定义程序中各种变量的访问规则。**

​		Java 内存模型（Java Memory Model 简称JMM）是一种抽象的概念，来屏蔽各种硬件和操作系统的内存访问差异，已实现让java程序在各种平台下都能达到一致的内存访问差异。而他并不真实存在，它描述的一组规则或规范，通过这组规范定义了程序中各个变量（包括实例字段、静态字段和构成数组对象的元素）的访问方式。JVM运行程序的实体是线程，而每个线程创建时 JVM 都会为其创建一个工作内存（有些地方称为栈空间），用于存储线程私有的数据，而Java 内存模型中规定所有变量都存储在主内存，其主内存是共享内存区域，所有线程都可以访问，但线程对变量的操作（读取赋值等）必须在工作内存中进行，首先要将变量从主内存拷贝到增加的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，工作内存中存储这主内存中的变量副本拷贝，工作内存是每个线程的私有数据区域，因

此不同的线程间无法访问对方的工作内存，线程间的通信（传值）必须通过主内存来完成。

## JMM 不同于 JVM 内存区域模式

JMM 与 JVM 内存区域的划分是不同的概念层次，更恰当说 JMM 描述的是一组规则，通过这组规则控制各个变量在共享数据区域内和私有数据区域的访问方式，**JMM是围绕原子性、有序性、可见性展开**。JMM 与 Java 内存区域唯一相似点，都存在共享数据区域和私有数据区域，在 JMM 中主内存属于共享数据区域，从某个程度上讲应该包括了堆和方法区，而工作内存数据线程私有数据区域，从某个程度上讲则应该包括程序计数器、虚拟机栈以及本地方法栈。

线程、工作内存、主内存工作交互图（基于JMM规范），如下： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/50405aea3c03449d884a8ad4b8a9912e~tplv-k3u1fbpfcp-watermark.awebp)

### 主内存

主要存储的是Java实例对象，所有线程创建的实例对象都存放在主内存中，不管该**实例对象是成员变量还是方法中的本地变量**（也称局部变量），当然也包括了共享的类信息、常量、静态变量。由于是共享数据区域，多个线程同一个变量进行访问可能会发送线程安全问题。

### 工作内存

主要存储当前方法的所有本地变量信息（工作内存中存储着主内存中的变量副本拷贝），每个线程只能访问自己的工作内存，即线程中的本地变量对其他线程是不可见的，就算是两个线程执行的是同一段代码，它们也会在各自的工作内存中创建属于当前线程的本地变量，当然也包括了字节码行号指示器、相关Native方法的信息。注意由于工作内存是每个线程的私有数据，线程间无法相互访问工作内存，因此存储在工作内存的数据不存在线程安全问题。

根据 JVM 虚拟机规范主内存与工作内存的数据存储类型以及操作方式，对于一个实例对象中的成员方法而言，如果方法中包括本地变量是基本数据类型（boolean、type、short、char、int、long、float、double），将直接存储在工作内存的帧栈中，而对象实例将存储在主内存（共享数据区域，堆）中。但对于实例对象的成员变量，不管它是基本数据类型或者包装类型（Integer、Double等）还是引用类型，都会被存储到堆区。至于 static 变量以及类本身相关信息将会存储在主内存中。

#### **针对long和double型变量的特殊规则** 

​		Java内存模型要求lock、unlock、read、load、assign、use、store、write这八种操作都具有原子性， 但是对于64位的数据类型（long和double），在模型中特别定义了一条宽松的规定：**允许虚拟机将没有被volatile修饰的64位数据的读写操作划分为两次32位的操作来进行**，即允许虚拟机实现自行选择是否要保证64位数据类型的load、store、read和write这四个操作的原子性，这就是所谓的“**long和double的非原子性协定**”（Non-Atomic Treatment of double and long Variables）。 

​		如果有多个线程共享一个并未声明为volatile的long或double类型的变量，并且同时对它们进行读取和修改操作，那么某些线程可能会读取到一个既不是原值，也不是其他线程修改值的代表了“半个变量”的数值。不过这种读取到“半个变量”的情况是非常罕见的，经过实际测试[1]，在目前主流平台下商用的**64位Java虚拟机**中并**不会出现**非原子性访问行为，但是对于**32位的Java虚拟机**，譬如比较常用的32 位x86平台下的HotSpot虚拟机，**对long类型的数据确实存在非原子性访问的风险。**从JDK 9起， HotSpot增加了一个实验性的参数**-XX：+AlwaysAtomicAccesses**（这是JEP 188对Java内存模型更新的 一部分内容）来约束虚拟机对所有数据类型进行原子性的访问。而针对double类型，由于**现代中央处理器**中一般都包含专门用于处理浮点数据的**浮点运算器（Floating Point Unit，FPU）**，用来专门处理单、双精度的浮点数据，所以哪怕是32位虚拟机中通常也不会出现非原子性访问的问题，实际测试也证实了这一点。笔者的看法是，在实际开发中，除非该数据有明确可知的线程竞争，否则我们在编写代码时一般不需要因为这个原因刻意把用到的long和double变量专门声明为volatile。 

需要注意的是，在主内存中的实例对象可以被多线程共享，倘若两个线程同时调用类同一个对象的同一个方法，那么两个线程会将要操作的数据拷贝一份到直接的工作内存中，执行晚操作后才刷新到主内存。模型如下图所示：![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8f1d9a661be646c0955733a2f9e917b6~tplv-k3u1fbpfcp-watermark.awebp)

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

在Java里面，可以通过 **volatile** 关键字来保证一定的“有序性”。另外可以通过 synchronized 和 Lock 来保证有序性，很显然，synchronized 和 Lock 保证每个时刻是只有一个线程执行同步代码，相当于是让线程顺序执行同步代码，自然就保证来有序性。

### 指令重排序

Java语言规范规定 JVM 线程内部维持顺序化语义。即只要程序的最终结果与它顺序化情况的结果相等，那么指令的执行顺序可以与代码顺序不一致，此过程叫做指令的重排序。

指令重排序的意义是什么？JVM能根据处理特性（CPU多级缓存、多核处理器等）适当的对机器指令进行重排序，使机器指令更更符合CPU的执行特性，最大限度的发挥机器性能。

下图为从源码到最终执行的指令序列示意图： ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a59b55828a194752a6a0a0950dacc857~tplv-k3u1fbpfcp-watermark.awebp)

#### as-if-serial 语义

`as-if-serial` 语义的意思是：不管怎么重排序（编译器和处理器为了提高并行度），（单线程）程序的执行结果不能被改变。编译器、runtime和处理器都必须遵守 as-if-serial 语义。

为了遵守 as-if-serial 语义，编译器和处理器不会对存在数据依赖关系的操作做重排序，因为这种重排序会改变执行结果。但是，如果操作之间不存在数据依赖关系，这些操作就可能被编译器和处理器重排序。

#### happens-before 原则

**屏蔽不同硬件环境下的指令重排序的规则不仅相同，提供了happends-before原则来辅助保证程序执行的原子性，只靠 synchronized 和 volatile 关键字来保证原子性、可见性以及有序性，那么编写并发程序可能会显得十分麻烦**，幸运的是，从JDK 5 开始，**Java 使用新的 JSR-133 内存模型，提供了 `happens-before 原则` 来辅助保证程序执行的原子性、可见性和有序性的问题**，它是判断数据十分存在竞争、线程十分安全的一句。happens-before 原则内容如下：

1. **程序顺序原则**，即在一个线程内必须保证语义串行，也就是说按照代码顺序执行。
2. **锁规则**，解锁（unlock）操作必然发生在后续的同一个锁的加锁（lock）之前，也就是说，如果对于一个锁解锁后，再加锁，那么加锁的动作必须在解锁动作之后（同一个锁）。
3. **volatile规则**， volatile变量的写，先发生于读，这保证了volatile变量的可见性，简单理解就是，volatile变量在每次被线程访问时，都强迫从主内存中读该变量的值，而当该变量发生变化时，又会强迫将最新的值刷新到主内存，任何时刻，不同的线程总是能够看到该变量的最新值。
4. **线程启动规则**，线程的 start() 方法先于它的每一个动作，即如果线程A在执行线程B的 start 方法之前修改了共享变量的值，那么当线程B执行start方法时，线程A对共享变量的修改对线程B可见。
5. **传递性**，A先于B，B先于C，那么A必然先于C。
6. **线程终止原则**，线程的所有操作先于线程的终结，Thread.join() 方法的作用是等待当前执行的线程终止。假设在线程B终止之前，修改了共享变量，线程A从线程B的join方法成功返回，线程B对共享变量的修改将对线程A可见。
7. **线程中断规则**，对线程 interrupt() 方法的调用先行发生于被中断线程的代码检查到中断事件的发生，可以通过 Thread.interrupted() 方法检测线程十分中断。
8. **对象终结规则**，对象的构造函数执行，结束先于 finalize() 方法。

> finalize()是Object中的方法，当垃圾回收器将要回收对象所占内存之前被调用，即当一个对象被虚拟机宣告死亡时会先调用它finalize()方法，让此对象处理它生前的最后事情（这个对象可以趁这个时机挣脱死亡的命运）。

# volatile

**volatile** 是Java虚拟机提供的轻量级的同步机制。volatile 关键字有如下两项特性：

- **保证其可见性：**当程序执行到volatile变量的读或写时，在其前面的操作肯定全部已经执行完毕，且结果已经对后面的操作可见；在其后面的操作肯定还没有执行

- **禁止指令重排：**在CPU、编译器进行指令优化时，不能把volatile变量后面的语句放到其前面执行，也不能把volatile变量前面的语句放到其后面执行

## volatile 的可见性

关于 volatile 的可见性作用，我们必须意识到被 volatile 修饰的变量对所有线程总是立即可见的，对于 volatile 变量的所有写操作总是能立刻反应到其他线程中。

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
```

## volatile 无法保证原子性

```java
//示例
public class VolatileVisibility {
    public static volatile int i =0;
    public static void increase(){
        i++;
    }
}
```

在并发场景下， `i` 变量的任何改变都会立马反应到其他线程中，但是如此存在多线程同时调用 increase() 方法的化，就会出现线程安全问题，**毕竟 `i++` 操作并不具备原子性**，该操作是先读取值，然后写回一个新值，相当于原来的值加上1，分两部完成。如果第二个线程在第一个线程读取旧值和写回新值期间读取 `i` 的值，那么第二个线程就会于第一个线程一起看到同一个值，并执行相同值的加1操作，这也就造成了线程安全失败，因此对于 increase 方法必须使用 synchronized 修饰，以便保证线程安全，需要注意的是一旦使用 synchronized 修饰方法后，由于 sunchronized 本身也具备于 volatile 相同的特性，即可见性，因此在这样的情况下就完全可以省去 volatile 修饰变量。

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

我们发现, 依然不是10000, 这说明volatile不能保证原子性. 

每个线程, 只有一个操作, counter++, 为什么不能保证原子性呢? 

其实counter++不是一步完成的. 他是分为多步完成的. 我们用下面的图来解释 ![img](https:////p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ce37d6fdd2a7403a826e3dfd8a60637e~tplv-k3u1fbpfcp-watermark.awebp) **原因：**线程A通过read, load将变量加载到工作内存, 通过use将变量发送到执行引擎, 执行引擎执行counter++，这时线程B启动了, 通过read, load将变量加载到工作内存, 通过user将变量发送到执行引擎, 然后执行复制操作assign, stroe, write操作. 我们看到这是经过了n个步骤. 虽然看起来就是简单的一句话。当线程B执行store将数据回传到主内存的时候, 同时会通知线程A, 丢弃counter++, 而这时counter已经自加了1, 将自加后的counter丢掉, 就导致总数据少1。

## volatile 禁止重排优化

volatile 关键字另一个作用就是禁止指令重排优化，从而避免多线程环境下程序出现乱序执行的现象，关于指令重排优化前面已经分析过，这里主要简单说明一下 volatile 是如何实现禁止指令重排优化的。先了解一个概念，**内存屏障**（Memory Barrier）

### 指令重排种类

1）**编译器重排序。**编译器在不改变单线程程序语义的前提下，可以重新安排语句的执行顺序。

2）**指令级并行的重排序。**现代处理器采用了**指令级并行技术**来将多条指令重叠执行。如果不存在数据依赖性，处理器可以改变语句对应机器指令的执行顺序。

3）**内存系统的重排序。**由于处理器使用缓存和读/写缓冲区，这使得加载和存储操作看上去可能是在乱序执行。

**参考文章：**https://heapdump.cn/article/3291930

**时钟周期：**由CPU时钟定义的定长时间间隔，是CPU工作的最小时间单位，也称节拍脉冲或T周期，完成一个基本动作所需要的时间。

### JSR是什么

Java分为三个体系，分别为**Java SE**（J2SE，Java2Platform Standard Edition，标准版），**JavaEE**（J2EE，Java 2Platform, Enterprise Edition，企业版），**Java ME**（J2ME，Java 2Platform Micro Edition，微型版）。

**JSR**是Java Specification Requests的缩写，意思是“Java 规范提案”。**是指向JCP(JavaCommunity Process)提出新增一个标准化技术规范的正式请求。** 

**JCP**（ Java Community Process) 是一个开放的国际组织，**主要由Java开发者以及被授权者组成，职能是发展和更新Java技术规范、参考实现（RI）、技术兼容包（TCK）**。Java技术和JCP两者的原创者都是SUN计算机公司。然而，JCP已经由SUN于1995年创造Java的非正式过程，演进到如今有数百名来自世界各地Java代表成员一同监督Java发展的正式程序。

**过程：**事物发展变化所经过的程序。

### Java有两个编译期

1、调用javac命令将java代码编译成java字节码(.class结尾的文件)

2、Unix派系平台上调用gcc命令将openjdk源码编译成汇编代码

编译期间java中所谓的指令重排主要是说编译openjdk时的指令重排，将Java代码编译成Java字节码是没有做指令重排的。即你加不加volatile，生成的字节码文件是一样的。

![image-20221020183547383](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221020183547383.png)

类属性在JVM中存储的时候会有一个属性：Access flags。JVM在运行的时候就是通过该属性来判断操作的类属性有没有加volatile修饰。

### JVM的内存屏障

为了保证内存可见性，java编译器在生成指令序列的适当位置会插入内存屏障指令来禁止指定类型的处理器重排序。

| **指令示例**             | **说明**                                                     |
| ------------------------ | ------------------------------------------------------------ |
| Load1;LoadLoad;Load2     | 保证load1的读取操作和load2及后续读取操作之前执行             |
| Store1;StoreStore;Store2 | 在store2及其后的写操作执行前，保证store1的写操作已刷新到主内存 |
| Load1;LoadStore;Store2   | 在store2及其后的写操作执行前，保证load1的读操作已经读取结束  |
| Store1;StoreLoad;Load2   | 在store1的写操作已刷新到主内存之后，load2及其后的读操作才能执行 |

**以常见的x86处理器（包括x64(也叫 x86-64, amd64)）来说，它是拥有相对较强的处理器内存模型，只允许Store-Load重排序。也因此在x86处理器的时候会省略掉这3种操作类型对应的内存屏障，在x86中，JMM仅需在volatile写后面插入一个StoreLoad屏障即可正确实现volatile写-读的内存语义。**
生产 x86 架构的 CPU 除了因特尔，它还把专利授权给了现在比较出名的超微（AMD）。然后在 1999 年 AMD 首次公开 64 位集以扩展 x86，此架构称为 AMD64。后来英特尔也推出了与之兼容的处理器，并命名Intel 64。两者一般被统称为 x86-64 或 x64，开创了 x86 的 64 位时代。
**x86指的是32位以前的处理器的指令集架构，x86-64是一个处理器的指令集架构，基于x86架构的64位拓展，向后兼容于16位及32位的x86架构。**
### 硬件层的内存屏障

Intel 硬件提供了一系列的内存屏障，主要有：

1. lfence，是一种 Load Barrier 读屏障；
2. sfence，是一种 Store Barrier 写屏障；
3. mfence，是一种全能型的屏障，具备 lfence 和 sfence 的能力；
4. Lock 前缀，Lock 不是一种内存屏障，但是它能完成类似内存屏障的功能。**Lock 会对 CPU总线和高速缓存加锁**，可以理解为 CPU 指令级的一种锁。它后面可以跟 ADD、ADC、AND、BTC、BTR、BTS、CMPXCHG、CMPXCH8B、DEC、INC、NEG、NOT、OR、SBB、SUB、XOR、XADD、and XCHG 等指令。

字节码层面acc_flags：ACC_VOLATILE

内存屏障，又称**内存栅栏**，是一个CPU指令，它的作用有两个：

1. 一是保证特定操作的执行顺序；
2. 二是保证某些变量的内存可见性（利用该特性实现 volatile 的内存可见性）。

由于编译器和处理器都能执行指令重排优化。如果在指令间插入一条 `Memory Barrier` 则会高速编译器和CPU，不管什么指令都不能和这条 Memory Barrier 指令重排序，也就是说通过插入内存屏障禁止在内存屏障前后的指令执行重排序优化。

Memory Barrier 的另外一个作用是强制刷出各种 CPU 的缓存数据，因此任何 CPU 上的线程都能读取到这些数据的最新版本。

**注：通过内存屏障来实现来进制处理器进行重排序**

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

### **lock指令的主要作用**

**lock指令要等待前面的指令全部执行完、会把buffered write刷入主存,写到主存之后，可见性由MESI协议保证。**

```java
Locking operations typically operate like I/O operations in that they wait for all previous instructions to complete and for all buffered writes to drain to memory
```

参考文章（具有用干货满满）

**从硬件层面理解volatile（java）：**https://blog.csdn.net/qq_18433441/article/details/108585843

**面试官：说一下 volitile 的内存语义，底层如何实现：**https://www.cxyxiaowu.com/20270.html

### storebuffer

 为了避免这种CPU运算能力的浪费，`Store Bufferes` 被引入使用。**处理器把它想要写入到主存的值写到缓存，然后继续去处理其他事情。**当所有**失效确认**（Invalidate Acknowledge）都接收到时，数据才会最终被提交。

#### StoreBuffer带来的问题

store buffer还会带来乱序的问题

假设a,b的初始值为0，a在cpu1的缓存中，b在cpu0的缓存中

**cpu0执行的伪代码如下**

```java
a = 1;//由于cpu0的缓存中没有a，a=1是个写指令，所以cpu0先把1放入到store buffer，然后发送read invalidate消息（可以看做read + invalidate两个消息）。再执行下一条指令
b = 1;//由于cpu0的缓存中有b，且是独占(Exclusive)，所以不需要发送消息，直接把1写入到缓存，覆盖到b原来的值。实际上b=1在a=1之前执行，所以叫做乱序了
```

**cpu1执行的伪代码如下**

```java
while(b == 0) continue; //由于cpu1的缓存中没有b，它发送一个read消息。cpu0接收到read消息之后，把自己包含b的缓存行的状态改成共享（Share），然后发送read response消息给cpu1。此消息中包括cpu0缓存中b的值，cpu1接收到b的值（假设此时b的值已经是1），退出while循环
assert(a == 1);//假设cpu1还没有接收到cpu0的read invalidate消息，此时cpu1的缓存中a的值还是0，所以执行这一行将会报错

//假设执行完assert(a == 1)之后，cpu1接收到read invalidate消息，它将自己包含a的缓存行置为无效（invalid）状态，然后发送read response（包含a，值为0）和invalidate acknowledge消息给cpu0。cpu0接收到a的值之后，填入到自己的缓存中，然后从store buffer取出a=1执行，将1覆盖到自己缓存中的a的值
```

实际的执行顺序如下

| cpu0   | cpu1                    |
| ------ | ----------------------- |
| b = 1; |                         |
|        | while(b == 0) continue; |
|        | assert(a == 1);         |
| a = 1; |                         |

那么怎么解决这个乱序问题呢？这就要引入写屏障的概念了。

**如果缓存中也有store buffer也有那么先从strore buffer读取，这个叫做store forwarding。**

### 写屏障

**假设在两个写操作之间，插入了写屏障，那么在后面一个写操作将修改的值刷入到缓存之前，要么必须等待store buffer为空，要么自己也放入到store buffer，等待store buffer中排在它前面的所有写操作执行完。**

以第二种情况举个例子

依旧假设a,b的初始值为0，a在cpu1的缓存中，b在cpu0的缓存中

**cpu0执行的伪代码如下**

```java
a = 1;//由于cpu0的缓存中没有a，a=1是个写指令，所以cpu0先把a=1放入到store buffer，然后发送read invalidate消息（可以看做read + invalidate两个消息）。再执行下一条指令
smp_mb();//写屏障，标记store buffer里面的所有条目（假设此时store buffer中只有a=1这个条目）
b = 1;//由于cpu0的缓存中有b，且是独占(Exclusive)，所以按理来说不需要发送消息，直接把1写入到缓存，覆盖到b原来的值就完事了。但是呢，由于store buffer里面存在被标记的条目，所以只好把b=1也放入到store buffer，等待a=1执行完再执行b=1。此时store buffer中存在两个条目，a=1被标记，b=1没有被标记
```

可以看到写屏障smp_mb()保证了a=1; b=1;的顺序

但是呢？又有个问题了，假设有一连串的写操作：

```java
a=1；
smp_mb();
b = 1;
c = 2;
d = 3;
```


由于写屏障的存在，后面几条写操作，比如丢入store buffer，等待a=1操作完成，才能继续执行。这看起来效率就比较低，且store buffer的大小是有限的。那么怎么解决这个问题呢？这就要引入invalidate queue的概念了

### invalid queue

执行失效也不是一个简单的操作，它需要处理器去处理。另外，**存储缓存（Store Buffers）并不是无穷大的，所以处理器有时需要等待失效确认的返回。这两个操作都会使得性能大幅降低。为了应付这种情况，引入了失效队列(invalid queue)**。

当cpu接收到invalidate消息，它必须使得自己的缓存行无效（当缓存比较繁忙的时候，这个无效需要等一会才能轮到），然后再发送invalidate response消息出去当然了，当cpu接收到大量的invalidate消息时，可以想象有些invalidate消息的处理不会很及时。为了解决这个问题，cpu引入了invalidate queue：当cpu接收到invalidate消息之后，它把invalidate消息加入到invalidate queue（后续cpu会根据invalidate queue来无效缓存行），然后立刻发送invalidate response消息出去。这样子响应就很快了，当然了也引入了新的问题。

#### invalidateQueue带来的问题

由于cpu发送invalidate response的时候，它的缓存行可能并没有失效的。后续如果这个cpu执行一个读操作，那么可能取到老的值。下面举例说明：

**假设a,b的初始值为0，a既在cpu0又在cpu1的缓存中（即缓存行是在cpu0和cpu1中都是share状态），b只在cpu0的缓存中（即缓存行在cpu0是exclusive状态，在cpu1中是invalidate状态）**

**cpu0执行的伪代码如下**

```java
a = 1;//由于cpu0的缓存中有a，a=1是个写指令，所以cpu0先把a=1放入到store buffer，然后发送invalidate消息。再执行下一条指令
smp_mb();//写屏障，标记store buffer里面的所有条目（假设此时store buffer中只有a=1这个条目）
b = 1;//由于cpu0的缓存中有b，且是独占(Exclusive)，所以按理来说不需要发送消息，直接把1写入到缓存，覆盖到b原来的值就完事了。但是呢，由于store buffer里面存在被标记的条目，所以只好把b=1也放入到store buffer，等待a=1执行完再执行b=1。此时store buffer中存在两个条目，a=1被标记，b=1没有被标记
```

**cpu1执行的伪代码如下**

```java
//假设cpu1接收到invalidate消息，它将消息放入invalidate queue，然后立刻发送invalidate response消息。cpu0接收到response消息之后，从store buffer中取出a=1，进行执行，即将自己的包含a的缓存行的状态置为Modify，值置为1。然后继续从store buffer中取出b=1，进行执行，即将自己的包含b的缓存行的状态置为Modify，值置为1
while(b == 0) continue; //由于cpu1的缓存中没有b，它发送一个read消息。cpu0接收到read消息之后，把自己包含b的缓存行的状态改成共享（Share），并刷入到主存，然后发送read response消息给cpu1。此消息中包括cpu0缓存中b的值，cpu1接收到b的值（假设此时b的值已经是1），退出while循环
assert(a == 1);//cpu1的缓存中有a，且值为0，于是执行这一行时报错
//cpu1从invalidate queue取出invalidate消息，将自己包含a的缓存行的状态置为失效。但是为时已晚，assert(a == 1)已经报错了
```

### 读屏障

读屏障会标记invalidate queue中的所有条目，读屏障之后的读操作，必须等待invalidate queue中所有被标记的条目执行完之后，才能执行

**读取modify状态的值会被刷入主存，之后改为share状态**

**依旧假设a,b的初始值为0，a既在cpu0又在cpu1的缓存中（即缓存行是在cpu0和cpu1中都是share状态），b只在cpu0的缓存中（即缓存行在cpu0是exclusive状态，在cpu1中是invalidate状态）**

```java
a = 1;//由于cpu0的缓存中有a，a=1是个写指令，所以cpu0先把a=1放入到store buffer，然后发送invalidate消息。再执行下一条指令
smp_mb();//写屏障，标记store buffer里面的所有条目（假设此时store buffer中只有a=1这个条目）
b = 1;//由于cpu0的缓存中有b，且是独占(Exclusive)，所以按理来说不需要发送消息，直接把1写入到缓存，覆盖到b原来的值就完事了。但是呢，由于store buffer里面存在被标记的条目，所以只好把b=1也放入到store buffer，等待a=1执行完再执行b=1。此时store buffer中存在两个条目，a=1被标记，b=1没有被标记
```

```java
//假设cpu1接收到invalidate消息，它将消息放入invalidate queue，然后立刻发送invalidate response消息。cpu0接收到response消息之后，从store buffer中取出a=1，进行执行，即将自己的包含a的缓存行的状态置为Modify，值置为1。然后继续从store buffer中取出b=1，进行执行，即将自己的包含b的缓存行的状态置为Modify，值置为1
​
while(b == 0) continue; //由于cpu1的缓存中没有b，它发送一个read消息（为什么不是read invalidate？因为这里不是个写指令）。cpu0接收到read消息之后，把自己包含b的缓存行的状态改成共享（Share），并刷入到主存，然后发送read response消息给cpu1。此消息中包括cpu0缓存中b的值，cpu1接收到b的值（假设此时b的值已经是1），退出while循环
​
smp_mb();//读屏障，标记invalidate queue里面的所有条目（假设invalidate queue中只有【a失效】这个条目）
​
assert(a == 1);//cpu1读取a，但是invalidate queue里面存在被标记的条目，所以必须等待。等啊等啊，终于cpu1开始取出invalidate queue的【a失效】这个条目，然后将包含a的缓存行的状态置为无效（invalid）
​
//invalidate queue中没有被标记的条目，于是cpu1可以继续执行a==1了。cpu1包含a的缓存行已经是失效状态了，于是它发送read消息。cpu0接收到read消息，将自己的包含a的缓存行的状态置为共享（share），并刷入到主存中，然后发送read response（包含a，值为1）。cpu1接收到read response之后，将a放入到自己的缓存行，此时a的值为1，那么执行assert(a == 1)不报错
```



### 有了volatile为什么还会有可见性问题？

- MESI协议只是保证了CPU的缓存一致性，volatile是java语言层面给出的保证。它们之间还差着java编译器、java虚拟机、JIT、操作系统、CPU核心


- 为了优化MESI协议的性能，cpu引入了store buffer、invalidate queue，它们也会导致可见性问题


- 有些CPU并没有支持MESI协议



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

#### 查看缓存行大小

sysctl hw.cachelinesize 128

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
伪共享指的是多个线程同时读写同一个缓存行的不同变量时导致的 CPU缓存失效。尽管这些变量之间没有任何关系，但由于在主内存中邻近，存在于同一个缓存行之中，它们的相互覆盖会导致频繁的缓存未命中，引发性能下降。
缓存行填充对于大多数原子来说是繁琐的，因为它们通常不规则地分散在内存中，因此彼此之间不会有太大的干扰。但是，驻留在数组中的原子对象往往彼此相邻，因此在没有这种预防措施的情况下，通常会共享缓存行数据（对性能有巨大的负面影响）。

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



**参考文章：https://juejin.cn/post/6893792938824990734**

# MESI

**参考文章**：https://zhuanlan.zhihu.com/p/84500221

# Java中的native是如何实现的（JNI）

```java
// Linux下是.so文件，Windows下是.dll文件
```

1. 编写带有native声明的方法的java类
2. 使用javac命令编译所编写的java类
3. 然后使用javah + java类名生成扩展名为h的头文件
4. 使用C/C++实现本地方法
5. 将C/C++编写的文件生成动态连接库 gcc命令

**参考文章：**https://segmentfault.com/a/1190000022812099

# CPU核心数

**参考文章**：https://zhuanlan.zhihu.com/p/86855590

**多处理器一般是采用基于总线监听机制的高速缓存一致性协议。在NUMA系统中，通常选择基于目录(directory-based)的方式来维护Cache的一致性。**



# 指令集 ISA Instruction Set Architecture

所谓指令集，**可以理解成硬件对外的接口**。我们运行程序是通过操作系统调度，操作系统然后让硬件去计算。
硬件架构指令集是CPU能够直接理解和执行的机器指令的集合。
不同的CPU架构具有不同的指令集，例如x86、ARM、MIPS等。硬件架构指令集定义了CPU可以执行的基本操作，例如算术运算、逻辑运算、内存访问等。

- 程序在执行之前要被编译为CPU能够理解的语言，这种语言或者说是规范就是指令集ISA
- *反映了CPU软件层面的设计*
- 指令集的Example - x86,Arm v8,Mipz

**参考文章：**https://a-suozhang.xyz/2019/09/27/ISA/

# 操作系统原语和硬件架构架构指令集的关系？

操作系统原语和硬件架构指令集是紧密相关的，但在不同的层次上发挥作用。
操作系统原语是操作系统提供的基本指令或函数，用于执行底层硬件操作或管理系统资源。它们是操作系统与硬件之间的接口，使得应用程序可以通过操作系统来访问底层硬件资源。操作系统原语通常涉及到进程管理、内存管理、文件系统操作等功能。
硬件架构指令集是CPU能够直接理解和执行的机器指令的集合。不同的CPU架构具有不同的指令集，例如x86、ARM、MIPS等。硬件架构指令集定义了CPU可以执行的基本操作，例如算术运算、逻辑运算、内存访问等。编译器将高级语言代码转换为硬件架构指令集的机器码，使得CPU可以直接执行。
在关系上，操作系统原语提供了一种抽象层，使得开发者可以使用更高级的操作系统接口而不需要了解底层硬件细节。操作系统原语通过调用底层硬件架构指令集来实现其功能。编译器则负责将高级语言代码转换为底层硬件架构指令集的机器码，以便在操作系统中执行。
因此，操作系统原语和硬件架构指令集是紧密相关的，相互配合以实现高级语言的执行和系统资源管理。



# 计算机系统

**x86指的是32位以前的处理器的指令集架构，x86-64是一个处理器的指令集架构，基于x86架构的64位拓展，向后兼容于16位及32位的x86架构。**x64于1999年由AMD设计，AMD首次公开64位集以扩展给x86，称为“AMD64”。其后也为英特尔所采用，现时英特尔称之为“Intel 64”，在之前曾使用过“Clackamas Technology” 、“IA-32e”及“EM64T”。
苹果公司和RPM包管理员以“x86-64”或“x86_64”称呼此64位架构。甲骨文公司及Microsoft称之为“x64”。BSD家族及其他Linux发行版则使用“amd64”，32位版本则称为“i386”，Arch Linux及其派生发行版用x86_64称呼此64位架构。

**现在 `x86` 一般指 `32` 位的架构。**

**该系列较早期的处理器名称是以数字来表示 `80x86`。由于以 `86` 作为结尾，包括 `Intel 8086`、`80186`、`80286`、`80386` 以及 `80486`，因此其架构被称为 `x86`。**

复杂指令集是 `x86`、`x64(也叫 x86-64, amd64)` 两种架构，专利在 `Intel` 和 `AMD` 两家公司手里， 该架构 `CPU` 主要是 `Intel` 和 `AMD` 两家公司，这种 `CPU` 常用在 `PC` 机上，包括 `Windows`，`macOS` 和 `Linux`。

简单指令集是 `arm` 一种架构，专利在 `ARM` 公司手里，该架构 `CPU` 主要有高通、三星、苹果、华为海思、联发科等公司。这种 `CPU` 常用在手机上，包括安卓和苹果。

**复杂指令集和精简指令集比较的话**，区别在于我们编程（直接写机器语言代码在 `CPU`上运行）的时候，比如实现乘法。根据提供的指令，复杂指令集可能一条命令就够了，而简单指令集我们可能需要加法、循环等多条指令。

## Intel系列处理器

- 8086处理器16位处理器 x86指令集

- [Pentium](https://baike.baidu.com/item/Pentium/2946980?fromModule=lemma_inlink)处理器

- [酷睿](https://baike.baidu.com/item/酷睿/1149953?fromModule=lemma_inlink)

## AMD

美国*AMD*半导体公司专门为计算机、通信和消费电子行业设计和制造各种创新的微处理器（CPU、GPU、主板芯片组、电视卡芯片等），以及提供闪存和低功率处理器解决方案，公司成立于1969年

- [锐龙](https://baike.baidu.com/item/锐龙/20470522?fromModule=lemma_inlink)

**CPU的工作分为以下 5 个阶段**

- 取指令（IF，instruction fetch），即将一条指令从[主存储器](https://baike.baidu.com/item/主存储器/10635399?fromModule=lemma_inlink)中取到[指令寄存器](https://baike.baidu.com/item/指令寄存器/3219483?fromModule=lemma_inlink)的过程。[程序计数器](https://baike.baidu.com/item/程序计数器/3219536?fromModule=lemma_inlink)中的数值，用来指示当前指令在主存中的位置。当 一条指令被取出后，[程序计数器](https://baike.baidu.com/item/程序计数器/3219536?fromModule=lemma_inlink)（PC）中的数值将根据指令字长度自动递增。

- 指令译码阶段（ID，instruction decode），取出指令后，[指令译码器](https://baike.baidu.com/item/指令译码器/3295261?fromModule=lemma_inlink)按照预定的指令格式，对取回的指令进行拆分和解释，识别区分出不同的指令类 别以及各种获取操作数的方法。现代[CISC](https://baike.baidu.com/item/CISC/1189443?fromModule=lemma_inlink)处理器会将拆分已提高并行率和效率。

- 执行指令阶段（EX，execute），具体实现指令的功能。CPU的不同部分被连接起来，以执行所需的操作。

- 访存取数阶段（MEM，memory），根据指令需要访问主存、读取操作数，CPU得到操作数在主存中的地址，并从主存中读取该操作数用于运算。部分指令不需要访问主存，则可以跳过该阶段。

- 结果写回阶段（WB，write back），作为最后一个阶段，结果写回阶段把执行指令阶段的运行结果数据“写回”到某种存储形式。结果数据一般会被写到CPU的内部寄存器中，以便被后续的指令快速地存取；许多指令还会改变[程序状态字寄存器](https://baike.baidu.com/item/程序状态字寄存器/10320153?fromModule=lemma_inlink)中标志位的状态，这些标志位标识着不同的操作结果，可被用来影响程序的动作。

## cpu结构

### 运算逻辑部件

主要能够进行相关的逻辑运算，如：可以执行移位操作以及逻辑操作，除此之外还可以执行定点或浮点算术运算操作以及地址运算和转换等命令，是一种多功能的运算单元，算术运算与逻辑运算。

### 寄存器部件

用来暂存指令、数据和地址的。

### 控制部件

用来对指令进行分析并且能够发出相应的控制信号，总线管理（控制与使用权），处理异常情况和特殊请求（中断）。

![image-20221022205520224](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221022205520224.png)

### 寄存器

寄存器是CPU的内部组成单元,是CPU运算时取指令和数据的地方，速度很快，寄存器可以用来暂存指令、数据和地址。

#### 通用寄存器

用来保存指令执行过程中临时存放的寄存器操作数和中间（或最终）的操作结果

#### 数据寄存器（DR）

存放操作数（满足各种数据类型）两个寄存器存放双倍字长数据

#### 地址寄存器（AR）

存放地址，其位数应满足最大的姿势范围用于特殊的寻址方式 段基值 栈指针

#### 条件码寄存器

存放条件码，可作程序分支的依据 如正、负、零、益出、进位等

#### 指令寄存器（IR）

指向了下一条要执行的指令所存放的地址，CPU的工作其实就是不断取出它指向的指令，然后执行这条指令

# 须知

**cpu和处理器的关系 其中指令集架构扮演者什么角色？**

**CPU（Central Processing Unit）是计算机中的核心组件，它负责执行指令和处理数据。处理器（Processor）是CPU的一种实现方式，是由电路和逻辑组件构成的硬件设备。
** CPU包括控制单元（Control Unit）和算术逻辑单元（Arithmetic Logic Unit），控制单元负责指令的解码和协调各个组件的工作，算术逻辑单元则执行算术运算和逻辑操作。
处理器是CPU的具体实现，可以根据不同的需求和设计理念选择不同的处理器。处理器的设计决定了CPU的性能、功耗和成本等特性。常见的处理器品牌有英特尔（Intel）、AMD、ARM等。
指令集架构（Instruction Set Architecture，ISA）定义了CPU支持的指令集和编程模型。它规定了程序员和编译器如何编写和生成机器代码，以及CPU如何执行这些指令。指令集架构在软硬件之间起到了桥梁的作用。
指令集架构对于软件开发者来说非常重要。不同的指令集架构需要使用相应的编译器和开发工具来生成可执行代码。因此，程序员需要了解目标平台的指令集架构，并针对其特性进行优化，以获得更好的性能和兼容性。
总之，CPU是计算机的核心组件，处理器是CPU的具体实现，指令集架构规定了CPU支持的指令集和编程模型，在软硬件之间起到了桥梁的作用。

**c1 c2编译器运行时将字节码转换为本地机器码 是什么码 操作系统原语吗？**

c1和c2是JVM（Java虚拟机）中的两个即时编译器。它们负责将Java字节码转换为本地机器码，以便在特定操作系统上执行。*
*本地机器码是一种特定于操作系统和硬件架构的指令集，由CPU直接执行**。 操作系统原语是指操作系统提供的基本指令或函数，用于*
*执行底层硬件操作或管理系统资源**。与此相反，c1和c2编译器是在JVM中实现的，旨在优化Java字节码以提高性能。它们使用的机器码是针对特定操作系统和硬件平台的，但不是操作系统原语。

**操作系统原语和硬件架构指令集的关系？**

操作系统原语和硬件架构指令集是紧密相关的，但在不同的层次上发挥作用。 **
操作系统原语是操作系统提供的基本指令或函数，用于执行底层硬件操作或管理系统资源。**它们是操作系统与硬件之间的接口，*
*使得应用程序可以通过操作系统来访问底层硬件资源。操作系统原语通常涉及到进程管理、内存管理、文件系统操作等功能。**
硬件架构指令集是CPU能够直接理解和执行的机器指令的集合。不同的CPU架构具有不同的指令集，例如x86、ARM、MIPS等。硬件架构指令集定义了CPU可以执行的基本操作，例如算术运算、逻辑运算、内存访问等。编译器将高级语言代码转换为硬件架构指令集的机器码，使得CPU可以直接执行。
在关系上，操作系统原语提供了一种抽象层，使得开发者可以使用更高级的操作系统接口而不需要了解底层硬件细节。操作系统原语通过调用底层硬件架构指令集来实现其功能。编译器则负责将高级语言代码转换为底层硬件架构指令集的机器码，以便在操作系统中执行。
因此，操作系统原语和硬件架构指令集是紧密相关的，相互配合以实现高级语言的执行和系统资源管理。

**cpu架构 x86 arm等是什么概念 详细介绍一下？**

x86和ARM是两种常见的CPU架构，它们代表了不同的指令集架构。 1. x86架构： -
x86架构最早由英特尔推出，后来成为个人计算机中最流行的CPU架构之一。 - x86架构使用**复杂指令集计算**（Complex Instruction Set
Computing，CISC）的设计思想。这意味着它的指令集非常丰富和复杂，可以执行多种复杂的操作。 - **x86架构广泛应用于桌面计算机、服务器和工作站等领域
**，因其**强大的计算能力和通用性能**受到广泛认可。 2. ARM架构： - ARM架构最初由英国公司ARM
Holdings开发，针对嵌入式系统和移动设备而设计。 - ARM架构使用**精简指令集**计算（Reduced Instruction Set
Computing，RISC）的设计思想。它的指令集相对较简单，但执行效率高，能够在低功耗和资源有限的环境下提供良好的性能。 - ARM架构被广泛应用于
**智能手机、平板电脑、物联网设备和嵌入式系统**等领域，因其**低功耗和高效性能**而受到青睐。
这两种架构的主要区别在于指令集的复杂性和执行效率。x86架构的指令集相对较复杂，能够执行更多的操作，适用于复杂的计算任务。而ARM架构的指令集相对简单，但执行效率高，适合于低功耗和资源有限的设备。
在软件开发中，不同的架构需要使用相应的编译器和指令集来开发和优化软件。因此，了解CPU架构对于开发者来说非常重要，以确保软件能够在特定的硬件平台上高效运行。

**cpu和处理器的关系 其中指令集架构扮演者什么角色？**

**CPU（Central Processing Unit）是计算机中的核心组件，它负责执行指令和处理数据。处理器（Processor）是CPU的一种实现方式，是由电路和逻辑组件构成的硬件设备。
** CPU包括控制单元（Control Unit）和算术逻辑单元（Arithmetic Logic Unit），控制单元负责指令的解码和协调各个组件的工作，算术逻辑单元则执行算术运算和逻辑操作。
处理器是CPU的具体实现，可以根据不同的需求和设计理念选择不同的处理器。处理器的设计决定了CPU的性能、功耗和成本等特性。常见的处理器品牌有英特尔（Intel）、AMD、ARM等。
指令集架构（Instruction Set Architecture，ISA）定义了CPU支持的指令集和编程模型。它规定了程序员和编译器如何编写和生成机器代码，以及CPU如何执行这些指令。指令集架构在软硬件之间起到了桥梁的作用。
指令集架构对于软件开发者来说非常重要。不同的指令集架构需要使用相应的编译器和开发工具来生成可执行代码。因此，程序员需要了解目标平台的指令集架构，并针对其特性进行优化，以获得更好的性能和兼容性。
总之，CPU是计算机的核心组件，处理器是CPU的具体实现，指令集架构规定了CPU支持的指令集和编程模型，在软硬件之间起到了桥梁的作用。

**不同公司生产的处理器 都有自己对应的指令集架构？**

是的，不同公司生产的处理器通常都有自己对应的指令集架构。
每个处理器厂商都会设计和推出自己的处理器产品线，并根据其设计理念和市场定位制定相应的指令集架构。一些知名的处理器厂商包括英特尔（Intel）、AMD、ARM、IBM等。
英特尔（Intel）是目前全球最大的x86架构处理器供应商，其主要产品系列包括Intel Core、Xeon等。AMD也是一家重要的x86架构处理器厂商，其产品线包括AMD
Ryzen、EPYC等。 ARM是一家专注于低功耗和嵌入式领域的处理器设计公司，其设计的ARM架构处理器在智能手机、平板电脑、物联网设备等领域应用广泛。
IBM则以POWER架构处理器为主，主要用于高性能计算和企业级服务器。
每种指令集架构都有其特定的优势和适用场景。不同的处理器厂商通过不同的指令集架构来满足不同的需求和市场。编写针对特定指令集架构的软件程序时需要考虑这些差异。