package com.madm.learnroute.jvm.os;

/**
 * 在早期的JVM中，synchronized（甚至是无竞争的synchronized）存在巨大的性能开销。因此，人们想出了一个聪明的"技巧"
 * ：双重检查锁定（Double-Checked Locking）。人们想通过双重检查锁定来降低同步的开销。下面是使用双重检查锁定来延迟初始化的实例代码。
 */

public class SafeDoubleCheckedLocking {
    private static SafeDoubleCheckedLocking instance;
    private SafeDoubleCheckedLocking(){};
    public static SafeDoubleCheckedLocking getInstance(){
        //第一次检测
        if(instance == null){
            //同步
            synchronized (SafeDoubleCheckedLocking.class){
                //多线程下可能会出现问题的地方
                if (instance == null){
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
