package com.madm.learnroute.concurrency.juc.threadpool;


import lombok.experimental.var;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Java结束线程的三种方法
 * 1.设置退出标志，使线程正常退出，也就是当run()方法完成后线程终止 通过volatile变量控制
 * 2.使用interrupt()方法中断线程
 * 3.使用stop方法强行终止线程（不推荐使用，Thread.stop, Thread.suspend, Thread.resume 和Runtime.runFinalizersOnExit 这些终止线程运行的方法已经被废弃，使用它们是极端不安全的！）
 */
public class ScheduleThreadPoolExecutorPractice {
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int RUNNING = -1 << COUNT_BITS;
    private final static AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    public static void main(String[] args) {

        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        LinkedList<Long> numOfMinute = new LinkedList<>();

        long start = System.currentTimeMillis();
        Thread calculation = new Thread(() -> {
            Thread cthread = Thread.currentThread();
            while (!cthread.isInterrupted()) {
                numOfMinute.add(System.currentTimeMillis());
            }
            System.out.println(numOfMinute.size());
        }, "计算线程");
        calculation.start();
        long end = System.currentTimeMillis();
        long totalTime = (end - start) / 1000;
        System.out.println(totalTime);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            ThreadInfo[] threadInfo = threadBean.dumpAllThreads(false, false);
            System.out.println(threadInfo.length + " os thread");
        }, 1, 1, TimeUnit.SECONDS);


        ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
        schedule.schedule(() -> calculation.interrupt(), 1, TimeUnit.SECONDS);
//        Task task = new Task("任务");
//        // one-shot
//        schedule.schedule(task, 2, TimeUnit.SECONDS);
//        schedule.scheduleWithFixedDelay(task, 0, 2, TimeUnit.SECONDS);//任务+延迟
//        schedule.scheduleAtFixedRate(task, 0, 2, TimeUnit.SECONDS);//任务延迟取最大值，稳定定时器
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
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
