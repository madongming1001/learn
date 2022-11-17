# Mac是不是基于Linux系统开发？

参考文章：https://blog.csdn.net/daocaokafei/article/details/114582246

从血统上来说，**Mac OSX基于BSD的内核。 由于BSD基本可以认为是Unix的开源版，而Linux开发中也争取与Unix兼容，所以MacOS很多设计与Linux相似。** Linux第一个GNU发行版在1992年发布，NeXStep在1989年就发布了。 所以Mac并不是一个基于Linux开发的发行版。

# 如何查看Linux内存页的大小

**getconf PAGE_SIZE**

local的mac是16384 16kb

# 什么是管程？

管程是指管理共享变量以及对共享变量操作的过程。

# 大端小端

### 一、什么是大小端？

对于一个由2个字节组成的16位整数，在[内存](https://so.csdn.net/so/search?q=内存&spm=1001.2101.3001.7020)中存储这两个字节有两种方法：一种是将低序字节存储在起始地址，这称为小端(little-endian)字节序；另一种方法是将高序字节存储在起始地址，这称为大端(big-endian)字节序。

![image-20221103185813853](/Users/madongming/IdeaProjects/learn/docs/noteImg/image-20221103185813853.png)

**参考文章：**https://blog.csdn.net/wwwlyj123321/article/details/100066463

# For循环break、continue retry

**continue retry示例**

```java
public class TestC {
    public static void main(String[] args) {
        retry:
        for (;;){
            int count = 1;
            System.out.println(count++);
            for (;;){
                System.out.println(count++);
                if (count == 5){
                    continue retry;
                }
            }
        }
    }
}
```

**break retry示例**

```java
public class TestC {
    public static void main(String[] args) {
        retry:
        for (;;){
            int count = 1;
            System.out.println(count++);
            for (;;){
                System.out.println(count++);
                if (count == 5){
                    break retry;
                }
            }
        }
    }
}
```

**参考文章：**https://blog.csdn.net/elsostal/article/details/109630920

# 错误

## 类文件具有错误的版本 55.0, 应为 52.0

**java编译文件后会产生【major.minor】信息 不同数字会对应不同的jdk版本，比如52对应的是jdk8**

```text
1.解压文件：jar -xvf caffeine-3.0.1.jar
2.查看二进制文件：javap -verbose Caffeine
```

**参考文章：**https://www.jianshu.com/p/a2b026e5790c

# UML

[UML](https://zh.wikipedia.org/wiki/统一建模语言) 是统一建模语言的简称，它是一种由一整套图表组成的标准化建模语言。UML用于帮助系统开发人员阐明，展示，构建和记录软件系统的产出。**统一建模语言（ UML）利用文本和图形文档，通过在对象之间建立更紧密的关系，来增强软件项目的分析和设计。**
(Unified Modeling Language，UML)图表可大致分为**结构性图表**和**行为性图表**两种。

**结构性图**表显示了系统在不同抽象层次和实现层次上的静态结构以及它们之间的相互关系。结构性图表中的元素表示系统中具意义的概念，可能包括抽象的、现实的和實作的概念。结构性图表有七种类型：

**行为性图表**显示了系统中对象的动态行为 ，可用以表达系统随时间的变化。行为性图表有七种类型：

**参考文章：**https://www.visual-paradigm.com/cn/guide/uml-unified-modeling-language/what-is-uml/#use-case-diagram

# fianlly在return之前执行还是之后执行

**return有表达式先计算return的表达式，之后执行finally，执行完之后返回。**

```java
public class FinallyTest1 {

    public static void main(String[] args) {
        
        System.out.println(test1());
    }

    public static int test1() {
        int b = 20;

        try {
            System.out.println("try block");

            return b += 80; 
        }
        catch (Exception e) {

            System.out.println("catch block");
        }
        finally {
            
            System.out.println("finally block");
            
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
        }
        return b;
    }
    
}
```

```command
try block
finally block
b>25, b = 100
100
```



# DAU是什么？

**日均活跃用户数量**(Daily Active User，*DAU*)*是*用于反映网站、互联网应用或网络游戏的运营情况的统计指标。

# NTP是什么？

网络时间协议，英文名称：Network Time Protocol（NTP）是用来使计算机[时间同步](https://baike.baidu.com/item/时间同步?fromModule=lemma_inlink)化的一种协议，它可以使[计算机](https://baike.baidu.com/item/计算机/140338?fromModule=lemma_inlink)对其[服务器](https://baike.baidu.com/item/服务器/100571?fromModule=lemma_inlink)或[时钟源](https://baike.baidu.com/item/时钟源/3219811?fromModule=lemma_inlink)（如石英钟，GPS等等)做同步化，它可以提供高精准度的时间校正（LAN上与标准间差小于1毫秒，WAN上几十毫秒），且可介由加密确认的方式来防止恶毒的[协议](https://baike.baidu.com/item/协议/670528?fromModule=lemma_inlink)攻击。NTP的目的是在无序的Internet环境中提供精确和健壮的时间服务

# Data Race是什么？

Data Race是指多个线程在没有正确加锁的情况下，同时访问同一块数据，并且至少有一个线程是写操作，对数据的读取和修改产生了竞争，从而导致各种不可预计的问题。



# RFO什么意思？

RFO(Read for Ownership)广播报文