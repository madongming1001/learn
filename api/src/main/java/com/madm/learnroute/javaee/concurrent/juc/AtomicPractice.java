package com.madm.learnroute.javaee.concurrent.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dongming.ma
 * @date 2023/9/10 18:56
 */
public class AtomicPractice {
    public static void main(String[] args) {
        AtomicInteger ai = new AtomicInteger(Integer.MAX_VALUE);
        System.out.println(ai.addAndGet(0));//最大值 10位
        System.out.println(ai.addAndGet(1));//最小值
    }
}
