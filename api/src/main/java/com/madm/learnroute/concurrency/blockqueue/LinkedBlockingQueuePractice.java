package com.madm.learnroute.concurrency.blockqueue;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueuePractice {
    static LinkedBlockingQueue<Object> objects1 = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
//        new ThreadExecutionPractice("asyncThread", () -> {
//            try {
//                TimeUnit.SECONDS.sleep(2l);
//                objects1.put("ssss");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        System.out.println("take为空，阻塞");
//        Object take = objects1.take();
//        System.out.println("take有值" + "，值为：" + take);
//        objects1.put("object");
//        LinkedBlockingQueue<Object> objects2 = new LinkedBlockingQueue<>(10);
        displacement(5, 8);
    }

    /**
     * 不用额外变量交换两个整数的值
     */
    public static void displacement(int a, int b) {
        System.out.println("a binary code : "+Integer.toBinaryString(a) + System.lineSeparator() +"b binary code : "+Integer.toBinaryString(b));
        a = a ^ b;
        System.out.println(Integer.toBinaryString(a));
        b = a ^ b;
        System.out.println(Integer.toBinaryString(b));
        a = a ^ b;
        System.out.println(Integer.toBinaryString(a));
    }
}
