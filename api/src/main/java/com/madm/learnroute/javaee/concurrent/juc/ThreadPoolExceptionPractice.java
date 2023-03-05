package com.madm.learnroute.javaee.concurrent.juc;

import com.mdm.exception.DataCenterRuntimeException;
import jodd.exception.ExceptionUtil;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * execute方法执行时，会抛出(打印)堆栈异常。
 * submit方法执行时，返回结果封装在future中，如果调用future.get()方法则必须进行异常捕获，从而可以抛出(打印)堆栈异常。
 *
 * @author dongming.ma
 * @date 2022/10/28 12:58
 */
public class ThreadPoolExceptionPractice {
    public static void main(String[] args) {
        ThreadPoolTaskExecutor executorService = buildThreadPoolTaskExecutor();
        executorService.execute(() -> sayHi("execute"));
        executorService.submit(() -> {
            try {
                sayBay("s");
            } catch (Exception e) {
                System.out.printf(ExceptionUtil.message(e));
            }
        });
    }

    private static void sayHi(String name) {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        System.out.println(printStr);
        throw new RuntimeException(printStr + ",我异常啦!哈哈哈!");
    }

    private static void sayBay(String name) {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        System.out.println(printStr);
        try {
            int i = 1 / 0;
        } catch (Throwable e) {
            throw new DataCenterRuntimeException(e);
        }
    }

    private static ThreadPoolTaskExecutor buildThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executorService = new ThreadPoolTaskExecutor();
        executorService.setThreadNamePrefix("personal");
        executorService.setCorePoolSize(5);
        executorService.setMaxPoolSize(10);
        executorService.setQueueCapacity(1000);
        executorService.setKeepAliveSeconds(30);
        executorService.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.initialize();
        return executorService;
    }
}
