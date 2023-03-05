package com.madm.learnroute.javaee.concurrent.juc;

import com.google.common.collect.Lists;
import com.madm.learnroute.javaee.ForkJoinPoolPractice;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Time;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CLH变种（同步队列）
 *
 * @author dongming.ma
 * @date 2022/7/11 23:43
 */
@Slf4j
public class ReentrantLockPractice {
    private ReentrantLock lock = new ReentrantLock();
    private CountDownLatch countdown = new CountDownLatch(1);


    public static void main(String[] args) {
        ReentrantLockPractice rt = new ReentrantLockPractice();
//        CompletableFuture.runAsync(ReentrantLockPractice::await);
//        CompletableFuture.runAsync(ReentrantLockPractice::signal);
        Thread t1 = new Thread(() -> {
            rt.await();
        });
        Thread t2 = new Thread(() -> {
            rt.await();
        });
        t1.setName("t1");
        t2.setName("t2");
        t1.start();
        t2.start();
    }

    public void await() {
        try {
            lock.lock();
            countdown.countDown();
            if (countdown.getCount() == 0) {
                TimeUnit.DAYS.sleep(1L);
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
}
