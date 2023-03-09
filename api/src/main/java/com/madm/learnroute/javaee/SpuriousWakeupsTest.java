package com.madm.learnroute.javaee;

import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2023/3/8 11:05
 */
public class SpuriousWakeupsTest {
    static class Product {
        /**
         * 产品数
         */
        private int product = 0;

        /**
         * 进货
         */
        public synchronized void purchase() {
            if (product >= 10) {
                System.out.println(Thread.currentThread().getName() + ":" + "产品已满!");
                /** 当商品已经满的时候，进货线程挂起 */
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /** 进货 */
            System.out.println(Thread.currentThread().getName() + ":" + ++product);
            /** 唤醒其他线程 */
            this.notifyAll();

        }

        /**
         * 售货
         */
        public synchronized void sale() {
            if (product <= 0) {
                System.out.println(Thread.currentThread().getName() + ":" + "产品已空");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /** 售货 */
            System.out.println(Thread.currentThread().getName() + ":" + --product);
            /** 唤醒其他线程 */
            this.notify();
        }
    }

    public static void main(String[] args) {
        Product product = new Product();
        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    /** 睡眠，便于观察结果 */
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                product.purchase();
            }
        }, "生产者A").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                product.sale();
            }

        }, "消费者C").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {

                product.purchase();
            }

        }, "生产者B").start();

        new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                product.sale();
            }

        }, "消费者D").start();
    }
}
