package com.madm.learnroute.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * java.util.concurrent类库中提供Condition类来实现线程之间的协调。调用Condition.await() 方法使线程等待，
 * 其他线程调用Condition.signal() 或 Condition.signalAll() 方法唤醒等待的线程。
 */
@Slf4j
public class ConditionQueue {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition noCig = lock.newCondition();
    private static Condition noTakeOutFood = lock.newCondition();

    private static boolean hasCig = false;
    private static boolean hasTakeOutFood = false;

    //送烟
    public void cigratee() {
        lock.lock();
        try {
            while (!hasCig) {
                try {
                    log.debug("没有烟，歇一会");
                    noCig.await();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.debug("有烟了，干活");
        } finally {
            lock.unlock();
        }
    }

    //送外卖
    public void takeout() {
        lock.lock();
        try {
            while (!hasTakeOutFood) {
                try {
                    log.debug("没有饭，歇一会");
                    noTakeOutFood.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            log.debug("有饭了，干活");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionQueue test = new ConditionQueue();
        new Thread(() -> {
            test.cigratee();
        }).start();

        new Thread(() -> {
            test.takeout();
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                hasCig = true;
                //唤醒送烟的等待线程
                noCig.signal();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                hasTakeOutFood = true;
                //唤醒送饭的等待线程
                noTakeOutFood.signal();
            } finally {
                lock.unlock();
            }
        }, "t2").start();
    }
}
