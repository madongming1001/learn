package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

public class InheritableThreadLocalPractice {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    @SneakyThrows
    public static void main(String[] args) {
        Thread.currentThread().setName("mainThread");
        threadLocal.set("hello world");
        //在set值的时候createMap把 Thread 的 inheritableThreadLocals 设置有值了
        inheritableThreadLocal.set("hello world");
        Thread inheritableThread = new Thread(() -> {
            System.out.println("线程内部主线程：" + threadLocal.get());
            System.out.println("子线程：" + inheritableThreadLocal.get());
        });
        inheritableThread.start();
        inheritableThread.join();
        System.out.println("主线程：" + threadLocal.get());
    }
}
