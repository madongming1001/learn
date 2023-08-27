package com.madm.learnroute.javaee.concurrent.juc;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * CLH变种（同步队列）
 *
 * @author dongming.ma
 * @date 2022/7/11 23:43
 */
@Slf4j
public class ReentrantLockPractice {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition putEmpty = lock.newCondition();
    private static Condition takeEmpty = lock.newCondition();

    private CountDownLatch countdown = new CountDownLatch(1);


    public static void main(String[] args) throws InterruptedException {
//        debugReentrantLock();
        debugCondition();
    }

    private static void debugReentrantLock() {
        ReentrantLockPractice rt = new ReentrantLockPractice();
        Thread t1 = new Thread(() -> {
            rt.await();
        });
        Thread t2 = new Thread(() -> {
            rt.await();
        });
        Thread t3 = new Thread(() -> {
            rt.await();
        });
        t1.setName("t1");
        t2.setName("t2");
        t3.setName("t3");
        t1.start();
        t2.start();
        t3.start();
    }

    public void await() {
        try {
            lock.lock();
            countdown.countDown();
            if (countdown.getCount() == 0) {
//                TimeUnit.DAYS.sleep(1L);
            }
            System.out.println("await时间为：" + System.currentTimeMillis());
            System.out.println("await等待结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        try {
            lock.lock();
            System.out.println("signal时间为：" + System.currentTimeMillis());
        } finally {
            lock.unlock();
        }
    }

    public static void debugCondition() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        //创建新的条件变量
//        Condition condition = lock.newCondition();
        Thread thread0 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程0获取锁");
                // sleep不会释放锁
                Thread.sleep(50000000L);
                //进入休息室等待
                System.out.println("线程0释放锁，进入等待");
//                condition.await();
                System.out.println("线程0被唤醒了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        thread0.setName("thread-01");
        thread0.start();
        log.info("thread0 未设置中断之前的中断状态为: {}",thread0.isInterrupted());
        thread0.interrupt();
        log.info("thread0 未设置中断之后的中断状态为: {}",thread0.isInterrupted());
        //叫醒
        Thread thread1 = new Thread(() -> {
            try {
                TimeUnit.DAYS.sleep(2L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            lock.lock();
            try {
                System.out.println("线程1获取锁");
                //唤醒
//                condition.signal();
                System.out.println("线程1唤醒线程0");
            } finally {
                lock.unlock();
                System.out.println("线程1释放锁");
            }
        });
        thread1.setName("thread-02");
        thread1.start();

        thread0.join();
        thread1.join();
    }
}
