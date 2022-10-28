package com.madm.learnroute.javaee.concurrency.synchronize;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 锁升级
 * 无   锁：001
 * 偏向 锁：101 （匿名偏向锁：是指对象刚创建，还没有偏向任何一方）
 * 轻量级锁：00
 * 重量级锁：10
 *
 * @author dongming.ma
 * @date 2022/7/11 12:37
 */
@Slf4j
public class LockUpgrade {
    public static void main(String[] args) throws InterruptedException {
        biasLockToLightweightLock();
//        lightweightLockToHeavyweightLock();
    }

    /**
     * 偏向锁到轻量级锁
     *
     * @throws InterruptedException
     */
    private static void biasLockToLightweightLock() throws InterruptedException {
        log.debug(ClassLayout.parseInstance(new Object()).toPrintable());
        //HotSpot 虚拟机在启动后有个 4s 的延迟才会对每个新建的对象开启偏向锁模式
        Thread.sleep(4000);
        Object obj = new Object();
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
        // 思考: 如果对象调用了hashCode,还会开启偏向锁模式吗
//        obj.hashCode();
        //log.debug(ClassLayout.parseInstance(obj).toPrintable());
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug(Thread.currentThread().getName() + "开始执行。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
                synchronized (obj) {
                    // 思考:偏向锁执行过程中，调用hashcode会发生什么?
                    //obj.hashCode();
                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
                }
                log.debug(Thread.currentThread().getName() + "释放锁。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "thread1");
        thread1.start();
        thread1.join();
        obj.hashCode();
        // 控制线程竞争时机
//        TimeUnit.SECONDS.sleep(1);
//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log.debug(Thread.currentThread().getName() + "开始执行。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
//                synchronized (obj) {
//                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
//                }
//                log.debug(Thread.currentThread().getName() + "释放锁。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
//            }
//        }, "thread2");
//        thread2.start();

        Thread.sleep(5000);
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
    }

    private static void lightweightLockToHeavyweightLock() throws InterruptedException {
        //HotSpot 虚拟机在启动后有个 4s 的延迟才会对每个新建的对象开启偏向锁模式
        log.debug(ClassLayout.parseInstance(new Object()).toPrintable());
        TimeUnit.SECONDS.sleep(5);
        Object obj = new Object();
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
        new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug(Thread.currentThread().getName() + "开始执行。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
                synchronized (obj) {
                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
                }
                log.debug(Thread.currentThread().getName() + "释放锁。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "thread1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                log.debug(Thread.currentThread().getName() + "开始执行。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
                synchronized (obj) {
                    log.debug(Thread.currentThread().getName() + "获取锁执行中。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
                }
                log.debug(Thread.currentThread().getName() + "释放锁。。。\n" + ClassLayout.parseInstance(obj).toPrintable());
            }
        }, "thread2").start();

        Thread.sleep(5000);
        log.debug(ClassLayout.parseInstance(obj).toPrintable());
    }
}
