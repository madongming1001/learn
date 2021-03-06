package com.madm.learnroute.concurrency.juc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dongming.ma
 * @date 2022/7/11 23:43
 */
@Slf4j
public class ReentrantLockLockSupportProcess {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        fixedThreadPool.execute(() -> compute());
        fixedThreadPool.execute(() -> compute());
        boolean allThreadsIsDone = true;
        while (true && allThreadsIsDone) {
            if (fixedThreadPool.getTaskCount() == fixedThreadPool.getCompletedTaskCount()) {
                fixedThreadPool.shutdownNow();
                allThreadsIsDone = false;
                log.debug("线程池关闭");
            }
        }

    }

    @SneakyThrows
    public static void compute() {
        lock.lock();
        try {
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                break;
            }
            System.out.println(Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }
}
