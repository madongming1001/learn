package com.madm.learnroute.jvm.juc.threadpool;

import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleThreadPoolExecutorPractice {
    public static void main(String[] args) {
        LinkedList<Long> numOfMinute = new LinkedList<>();
        Thread calculation = new Thread(() -> {
            Thread cthread = Thread.currentThread();
            while (!cthread.isInterrupted()) {
                numOfMinute.add(System.currentTimeMillis());
            }
            System.out.println(numOfMinute.size());
        }, "计算线程");
        calculation.start();
        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
        schedule.schedule(() -> {
            calculation.interrupt();
        }, 1, TimeUnit.SECONDS);

//        Task task = new Task("任务");
//        // one-shot
//        schedule.schedule(task, 2, TimeUnit.SECONDS);
//        schedule.scheduleWithFixedDelay(task, 0, 2, TimeUnit.SECONDS);//任务+延迟
//        schedule.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);//任务延迟取最大值，稳定定时器
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
        System.out.println("Executing：" + name + "，Current Seconds：" + LocalTime.now().getSecond());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
