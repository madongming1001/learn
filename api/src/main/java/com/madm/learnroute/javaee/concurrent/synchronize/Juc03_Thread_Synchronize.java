package com.madm.learnroute.javaee.concurrency.synchronize;

import org.openjdk.jol.info.ClassLayout;

/**
 *                  ,;,,;
 *                ,;;'(    社
 *      __      ,;;' ' \   会
 *   /'  '\'~~'~' \ /'\.)  主
 * ,;(      )    /  |.     义
 *,;' \    /-.,,(   ) \    码
 *     ) /       ) / )|    农
 *     ||        ||  \)     
 *     (_\       (_\
 * @author ：杨过
 * @date ：Created in 2020/4/28 17:15
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description:
 * -XX:BiasedLockingStartupDelay=0 关闭偏向锁延迟
 * -XX:-UseCompressedOops 对象头指针压缩
 * -XX:-UseCompressedClassPointers klass pointer指针压缩
 **/
public class Juc03_Thread_Synchronize {

    public static void rl() throws Exception {
        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
        //HotSpot 虚拟机在启动后有4s的延迟才会对每个新建的对象开启偏向锁模式
        Thread.sleep(5000);
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        String tname = Thread.currentThread().getName();
        synchronized (obj) {
            System.out.println(String.format("{}:) hold {}->monitor lock", tname, obj));
            synchronized (obj) {
                System.out.println(String.format("{}:) re-hold {}->monitor lock", tname, obj));
                System.out.println(ClassLayout.parseInstance(obj).toPrintable());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Juc03_Thread_Synchronize.rl();
    }
}
