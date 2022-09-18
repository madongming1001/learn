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
            for (int i = 0; i < 5_0000_0000; i++) {
                num.getAndIncrement();
            }
            System.out.println(Thread.currentThread().getName() + "执行结束!");
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        //没有触发安全点
        Thread.sleep(1000);
        System.out.println("num = " + num);
    }
}
