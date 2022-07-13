package com.madm.learnroute.concurrency.juc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class ConditionUseCase {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void conditionWait() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("沉睡前");
            condition.await();
            System.out.println("唤醒后");
        } finally {
            lock.unlock();
        }
    }

    public void conditionSignal() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionUseCase c = new ConditionUseCase();
        try {
            c.conditionSignal();
            c.conditionWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
