package com.madm.learnroute.javaee.concurrent.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CLH变种（同步队列）
 * @author dongming.ma
 * @date 2022/7/11 23:43
 */
@Slf4j
public class ReentrantLockPractice {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        CompletableFuture.runAsync(ReentrantLockPractice::await);
        CompletableFuture.runAsync(ReentrantLockPractice::signal);
    }

    public static void await() {
        try {
            lock.lock();
            System.out.println("await时间为：" +  System.currentTimeMillis());
            condition.await();
            System.out.println("await等待结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void signal() {
        try {
            lock.lock();
            System.out.println("signal时间为：" + System.currentTimeMillis());
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
}
