package com.madm.learnroute.ThreadPool;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduleThreadPoolExecutorExample {
    public static void main(String[] args) {
//        ScheduledThreadPoolExecutor schedule = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(5);
//        Task task = new Task("任务");
//        schedule.schedule(task,2, TimeUnit.SECONDS);
//        schedule.scheduleWithFixedDelay(task,0,2,TimeUnit.SECONDS);//任务+延迟
//        schedule.scheduleAtFixedRate(task,0,2,TimeUnit.SECONDS);//任务延迟取最大值，稳定定时器
        System.out.println(Math.random() * 10 + 1);
    }


}

class Task implements Runnable{
    private String name;

    public Task(String name) {
        this.name = name;
    }

    // 一定要做try catch处理 要不然会导致定时任务停掉 没有做异常处理
    @Override
    public void run() {
        System.out.println("Executing："+name+"，Current Seconds："+ new Date().getSeconds());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
