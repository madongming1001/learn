package com.madm.learnroute.javaee.concurrent.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Fox
 */
@Slf4j
public class ThreadStopDemo {

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "获取锁");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "执行完成");
        });
        t1.start();
        Thread.sleep(2000);
        // 停止thread，并释放锁
        t1.stop();
        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "等待获取锁");
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "获取锁");
            }
        });
        t2.start();
    }
}
