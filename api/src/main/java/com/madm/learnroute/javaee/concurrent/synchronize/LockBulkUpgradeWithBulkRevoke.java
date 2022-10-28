package com.madm.learnroute.javaee.concurrency.synchronize;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * 批量重偏向与批量撤销 机制
 * <p>
 * -XX:BiasedLockingBulkRebiasThreshold = 20   // 默认偏向锁批量重偏向阈值
 * -XX:BiasedLockingBulkRevokeThreshold = 40   // 默认偏向锁批量撤销阈值 超过阈值之后new出来的对象没有偏向锁状态 如果对象加锁的话 直接是轻量级锁
 * -XX:+UseBiasedLocking // 使用偏向锁，jdk6之后默认开启
 * -XX:BiasedLockingStartupDelay = 0 // 延迟偏向时间, 默认不为0，意思为jvm启动多少ms以后开启偏向锁机制（此处设为0，不延迟）
 * -XX:BiasedLockingDecayTime=25000ms
 * <p>
 * 批量重偏向(bulk rebias)机制是为了解决:一个线程创建了大量对象并执行了初始的同步操作，后来另一个线程也来将这些对象作为锁对象进行操作，这样会导致大量的偏向锁撤销操作。
 * 批量撤销(bulk revoke)机制是为了解决:在明显多线程竞争剧烈的场景下使用偏向锁是不合适的。
 * <p>
 * 所以批量撤销会把正在执行同步的对象的锁状态由偏向锁变为轻量级锁，而不在执行同步的对象的锁状态不会改变（如对象66）。
 *
 * @author dongming.ma
 * @date 2022/7/11 16:47
 */
@Slf4j
public class LockBulkUpgradeWithBulkRevoke {

    public static void main(String[] args) throws InterruptedException {
        //为了防止批量的锁撤销升级为轻量级锁 20次，但是实际测试到了第20次就会成为批量偏向
//        bulkRebias();
//        bulkRevoke();
        bulkRevoke2();
    }

    private static void bulkRebias() throws InterruptedException {
        // 首先我们创建一个list，来存放锁对象
        List<LockBulkUpgradeWithBulkRevoke> list = new LinkedList<>();

        // 线程1
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                LockBulkUpgradeWithBulkRevoke LockBulkUpgradeWithBulkRevoke = new LockBulkUpgradeWithBulkRevoke();
                list.add(LockBulkUpgradeWithBulkRevoke); // 新建锁对象
                synchronized (LockBulkUpgradeWithBulkRevoke) {
                    System.out.println("第" + (i + 1) + "次加锁-线程1"); // 50个妹子第一次结婚
                    System.out.println(ClassLayout.parseInstance(LockBulkUpgradeWithBulkRevoke).toPrintable()); // 打印对象头信息
                }
            }
        }, "线程1");
        thread1.start();

        // 让线程1跑一会儿
        Thread.sleep(2000);

        // 线程2
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                LockBulkUpgradeWithBulkRevoke LockBulkUpgradeWithBulkRevoke = list.get(i);
                synchronized (LockBulkUpgradeWithBulkRevoke) {
                    System.out.println("第" + (i + 1) + "次加锁-线程2"); // 前30个妹子依次被30个老王看上，前边20个直接离婚了，后边zf有新规定了，20-30的妹子直接把自己的老公都换成了对应的老王
                    System.out.println(ClassLayout.parseInstance(LockBulkUpgradeWithBulkRevoke).toPrintable()); // 打印对象头信息
                }
            }
        }, "线程2");
        thread2.start();
        thread1.join();
        thread2.join();
    }


    private static void bulkRevoke2() throws InterruptedException {
        // 首先我们创建一个list，来存放锁对象
        List<Object> list = new LinkedList<>();

        // 线程1
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                Object obj = new Object();
                list.add(obj); // 新建锁对象
                synchronized (obj) {
                    System.out.println("第" + (i + 1) + "次加锁-线程1"); // 50个妹子第一次结婚
                    System.out.println(ClassLayout.parseInstance(obj).toPrintable());
                }

            }
            LockSupport.park();
        }, "线程1").start();

        // 让线程1跑一会儿
        Thread.sleep(2000);

        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                Object obj = list.get(i);
                synchronized (obj) {
                    System.out.println("第" + (i + 1) + "次加锁-线程2"); // 前40个妹子依次被40个老王看上，前边20个直接离婚了，后边zf有新规定了，20-40的妹子直接把自己的老公都换成了对应的老王
                    System.out.println(ClassLayout.parseInstance(obj).toPrintable());

                }
            }
            LockSupport.park();
        }, "线程2").start();

        // 让线程2跑一会儿
        Thread.sleep(2000);

        // 线程3
        new Thread(() -> {
            for (int i = 19; i <= 41; i++) {
                if (i == 19) {
                    log.info("thread3 第{}次运行：" + ClassLayout.parseInstance(new Object()).toPrintable(), i);
                } else if (i == 40) {
                    log.info("thread3 第{}次运行：" + ClassLayout.parseInstance(new Object()).toPrintable(), i);
                } else if (i == 41) {
                    Object obj41 = new Object();
                    synchronized (obj41) {
                        log.info("thread3 第{}次运行：" + ClassLayout.parseInstance(obj41).toPrintable(), i);
                    }
                    break;
                }
                Object obj = list.get(i);
                synchronized (obj) {
                    System.out.println("第" + (i + 1) + "次加锁-线程3"); // 20-40的妹子觉得很刺激，换过老王了，还想接着换王中王？玩砸了，这个地区的女的以后都不准结婚了，结婚的也都给我离婚
                    System.out.println(ClassLayout.parseInstance(obj).toPrintable());
                }
            }
            LockSupport.park();
        }, "线程3").start();

        // 让线程3跑一会儿
        Thread.sleep(2000);
        System.out.println("刚出生的妹子"); // 生不逢时，政策不一样了，该地区的妹子以后都不让结婚了
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
        LockSupport.park();
    }

    private static void bulkRevoke() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        //偏向延时
        Thread.sleep(5000);
        final ArrayList<Object> objArrayList = new ArrayList<>();
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                while (objArrayList.size() < 80) {
                    Object obj = new Object();
                    synchronized (obj) {
                        objArrayList.add(obj);
                    }
                }

                //保持线程不要退出
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 39; i++) {
                    if (i == 38) {
                        System.out.println((i + 1) + ":");
                        //这里主要是给主线程留出时间，让主线程打印出一个new Dog
                        countDownLatch.countDown();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    Object obj = objArrayList.get(i);
                    synchronized (obj) {
                        objArrayList.add(obj);
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //保持线程不要退出
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        new Thread(runnable1).start();
        //保证线程1生成完毕
        Thread.sleep(3000);

        //重新偏向线程2
        new Thread(runnable2).start();
        Thread.sleep(3000);

        //线程3导致偏向锁升级
        new Thread(runnable2).start();

        //线程3导致锁升级到39的时候，new出来的对象，默认是可偏向的状态
        countDownLatch.await();
        log.info("第39个时候锁状态是：" + ClassLayout.parseInstance(new Object()).toPrintable());

        //保证之后
        Thread.sleep(3000);
        //第39之后的新对象，已经没有偏向了，偏向被撤销了
        log.info("第40个时候锁状态是：" + ClassLayout.parseInstance(new Object()).toPrintable());
    }
}
