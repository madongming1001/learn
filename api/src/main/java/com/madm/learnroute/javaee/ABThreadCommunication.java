package com.madm.learnroute.javaee;

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

    private volatile static boolean a = false;
    private volatile static boolean b = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("发送a线程的信号");
            a = true;
            while (!b) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("收到b线程的信号");
        });
        Thread t2 = new Thread(() -> {
            System.out.println("收到a线程的信号");
            while (!a) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("发送b线程的信号");
            b = true;
        });
        t1.start();
        t2.start();
    }

}
