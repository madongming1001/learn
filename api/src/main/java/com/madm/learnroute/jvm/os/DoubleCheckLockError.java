package com.madm.learnroute.jvm.os;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Double Check Lock的单例为什么要用Volatile?
 *
 * @Author: madongming
 * @DATE: 2022/6/22 17:11
 */
public class DoubleCheckLockError {
    public static AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) throws Exception {
        for (; ; ) {
            CountDownLatch latch = new CountDownLatch(1);
            CountDownLatch end = new CountDownLatch(100);
            for (int i = 0; i < 100; i++) {
                Thread t1 = new Thread(() -> {
                    try {
                        int n = num.incrementAndGet();
                        // 创建100个线程,同时在这里等着
                        latch.await();
                        // 当100个线程创建完之后，同时放开，同时创建对象
                        SingleObj singleObj = SingleObj.getInstance();
                        // 当singleObj不为null，但是singleObj.i为0的时候，说明实例化的对象已经赋值给引用对象，但是构造方法还没有执行
                        if (singleObj.index == 0) {
                            System.err.println("对象第" + n + "次时未初始化完成,i==0");
                            System.exit(0);
                        }
                        // 当线程等到到100个的时候，end.await();会放行，同时会调用reset()重置单例
                        end.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                t1.start();
            }
            // 100个线程创建完之后，调用countDown() 放行latch.await();
            latch.countDown();
            // 等待100线程的创建
            end.await();
            // 重置
            SingleObj.reset();
        }
    }

    static class SingleObj {
        private int index;
        private static SingleObj single;

        private SingleObj() {
//            index = 1;
            index = 1 + new Random().nextInt(100);
        }

        public static SingleObj getInstance() {
            if (null == single) {
                synchronized (SingleObj.class) {
                    if (null == single) {
                        //1、创建对象分配内存空间
                        //2、设置值
                        //3、把刚才创建的对象赋值给变量
                        //会发生指令重排
                        single = new SingleObj();
                    }
                }
            }
            return single;
        }

        public static void reset() {
            single = null;
        }
    }

}
