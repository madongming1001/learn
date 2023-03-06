package com.madm.learnroute.javaee.concurrent.juc.reentrantlock;

import com.mdm.pojo.User;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantReadWriteLock 锁降级
 *
 * @author dongming.ma
 * @date 2022/7/12 19:52
 */
public class ReentrantReadWriteLockTest {
    public static void main(String[] args) {
        CachedData cachedData = new CachedData();
        Thread t1 = new Thread(() -> {
            cachedData.execReadLock();
        });
        Thread t2 = new Thread(() -> {
            cachedData.execReadLock();
        });
        Thread t3 = new Thread(() -> {
            cachedData.execReadLock();
        });
        t1.setName("线程-1");
        t2.setName("线程-2");
        t3.setName("线程-3");
        t1.start();
        t2.start();
        t3.start();
    }
}

class CachedData {
    Object data;
    volatile boolean cacheValid;
    final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @SneakyThrows
    void execReadLock() {
        readWriteLock.readLock().lock();
        TimeUnit.DAYS.sleep(2l);
        readWriteLock.readLock().unlock();
    }

    void processCachedData() {
        readWriteLock.readLock().lock();
        if (!cacheValid) {
            // Must release read lock before acquiring write lock
            readWriteLock.readLock().unlock();
            readWriteLock.writeLock().lock();//如果有读锁或者不是当前线程获得的
            try {
                // Recheck state because another thread might have
                // acquired write lock and changed state before we did.
                if (!cacheValid) {
                    data = new User(1, "user");
                    cacheValid = true;
                }
                // Downgrade by acquiring read lock before releasing write lock
                readWriteLock.readLock().lock();
            } finally {
                readWriteLock.writeLock().unlock(); // Unlock write, still hold read
            }
        }
        try {
            use(data);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private void use(Object data) {
        System.out.println(data);
    }
}
