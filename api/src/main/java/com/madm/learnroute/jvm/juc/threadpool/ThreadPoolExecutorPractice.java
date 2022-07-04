package com.madm.learnroute.jvm.juc.threadpool;

import lombok.SneakyThrows;

import java.util.concurrent.*;

public class ThreadPoolExecutorPractice {
    public static void main(String[] args) {
        ExecutorService ws = Executors.newWorkStealingPool();
        // 线程池的对应状态：
        // RUNNING
        // SHUTDOWN
        // STOP
        // TIDYING 整理
        // TERMINATED
        // private static final int CAPACITY   = (1 << COUNT_BITS) - 1;
        // private static int runStateOf(int c)     { return c & ~CAPACITY; }
        // private static int workerCountOf(int c)  { return c & CAPACITY; }
        // private static int ctlOf(int rs, int wc) { return rs | wc; }
        // DiscardPolicy
        // CallerRunPolicy
        // AbortPolicy
        // DiscardOldestPolicy


        //线程池线程预热，线程池的预热仅仅针对核心线程 ⚠️
        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor)Executors.newFixedThreadPool(10);
        //启动所有的核心线程
        fixedThreadPool.prestartAllCoreThreads();
        //仅启动一个核心线程
        fixedThreadPool.prestartCoreThread();
        // allowCoreThreadTimeOut，线程池中 corePoolSize 线程空闲时间达到keepAliveTime也将关闭
        // shutdownNow


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
            @SneakyThrows
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                throw new IllegalAccessException();
            }
        });

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第" + finalI + "个任务被执行！");
                }
            });
        }

//        ExecutorService executorService1 = Executors.newCachedThreadPool();
//        ExecutorService executorService2 = Executors.newFixedThreadPool(10);
//        ScheduledExecutorService executorService3 = Executors.newScheduledThreadPool(10);
//        ExecutorService executorService4 = Executors.newSingleThreadExecutor();
    }
}
