package com.madm.learnroute.javaee;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueuePractice {
    public static void main(String[] args) {
        LinkedBlockingQueue<Object> objects1 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Object> objects2 = new LinkedBlockingQueue<>(10);
        displacement(4,3);
    }

    /**
     * 不用额外变量交换两个整数的值
     */
    public static void displacement(int a, int b) {
        a = a ^ b;
        System.out.println("第一步a是：" + a);
        b = a ^ b;
        System.out.println("第二步b是：" + b);
        a = a ^ b;
        System.out.println("第三步a是：" + a);
    }
}
