package com.madm.learnroute.javaee;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 交叉打印
 */
public class ThreadPrintDemo {
    static AtomicInteger cxsNum = new AtomicInteger(0);
    static volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = printNumber1();
        Thread t2 = printNumber2();
        t1.start();
        t2.start();
    }

    private static Thread printNumber1() {
        return new Thread(() -> {
            for (; 100 > cxsNum.get(); ) {
                if (!flag && (cxsNum.get() == 0 || cxsNum.incrementAndGet() % 2 == 0)) {
                    try {
                        Thread.sleep(100);// 防止打印速度过快导致混乱
                    } catch (InterruptedException e) {            //NO
                    }
                    System.out.println(cxsNum.get() + "：当前线程是：" + Thread.currentThread().getName());
                    flag = true;
                }
            }
        }, "thread 1");
    }

    private static Thread printNumber2() {
        return new Thread(() -> {
            for (; 100 > cxsNum.get(); ) {
                if (flag && (cxsNum.incrementAndGet() % 2 != 0)) {
                    try {
                        Thread.sleep(100);// 防止打印速度过快导致混乱
                    } catch (InterruptedException e) {            //NO
                    }
                    System.out.println(cxsNum.get() + "：当前线程是：" + Thread.currentThread().getName());
                    flag = false;
                }
            }
        }, "thread 2");
    }


}
