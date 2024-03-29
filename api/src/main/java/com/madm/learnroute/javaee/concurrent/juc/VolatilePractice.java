package com.madm.learnroute.javaee.concurrent.juc;

public class VolatilePractice {

    private static final int THREADS_COUNT = 20;
    public static volatile int i = 0;

    public static void increase() {
        i++;
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[THREADS_COUNT];
        for (int j = 0; j < threads.length; j++) {
            threads[j] = new Thread(() -> {
                for (int k = 0; k < 1000; k++) {
                    increase();
                }
            });
            threads[j].start();
        }
        System.out.println("i == " + i);

    }
}