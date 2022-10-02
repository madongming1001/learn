package com.madm.learnroute.javaee;

import java.util.concurrent.locks.LockSupport;


/**
 * @author Fox
 *
 * -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -Xcomp
 * hsdis-amd64.dll
 *  可见性案例
 */
public class VisibilityPractice {
    // storeLoad  JVM内存屏障  ---->  (汇编层面指令)  lock; addl $0,0(%%rsp)
    // lock前缀指令不是内存屏障的指令，但是有内存屏障的效果  （立马刷新会主内存，还有一个效果是另其他工作内存缓存失效）
    private volatile boolean flag = true;

    private volatile boolean flags = true;

    private Integer count = 0;

    public void refresh() {
        flag = false;
        System.out.println(Thread.currentThread().getName() + "修改flag:"+flag);
    }

    public void load() {
        System.out.println(Thread.currentThread().getName() + "开始执行.....");
        while (flag) {
            //TODO  业务逻辑
            count++;
            //JMM模型    内存模型： 线程间通信有关   共享内存模型
            //没有跳出循环   可见性的问题
            //能够跳出循环   内存屏障
//            UnsafeFactory.getUnsafe().storeFence();
//            System.out.println("字段偏移量："+UnsafeFactory.getFieldOffset(UnsafeFactory.getUnsafe(), VisibilityPractice.class, "count"));
            Thread.yield();//能够跳出循环    ?   释放时间片，上下文切换   加载上下文：flag=true
            System.out.println(count);//能够跳出循环    内存屏障

            // 内存屏障
            LockSupport.unpark(Thread.currentThread());

            //调用方法的do while会重新读取 不调用方法的while不会重新读取 跳出循环与编译器模式也有关系
            shortWait(1000000); //1ms
            shortWait(1000);
            try {
                Thread.sleep(1);   //内存屏障
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //总结：  Java中可见性如何保证？ 方式归类有两种：
            // 1.  jvm层面 storeLoad内存屏障    ===>  x86   lock替代了mfence
            // 2.  上下文切换   Thread.yield();
            // 当前线程对共享变量的操作会存在读不到，或者不能立即读到另一个线程对此变量的写操作 不能穷举
        }
        System.out.println(Thread.currentThread().getName() + "跳出循环: count=" + count);
    }

    public static void main(String[] args) throws InterruptedException {
        VisibilityPractice test = new VisibilityPractice();

        // 线程threadA模拟数据加载场景
        Thread threadA = new Thread(() -> test.load(), "threadA");
        threadA.start();

        // 让threadA执行一会儿
        Thread.sleep(1000);
        // 线程threadB通过flag控制threadA的执行时间
        Thread threadB = new Thread(() -> test.refresh(), "threadB");
        threadB.start();

    }

    public static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }
}