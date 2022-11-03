package com.madm.learnroute.jvm.jmm;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * //关闭延迟开启偏向锁
 * -XX:BiasedLockingStartupDelay=0
 * //禁止偏向锁
 * -XX:-UseBiasedLocking
 * //启用偏向锁
 * -XX:+UseBiasedLocking
 * //默认偏向锁批量重偏向阈值
 * -XX:BiasedLockingBulkRebiasThreshold=20
 * //默认偏向锁批量撤销阈值
 * -XX:BiasedLockingBulkRevokeThreshold=40
 * //使用偏向锁，jdk6之后默认开启
 * -XX:+UseBiasedLocking
 * //延迟偏向时间, 默认不为0，意思为jvm启动多少ms以后开启偏向锁机制（此处设为0，不延迟）
 * -XX:BiasedLockingStartupDelay=0
 *
 * @author dongming.ma
 * @date 2022/11/3 19:48
 */
@Slf4j
public class BiasedLockingPractice {
    @SneakyThrows
    public static void main(String[] args) {
        //bulkRebias();//批量重偏向
        bulkRevoke();//批量撤销
    }

    private static void bulkRevoke() throws InterruptedException {
        // 首先我们创建一个list，来存放锁对象
        List<BiasedLockingPractice> list = new LinkedList<>();

        // 线程1
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                BiasedLockingPractice BiasedLockingPractice = new BiasedLockingPractice();
                list.add(BiasedLockingPractice); // 新建锁对象
                synchronized (BiasedLockingPractice) {
                    System.out.println("第" + (i + 1) + "次加锁-线程1"); // 50个妹子第一次结婚
                    System.out.println(ClassLayout.parseInstance(BiasedLockingPractice).toPrintable());
                }

            }
            LockSupport.park();
        }, "线程1").start();

        // 让线程1跑一会儿
        Thread.sleep(2000);

        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                BiasedLockingPractice BiasedLockingPractice = list.get(i);
                synchronized (BiasedLockingPractice) {
                    System.out.println("第" + (i + 1) + "次加锁-线程2"); // 前40个妹子依次被40个老王看上，前边20个直接离婚了，后边zf有新规定了，20-40的妹子直接把自己的老公都换成了对应的老王
                    System.out.println(ClassLayout.parseInstance(BiasedLockingPractice).toPrintable());

                }
            }
            LockSupport.park();
        }, "线程2").start();

        // 让线程2跑一会儿
        Thread.sleep(2000);

        // 线程3
        new Thread(() -> {
            for (int i = 20; i < 40; i++) {
                BiasedLockingPractice BiasedLockingPractice = list.get(i);
                synchronized (BiasedLockingPractice) {
                    System.out.println("第" + (i + 1) + "次加锁-线程3"); // 20-40的妹子觉得很刺激，换过老王了，还想接着换王中王？玩砸了，这个地区的女的以后都不准结婚了，结婚的也都给我离婚
                    System.out.println(ClassLayout.parseInstance(BiasedLockingPractice).toPrintable());
                }
            }
            LockSupport.park();
        }, "线程3").start();

        // 让线程3跑一会儿
        Thread.sleep(2000);
        System.out.println("刚出生的妹子"); // 生不逢时，政策不一样了，该地区的妹子以后都不让结婚了
        System.out.println(ClassLayout.parseInstance(new BiasedLockingPractice()).toPrintable());
        LockSupport.park();
    }

    private static void bulkRebias() throws InterruptedException {
        // 首先我们创建一个list，来存放锁对象
        List<BiasedLockingPractice> list = new LinkedList<>();

        // 线程1
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                BiasedLockingPractice BiasedLockingPractice = new BiasedLockingPractice();
                list.add(BiasedLockingPractice); // 新建锁对象
                synchronized (BiasedLockingPractice) {
                    if (i + 1 == 50) {
                        System.out.println("第" + (i + 1) + "次加锁-线程1"); // 50个妹子第一次结婚
                        System.out.println(ClassLayout.parseInstance(BiasedLockingPractice).toPrintable()); // 打印对象头信息
                    }
                }
            }
        }, "线程1").start();

        // 让线程1跑一会儿
        Thread.sleep(2000);

        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                BiasedLockingPractice BiasedLockingPractice = list.get(i);
                synchronized (BiasedLockingPractice) {
                    if (i + 1 == 19 || i + 1 == 20) {
                        System.out.println("第" + (i + 1) + "次加锁-线程2"); // 前30个妹子依次被30个老王看上，前边20个直接离婚了，后边zf有新规定了，20-30的妹子直接把自己的老公都换成了对应的老王
                        System.out.println(ClassLayout.parseInstance(BiasedLockingPractice).toPrintable()); // 打印对象头信息
                    }
                }
            }
        }, "线程2").start();

        LockSupport.park();
    }
}
