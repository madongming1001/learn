package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

/**
 * @author dongming.ma
 * @date 2022/10/27 14:47
 */
public class JavaInterruptedPractice {


    @SneakyThrows
    public static void main(String args[]) {
        Thread t1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {

            }
            System.out.println("is thread t1 interrupted..: " + Thread.currentThread().interrupted());
            System.out.println("is thread t1 interrupted..: " + Thread.currentThread().interrupted());
        });
        t1.start();
        t1.interrupt();
    }
}
