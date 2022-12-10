package com.madm.learnroute.javaee;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;


/**
 * https://mp.weixin.qq.com/s/2u-dXWNVtZwpA3zG58xNaQ
 * 有两个线程，A 线程向一个集合里面依次添加元素“abc”字符串，一共添加十次，当添加到第五次的时候，
 * 希望 B 线程能够收到 A 线程的通知，然后 B 线程执行相关的业务操作。线程间通信的模型有两种：共享内存和消息传递，
 * 以下方式都是基本这两种模型来实现的：
 * 5种方式
 * 1、使用 volatile 关键字
 * 2、使用 Object 类的 wait()/notify()
 * 3、使用JUC工具类 CountDownLatch
 * 4、使用 ReentrantLock 结合 Condition
 * 5、基本 LockSupport 实现线程间的阻塞和唤醒
 * <p>2,4 等到唤醒线程代码执行完毕才释放锁，被唤醒的线程才能获取锁<p/>
 */
public class ABThreadCommunication {

    private volatile static boolean flag = false;

    public static void main(String[] args) {
        ABCommunicationForVolatile();
//        ABCommunicationForBlockingQueue();
    }

    private static void ABCommunicationForBlockingQueue() {
        SynchronousQueue bq = new SynchronousQueue();
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("发送线程1的信号");
                bq.put("a");
                String el = (String) bq.take();
                System.out.println("收到线程2的信号");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        Thread t2 = new Thread(() -> {
            try {
                String el = (String) bq.take();
                System.out.println("收到线程1的信号");
                bq.put("b");
                System.out.println("发送线程2的信号");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        t1.start();
        t2.start();
    }

    private static void ABCommunicationForVolatile() {
        Thread t1 = new Thread(() -> {
            System.out.println("发送a线程的信号");
            flag = true;
            while (flag) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("收到b线程的信号");
        });
        Thread t2 = new Thread(() -> {
            while (!flag) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("收到a线程的信号");
            System.out.println("发送b线程的信号");
            flag = false;
        });
        t1.start();
        t2.start();
    }

}
