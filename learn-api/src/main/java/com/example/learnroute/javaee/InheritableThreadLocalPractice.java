package com.example.learnroute.javaee;

public class InheritableThreadLocalPractice {
    public static ThreadLocal<String> InheritableThreadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
    public static void main(String[] args) {
        threadLocal.set("hello world");
        threadLocal.set("hello world1");
        InheritableThreadLocal.set("hello world");
        InheritableThreadLocal.set("hello world1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程："+threadLocal.get());
            }
        }).start();
        System.out.println("主线程："+threadLocal.get());
    }
}
