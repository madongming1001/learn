package com.madm.learnroute.javaee;

import com.madm.learnroute.jvm.juc.threadPool.ThreadExecutionPractice;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LinkedBlockingQueuePractice {
    private static LinkedBlockingQueue<Object> objects1 = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        new ThreadExecutionPractice("asyncThread", () -> {
            try {
                TimeUnit.SECONDS.sleep(2l);
                objects1.put("ssss");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("take为空，阻塞");
        Object take = objects1.take();
        System.out.println("take有值" + "，值为：" + take);
        objects1.put("object");
        LinkedBlockingQueue<Object> objects2 = new LinkedBlockingQueue<>(10);
        displacement(4, 3);
    }

    /**
     * 不用额外变量交换两个整数的值
     */
    public static void displacement(int a, int b) {
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
    }
}
