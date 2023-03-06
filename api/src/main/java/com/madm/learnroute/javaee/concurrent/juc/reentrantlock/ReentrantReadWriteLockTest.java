package com.madm.learnroute.javaee.concurrent.juc.reentrantlock;

import com.mdm.pojo.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
class CachedData {
    Object data;
    volatile boolean cacheValid;
    final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    @SneakyThrows
    void execReadLock() {
        readLock.lock();
        log.info("当前持有读锁线程数是：{}", readWriteLock.getReadHoldCount());
        TimeUnit.DAYS.sleep(2l);
        readLock.unlock();
    }

    void processCachedData() {
        readLock.lock();
        if (!cacheValid) {
            // Must release read lock before acquiring write lock
            readLock.unlock();
            writeLock.lock();//如果有读锁或者不是当前线程获得的
            try {
                // Recheck state because another thread might have
                // acquired write lock and changed state before we did.
                if (!cacheValid) {
                    data = new User(1, "user");
                    cacheValid = true;
                }
                // Downgrade by acquiring read lock before releasing write lock
                readLock.lock();
            } finally {
                writeLock.unlock(); // Unlock write, still hold read
            }
        }
        try {
            use(data);
        } finally {
            readLock.unlock();
        }
    }

    private void use(Object data) {
        System.out.println(data);
    }
}
