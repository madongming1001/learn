package com.madm.learnroute.javaee;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

public class ArthasPractice {

    private static HashSet hashSet = new HashSet();
    private static volatile boolean viability = true;


    public static void main(String[] args) {
        //模拟CPU过高
        // 模拟线程死锁
        //无异常类型 CancellationException CompletionException 未经检查异常 不强制抛出
        CompletableFuture.allOf(CompletableFuture.runAsync(() -> cpuHigh()),CompletableFuture.runAsync(() -> deadThread())).join();
        // 不断的向 hashSet 集合增加数据
//        addHashSetThread();
        while (viability) System.out.println("111");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> viability = false));
    }

    /**
     * 不断的向 hashSet 集合添加数据
     */
    public static void addHashSetThread() {
        // 初始化常量
        new Thread(() -> {
            while (true) {
                int count = 1;
                while (true) {
                    try {
                        hashSet.add("count" + count);
                        Thread.sleep(1000L);
                        count++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public static void cpuHigh() {
        new Thread(() -> {
            while (true) {

            }
        }).start();
    }

    private static void deadThread() {
        /** 创建资源 */
        Object resourceA = new Object();
        Object resourceB = new Object();
        // 创建线程
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println(Thread.currentThread() + " get ResourceA");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resourceB");
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread() + " get resourceB");
                }
            }
        });
        Thread threadB = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println(Thread.currentThread() + " get ResourceB");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "waiting get resourceA");
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread() + " get resourceA");
                }
            }
        });
        threadA.start();
        threadB.start();
    }

}
