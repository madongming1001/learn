package com.madm.learnroute.concurrency.juc.threadpool;

import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorPractice {

    private static final int COUNT_BITS = Integer.SIZE - 3; // 29 11101
    private static final int CAPACITY = (1 << COUNT_BITS) - 1; // 29个1 00011111111111111111111111111111
    private static final int RUNNING = -1 << COUNT_BITS;

    private static int index;

    private static int runStateOf(int c) {
        return c & ~CAPACITY;
    }

    private static int workerCountOf(int c) {
        return c & CAPACITY;
    }

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    public static void main(String[] args) {
        System.out.println(Long.toBinaryString(4294967296l));
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.toBinaryString(2147483647));

        AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));
        System.out.println(Integer.toBinaryString(ctl.get()));
        System.out.println(Integer.toBinaryString(CAPACITY));
        System.out.println(Integer.toBinaryString(~CAPACITY));
        System.out.println(Integer.toBinaryString((1 << 16) - 1));
        // 1111111111111111
        System.out.println(Integer.toBinaryString(0xFFFF));
        //1111111111111111
        System.out.println(Integer.toBinaryString(65535));
        System.out.println(Integer.parseInt("111111111111", 2));


        System.out.println(RUNNING);
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(-1 << 29);
        System.out.println(Integer.toBinaryString(-536870912));
//        System.out.println(CAPACITY);
//        executeSchedule();
        System.out.println(Integer.toBinaryString(-1 - 1));//11111111111111111111111111111110
        System.out.println(-1 - 1);
        System.out.println(Long.valueOf("10100000000000000000000000000000", 2));
        System.out.println(Integer.toBinaryString(~3) + 1);//按位取反
        System.out.println((~3) + 1);
    }

    @SneakyThrows
    private static void executeSchedule() {
        ExecutorService ws = Executors.newWorkStealingPool();
        // 线程池的对应状态：
        // RUNNING
        // SHUTDOWN
        // STOP
        // TIDYING 整理
        // TERMINATED

//         DiscardPolicy
//         CallerRunPolicy
        // AbortPolicy
        // DiscardOldestPolicy


        //线程池线程预热，线程池的预热仅仅针对核心线程 ⚠️
        ThreadPoolExecutor fixedThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
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

        List<Callable<Integer>> callables = Arrays.asList(() -> index++, () -> index++);

        threadPoolExecutor.invokeAny(callables);

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
