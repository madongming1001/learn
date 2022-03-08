package com.madm.learnroute.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreTest {
    private static final int THREAD_COUNT = 30;

    private static Semaphore semaphore = new Semaphore(10);
    private static ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    public static void main(String[] args) {

        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        semaphore.tryAcquire(20, TimeUnit.MILLISECONDS);
                        System.out.println("save data");
                        semaphore.release();
                    } catch (InterruptedException e) {
                    }
                }
            });
        }
        threadPool.shutdown();
    }
}
