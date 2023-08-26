package com.madm.learnroute.jvm.os;

/**
 * 在早期的JVM中，synchronized（甚至是无竞争的synchronized）存在巨大的性能开销。因此，人们想出了一个聪明的"技巧"
 * ：双重检查锁定（Double-Checked Locking）。人们想通过双重检查锁定来降低同步的开销。下面是使用双重检查锁定来延迟初始化的实例代码。
 */

public class SafeDoubleCheckedLocking {
    private static SafeDoubleCheckedLocking instance;

    private SafeDoubleCheckedLocking() {
    }

    ;

    public static SafeDoubleCheckedLocking getInstance() {
        //第一次检测,防止线程串行执行影响效率
        if (instance == null) {
            //同步
            synchronized (SafeDoubleCheckedLocking.class) {
                //多线程下可能会出现问题的地方
                //第二次检查，防止上一个线程已经创建好了对象，当前线程获取到锁继续执行的时候就会出现覆盖对象创建的操作
                if (instance == null) {
                    //1、分配内存空间并将对象的字段赋默认值
                    //2、调用init进行初始化（对象的字段赋初始值）
                    //3、对象的内存地址赋值给局部变量
                    instance = new SafeDoubleCheckedLocking();
                }
            }
        }
        return instance;
    }


}
