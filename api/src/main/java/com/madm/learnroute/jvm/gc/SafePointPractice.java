package com.madm.learnroute.jvm.gc;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * VM options:
 * -XX:-PrintGC
 * -XX:+PrintSafepointStatistics deprecated
 *
 * java -XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsFinal 2>&1 | grep Safepoint
 * -XX:+SafepointTimeout -XX:SafepointTimeoutDelay=7000
 *
 * 参考文章:https://mp.weixin.qq.com/s/cf-rvoEVLxFrdxYZrpIXAA
 *
 * @author dongming.ma
 * @date 2022/10/20 11:17
 */
public class SafePointPractice {

    public static AtomicInteger atomicInteger = new AtomicInteger(0);

    @SneakyThrows
    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i < 5_0000_0000; i++) {
                atomicInteger.getAndIncrement();
            }
            System.out.println(Thread.currentThread().getName() + "执行结束!");
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        //没有触发安全点
//        Thread.sleep(500);
        //出发安全点
        /**
         * 看下Safepoint.cpp源码，通过注释可以了解，Java线程可能处于几种不同的状态，不同的状态提供了几种不同的处理方案。
         * 第二点内容最多，有可能是重点，所以简单梳理第一点后，从第二点开始重点分析。
         * When returning from the native code, a Java thread must check the safepoint _state to see if we must block.
         * 看到第二点的第一句话中有一个must，意思就是一个线程在运行 native 方法后，返回到 Java 线程后，一定会进行 safepoint 的检测。看看我们是否必须阻塞
         *
         * HotSpot JVM调用-XX:GuaranteedSafepointInterval 来设置safepoints的周期，
         * 每经过-XX:GuaranteedSafepointInterval 配置的时间，都会让所有线程进入 Safepoint，该选项默认为 1000ms。
         * 为什么会发生这种情况 必须等两个线程执行完毕之后打印出了总体的相加结果？
         * 首先因为 GuaranteedSafepointInterval 这个参数控制的就是让程序每隔多长时间进入一次safepoint 而进入safepoint是否进行垃圾回收是内存情况而定
         * 然后调用本地方法返回的时候就会检查当前程序是否运行到了检查点 如果到了就会挂起线程阻塞，又因为jvm虚拟机对安全点设置的优化 可数循环（循环结束后设置安全点） 不可数循环（循环中间设置安全点）
         * 所以保证了两个线程都是在运行到安全点之后才挂起的 safepoint结束之后就输出语句
         */
        Thread.sleep(1000);//为什么这里需要等到两个线程执行完毕了 才输出总的结果 safePoint 问题有安全点导致的长时间停顿
        System.out.println("num = " + atomicInteger);
    }
}
