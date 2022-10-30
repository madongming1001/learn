package com.madm.learnroute.javaee.concurrent.juc;

/**
 * @author Fox
 */
public class ThreadStopDemo {

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "获取锁");
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "执行完成");
            }
        });
        thread.start();
        Thread.sleep(2000);
        // 停止thread，并释放锁
        thread.stop();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "等待获取锁");
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName() + "获取锁");
                }
            }
        }).start();

    }
}
