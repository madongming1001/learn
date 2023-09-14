package com.madm.learnroute.javaee.concurrent.juc;

import java.util.concurrent.TimeUnit;

/**
 * 可以知道导致虚假唤醒的原因主要就是一个线程直接在if代码块中被唤醒了，这时它已经跳过了if判断。
 *
 * @author dongming.ma
 * @date 2023/9/14 18:49
 */
public class SpuriousWakeupPractice {
    private int product = 0;
    public synchronized void get() {
        if (product >= 10) {
            System.out.println(Thread.currentThread().getName() + ":" + "产品已满");
            /** 商品已经满的时候进货线程挂起 */
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            /**进货*/
            System.out.println(Thread.currentThread().getName() + ":" + ++product);
            /**唤醒其他线程*/
            notifyAll();
        }
    }

    public synchronized void sale() {
        if (product <= 0) {
            System.out.println(Thread.currentThread().getName() + ":" + "产品已空");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            /**售货*/
            System.out.println(Thread.currentThread().getName() + ":" + --product);
            /**唤醒其他线程*/
            notify();
        }
    }

    public static void main(String[] args) {
        SpuriousWakeupPractice sw = new SpuriousWakeupPractice();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sw.get();
            }
        }, "生产者A").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sw.get();
            }
        }, "生产者B").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sw.sale();
            }
        }, "消费者C").start();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                sw.sale();
            }
        }, "消费者D").start();
    }

}
