package com.madm.learnroute.concurrency.juc;

public class VolatilePractice {

    public static volatile int i = 0;
    private static final int THREADS_COUNT = 20;

    public static void increase() {
        i++;
    }

    public static void main(String[] args) {
//        Thread[] threads = new Thread[THREADS_COUNT];
//        for (int j = 0; j < threads.length; j++) {
//            threads[j] = new Thread(() -> {
//                for (int k = 0; k < 10000; k++) {
//                    increase();
//                }
//            });
//            threads[j].start();
//        }
    }
}