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
//        Thread.sleep(5000);
        // 首先我们创建一个list，来存放锁对象
        List<BiasedLockingPractice> list = new LinkedList<>();

        // 线程1
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                BiasedLockingPractice testBiasedLocking = new BiasedLockingPractice();
                list.add(testBiasedLocking); // 新建锁对象
                synchronized (testBiasedLocking) {
                    System.out.println("第" + (i + 1) + "次加锁-线程1"); // 50个妹子第一次结婚
                    System.out.println(ClassLayout.parseInstance(testBiasedLocking).toPrintable()); // 打印对象头信息
                }
            }
        }, "线程1").start();

        // 让线程1跑一会儿
        Thread.sleep(2000);

        // 线程2
        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                BiasedLockingPractice testBiasedLocking = list.get(i);
                synchronized (testBiasedLocking) {
                    if (i + 1 == 19 || i + 1 == 20) {
                        System.out.println("第" + (i + 1) + "次加锁-线程2"); // 前30个妹子依次被30个老王看上，前边20个直接离婚了，后边zf有新规定了，20-30的妹子直接把自己的老公都换成了对应的老王
                        System.out.println(ClassLayout.parseInstance(testBiasedLocking).toPrintable()); // 打印对象头信息
                    }
                }
            }
        }, "线程2").start();

        LockSupport.park();
    }
}
