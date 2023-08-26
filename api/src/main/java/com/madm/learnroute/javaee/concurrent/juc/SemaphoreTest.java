package com.madm.learnroute.javaee.concurrent.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量(Semaphore)，又被称为信号灯，在多线程环境下用于协调各个线程, 以保证它们能够正确、合理的使用公共资源。
 * 信号量维护了一个许可集，我们在初始化Semaphore时需要为这个许可集传入一个数量值，该数量值代表同一时间能访问共享资源的线程数量。
 * <p>
 * 线程可以通过acquire()方法获取到一个许可，然后对共享资源进行操作。注意如果许可集已分配完了，
 * 那么线程将进入等待状态，直到其他线程释放许可才有机会再获取许可，线程释放一个许可通过release()方法完成，"许可"将被归还给Semaphore。
 */
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
