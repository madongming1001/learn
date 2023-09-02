package com.madm.learnroute.javaee;

import lombok.SneakyThrows;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 子线程在没有set值的情况下的，可以直接使用到父线程在 InheritableThreadLocal 设置的值，这里是实现了父线程向子线程共享数据。
 * 子线程重新设置了一个新的值，这个值的作为范围只在子线程上，不会影响到父线程的数据。
 *
 * 因为父线程InheritableThreadLocal添加值的时候会初始化父线程的inheritableThreadLocal
 * 在父线程执行的时候创建一个子线程默认创建的子线程的父线程都是外侧的父线程，子线程创建的时候就会判断如果父线程的 InheritableThreadLocal 有值
 * 就把父线程的 InheritableThreadLocal 赋值给子线程的 InheritableThreadLocal
 * 这样子线程在里面get的时候默认就会有 父线程 InheritableThreadLocal 的值
 * 但是在子线程里面添加值的时候添加的是子线程的 InheritableThreadLocal 所以在父线程在获取值的时候不会被修改 数据还是创建子线程时候的数据
 */
class LocalVariable {
    private Long[] a = new Long[1024 * 1024];
}

public class InheritableThreadLocalPractice {
    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    @SneakyThrows
    public static void main(String[] args) {
        Thread.currentThread().setName("mainThread");
        threadLocal.set("hello world");
        //在set值的时候createMap把 Thread 的 inheritableThreadLocals 设置有值了
        inheritableThreadLocal.set("weyeyeyewryr");
        Thread inheritableThread = new Thread(() -> {
            System.out.println("线程内部主线程：" + threadLocal.get());
            System.out.println("子线程：" + inheritableThreadLocal.get());
        });
        inheritableThread.start();
        inheritableThread.join();
        System.out.println("主线程：" + threadLocal.get());
        threadLocal.remove();
        inheritableThreadLocal.remove();

        memoryLeak();
    }

    @SneakyThrows
    private static void memoryLeak() {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
        ThreadLocal<LocalVariable> localVariable = new ThreadLocal<>();

        Thread.sleep(5000 * 4);
        for (int i = 0; i < 50; ++i) {
            poolExecutor.execute(() -> {
                localVariable.set(new LocalVariable());//这个时候才会把每个加入到线程的threadlocalmap中
                System.out.println("use local varaible" + localVariable.get());
                localVariable.remove();
            });
        }
        System.out.println("pool execute over");

    }
}
