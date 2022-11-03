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

**参考文章：**https://blog.csdn.net/elsostal/article/details/109630920

# 错误

## 类文件具有错误的版本 55.0, 应为 52.0

```text
1.解压文件：jar -xvf caffeine-3.0.1.jar
2.查看二进制文件：javap -verbose Caffeine
```

**参考文章：**https://www.jianshu.com/p/a2b026e5790c