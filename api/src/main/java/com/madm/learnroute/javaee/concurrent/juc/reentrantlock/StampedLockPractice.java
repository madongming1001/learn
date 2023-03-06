package com.madm.learnroute.javaee.concurrent.juc.reentrantlock;

import java.util.concurrent.locks.StampedLock;

/**
 * 相比较于ReentrantReadWriteLock而言，改进之处在于：读的过程中也允许获取写锁后写入，如此，读取的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观读锁。
 * StampedLock支持三种模式的锁：
 * 1、写锁writeLock，当一个线程获取该锁后，其它请求的线程必须等待；
 *
 * 2、悲观读锁readLock，与ReentrantReadWriteLock功能类型，在没有线程获取独占写锁的情况下，同时多个线程可以获取该锁，如果已经有线程持有写锁，其他线程请求获取该读锁会被阻塞。
 *
 * 3、乐观读锁tryOptimisticRead，是相对于悲观锁来说的，如果当前没有线程持有写锁，则简单的返回一个非0的stamp版本信息，获取该stamp后在具体操作数据前还需要调用validate验证下该stamp是否已经不可用，
 *    也就是看当调用tryOptimisticRead返回stamp后到到当前时间间是否有其他线程持有了写锁，如果是那么validate会返回0，否者就可以使用该stamp版本的锁对数据进行操作。
 */
public class StampedLockPractice {
    private double x, y;

    private final StampedLock stampedLock = new StampedLock();

    void move(double deltaX, double deltaY) { // an exclusively locked method
        // 获取写锁
        long stamp = stampedLock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin() { // A read-only method
        // 获取乐观读锁
        long stamp = stampedLock.tryOptimisticRead();
        double currentX = x, currentY = y;
        // 检查乐观读锁后是否有其他写锁发生
        if (!stampedLock.validate(stamp)) {
            // 乐观锁加成失败，重新获取读锁
            stamp = stampedLock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    void moveIfAtOrigin(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        // 获取读锁
        long stamp = stampedLock.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                // 尝试升级为写锁
                long ws = stampedLock.tryConvertToWriteLock(stamp);
                // 如果失败，返回0
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    stampedLock.unlockRead(stamp);
                    stamp = stampedLock.writeLock();
                }
            }
        } finally {
            stampedLock.unlock(stamp);
        }
    }
}
