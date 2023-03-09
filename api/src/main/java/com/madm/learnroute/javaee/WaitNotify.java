package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class WaitNotify {
    static boolean flag = true;
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread waitThread = new Thread(new Wait(), "WaitThread");
        waitThread.start();
        TimeUnit.SECONDS.sleep(1);
        Thread notifyThread = new Thread(new Notify(), "NotifyThread");
        notifyThread.start();
    }

    static class Wait implements Runnable {
        private int index;

        private int conCount;

        public void run() {
            synchronized (lock) {
                System.out.println("waitThread 第 " + ++index + " 次获取锁");
                while (flag) {
                    System.out.println("waitThread 第 " + ++conCount + " 次进入循环");
                    System.out.println(String.format(Thread.currentThread() + " flag is true. wait @" + new SimpleDateFormat("HH:mm:ss").format(new Date())));
                    try {
                        lock.wait();
                        System.out.println("lock notify");
                    } catch (InterruptedException e) {
                    }
                }
                System.out.println("waitThread 继续执行");
                System.out.println(Thread.currentThread() + " flag is false. running@ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }
    }

    static class Notify implements Runnable {
        @SneakyThrows
        public void run() {
            // 加锁，拥有lock的Monitor
            synchronized (lock) {
                // 获取lock的锁，然后进行通知，通知时不会释放lock的锁，
                // 直到当前线程释放了lock后，WaitThread才能从wait方法中返回
                System.out.println(Thread.currentThread() + " hold lock. notify @ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                lock.notifyAll();
                flag = false;
                TimeUnit.SECONDS.sleep(5);
            }
            synchronized (lock) {
                System.out.println(Thread.currentThread() + " hold lock again. sleep @ " + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                TimeUnit.SECONDS.sleep(5);
            }
        }
    }


}
