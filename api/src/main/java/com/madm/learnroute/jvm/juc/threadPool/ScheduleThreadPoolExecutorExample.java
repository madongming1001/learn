package com.madm.learnroute.jvm.juc.threadPool;

import org.apache.commons.lang3.RandomUtils;

import java.util.Date;

public class ScheduleThreadPoolExecutorExample {
    public static void main(String[] args) {
//        ScheduledThreadPoolExecutor schedule = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(5);
//        Task task = new Task("任务");
//        schedule.schedule(task,2, TimeUnit.SECONDS);
//        schedule.scheduleWithFixedDelay(task,0,2,TimeUnit.SECONDS);//任务+延迟
//        schedule.scheduleAtFixedRate(task,0,2,TimeUnit.SECONDS);//任务延迟取最大值，稳定定时器

        long randomLong = randomLong = RandomUtils.nextLong(100000000000000000L,999999999999999999L);
        long ri = 0;
        while ((randomLong + "").length() == 18) {
            if (randomLong == 0) {
                continue;
            }
            ri++;
        }
        System.out.println(randomLong);
        System.out.println(ri);
        System.out.println(RandomUtils.nextLong(0,999999999999999999L));
        System.out.println(RandomUtils.nextLong(100000000000000000L,999999999999999999L));
    }


}

class Task implements Runnable {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    // 一定要做try catch处理 要不然会导致定时任务停掉 没有做异常处理
    @Override
    public void run() {
        System.out.println("Executing：" + name + "，Current Seconds：" + new Date().getSeconds());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
