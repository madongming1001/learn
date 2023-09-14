package com.madm.learnroute.javaee.concurrent.juc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * execute 方法执行时，会抛出(打印)堆栈异常。 抛出异常的线程会被销毁
 * submit 方法执行时，返回结果封装在future中，如果调用future.get()方法则必须进行异常捕获，从而可以抛出(打印)堆栈异常。 抛出异常的线程继续执行
 *
 * @author dongming.ma
 * @date 2022/10/28 12:58
 */
public class ThreadPoolExceptionPractice {
    public static void main(String[] args) {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        executorService.execute(() -> sayHi("execute"));
        executorService.submit(() -> sayBay("submit"));
        scheduledExecutorService.schedule(() -> sayBay("com/madm/learnroute/schedule"), 2, TimeUnit.SECONDS);
    }

    private static void sayHi(String name) {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        System.out.println(printStr);
        throw new RuntimeException(printStr + ",我异常啦!哈哈哈!");
    }

    private static void sayBay(String name) {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        System.out.println(printStr);
        int i = 1 / 0;
//        try {
//        } catch (Throwable e) {
//            throw new DataCenterRuntimeException(e);
//        }
    }

    private static void sayYou(String name) {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        System.out.println(printStr);
        int i = 1 / 0;
    }


}
