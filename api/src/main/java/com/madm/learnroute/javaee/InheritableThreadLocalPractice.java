package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class LocalVariable {
    private Long[] a = new Long[1024 * 1024];
}

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
        threadLocal.remove();
        inheritableThreadLocal.remove();

        memoryLeak();
    }

    @SneakyThrows
    private static void memoryLeak() {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
        ThreadLocal<LocalVariable> localVariable = new ThreadLocal<>();

        Thread.sleep(5000 * 4);
        for (int i = 0; i < 50; ++i) {
            poolExecutor.execute(() -> {
                localVariable.set(new LocalVariable());
                System.out.println("use local varaible" + localVariable.get());
                localVariable.remove();
            });
        }
        System.out.println("pool execute over");

    }
}
