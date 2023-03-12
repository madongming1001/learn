package com.madm.learnroute.javaee.concurrent.juc;

import cn.hutool.core.date.DateUtil;
import io.netty.handler.codec.DateFormatter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * @author dongming.ma
 * @date 2023/3/11 00:58
 */
@Slf4j
public class ScheduledThreadPoolExecutorTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建大小为5的线程池
        ScheduledThreadPoolExecutor scheduledThreadPool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);

        for (int i = 0; i < 3; i++) {
            Task worker = new Task("task-" + i);
            // 只执行一次
//          scheduledThreadPool.schedule(worker, 5, TimeUnit.SECONDS);
            // 周期性执行，每5秒执行一次
            scheduledThreadPool.scheduleAtFixedRate(worker, 0, 5, TimeUnit.SECONDS);
//            scheduledThreadPool.scheduleWithFixedDelay(worker, 0, 5, TimeUnit.SECONDS);
        }
        scheduledThreadPool.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        scheduledThreadPool.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);


        Thread.sleep(10000);

        System.out.println("Shutting down executor...");
        // 关闭线程池
        scheduledThreadPool.shutdown();
        boolean isDone;
        // 等待线程池终止
        do {
            isDone = scheduledThreadPool.awaitTermination(1, TimeUnit.DAYS);
            System.out.println("awaitTermination...");
        } while (!isDone);

        System.out.println("Finished all threads");
    }


}


class Task implements Runnable {

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("name = " + name + ", startTime = " + DateUtil.format(LocalDateTime.now(), NORM_DATETIME_PATTERN));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("name = " + name + ", endTime = " + DateUtil.format(LocalDateTime.now(), NORM_DATETIME_PATTERN));
    }

}
