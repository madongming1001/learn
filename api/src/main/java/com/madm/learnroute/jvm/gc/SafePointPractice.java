package com.madm.learnroute.jvm.gc;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * VM options:
 * -XX:-PrintGC
 * -XX:+PrintSafepointStatistics deprecated
 *
 * @author dongming.ma
 * @date 2022/10/20 11:17
 */
public class SafePointPractice {

    public static AtomicInteger num = new AtomicInteger(0);

    @SneakyThrows
    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i < 21_0000_0000; i++) {
                num.getAndAdd(1);
            }
            System.out.println(Thread.currentThread().getName() + "执行结束!");
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        Thread.sleep(2000);
        System.out.println("num = " + num);
    }
}
