package com.madm.learnroute.javaee;

public class InheritableThreadLocalPractice {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("hello world");
        inheritableThreadLocal.set("hello world");
        new Thread(() -> {
            System.out.println("线程内部主线程：" + threadLocal.get());
            System.out.println("子线程：" + inheritableThreadLocal.get());
        }).start();
        System.out.println("主线程：" + threadLocal.get());
    }
}
