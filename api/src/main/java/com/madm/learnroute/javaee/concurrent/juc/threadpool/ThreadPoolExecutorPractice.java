package com.madm.learnroute.javaee.concurrent.juc.threadpool;

import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    @SneakyThrows
    public static void main(String[] args) {
//        ExecutorService executorService = Executors.newFixedThreadPool(1);
//
//        executorService.submit(() -> {
//            System.out.println(Thread.currentThread().getName());
//            int i = 1 / 0;
//        });
//
//        Thread.sleep(2000L);
//        executorService.submit(() -> {
//            System.out.println(Thread.currentThread().getName());
//            System.out.println("当线程池抛出异常后继续新的任务");
//        });
//
//        executeSchedule();
//        threadPoolParameterTest();
//        firstThreadBlockingOtherThreads();
//        test();
//        workStealingPoolTest();
        firstThreadBlockingOtherThreads();
    }

    private static void threadPoolParameterTest() {
        System.out.println(Long.toBinaryString(4294967296L));
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
        System.out.println(Integer.toBinaryString(0 << 29) + "0<<29");
        System.out.println(Integer.toBinaryString(-536870912));
//        System.out.println(CAPACITY);
        executeSchedule();
        System.out.println(Integer.toBinaryString(-1 - 1));//11111111111111111111111111111110
        System.out.println(-1 - 1);
        System.out.println(Long.valueOf("10100000000000000000000000000000", 2));
        System.out.println(Integer.toBinaryString(~3) + 1);//按位取反
        System.out.println((~3) + 1);
    }

    @SneakyThrows
    private static void test() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < 3; i++) {
            System.out.println(i + "1111");
            threadPoolExecutor.execute(() -> {
                int number = atomicInteger.incrementAndGet();
                while (true) {
//                    try {
                    System.out.println(number + "等待下次执行～");
//                            try {
                    try {
                        int a = 1 / 0;
                        Thread.sleep(1000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    } catch (Exception e) {

//                        try {
                    System.out.println(number + "睡觉10s");
//                            Thread.sleep(10000L);
//                        } catch (InterruptedException ex) {
////                            throw new RuntimeException(ex);
//                        }
//                        threadPoolExecutor.shutdown();
//                        throw new RuntimeException(e);
//                        log.error("失败，err={}",e.toString());
//                    }

//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
                }
            });
        }
        threadPoolExecutor.execute(() -> {
            System.out.println("任务发布完成～，开始执行退出操作");
        });

        System.out.println("===============");
//        Thread.sleep(6000L);
//        threadPoolExecutor.shutdownNow();
        threadPoolExecutor.shutdown();
        boolean b = threadPoolExecutor.awaitTermination(5, TimeUnit.MINUTES);
        System.out.println(threadPoolExecutor.isShutdown());
//        if (!b) {
//            threadPoolExecutor.shutdownNow();
//            System.out.println("shutdownNow!");
//        }
        System.out.println("shutdown完成～" + b);
    }

    private static void firstThreadBlockingOtherThreads() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 20, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), (runnable) -> {
            //1.实现一个自己的线程池工厂
            //创建一个线程
            Thread thread = new Thread(runnable);
            //给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
            thread.setDefaultUncaughtExceptionHandler((Thread t1, Throwable e) -> System.out.println("线程工厂设置的exceptionHandler" + e.getMessage()));
            return thread;
        }) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {

            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {

            }

            @Override
            protected void terminated() {

            }
        };
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.execute(() -> {
                int number = atomicInteger.getAndIncrement();
                System.out.println("当前线程名称 ：" + Thread.currentThread().getThreadGroup().getName());
                while (true) {
                    int a = 1 / 0;
                    try {
                        System.out.println(number + "等下次运行");
                        System.out.println("当前线程名称 ：" + Thread.currentThread().getName());
                        Thread.sleep(2000L);

                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                }
            });
        }
        while (!(threadPoolExecutor.getActiveCount() == 5)) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("任务发布完成");
    }

    @SneakyThrows
    private static void executeSchedule() {
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

//        List<Callable<Integer>> callables = Arrays.asList(() -> index++, () -> index++);

//        threadPoolExecutor.invokeAny(callables);

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            threadPoolExecutor.execute(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("第" + finalI + "个任务被执行！");
            });
        }

//        ExecutorService executorService1 = Executors.newCachedThreadPool();
//        ExecutorService executorService2 = Executors.newFixedThreadPool(10);
//        ScheduledExecutorService executorService3 = Executors.newScheduledThreadPool(10);
//        ExecutorService executorService4 = Executors.newSingleThreadExecutor();
    }

    private static void workStealingPoolTest() throws InterruptedException {
        System.out.println("获得JAVA虚拟机可用的最大CPU处理器数量：" + Runtime.getRuntime().availableProcessors());
        ExecutorService executorService = Executors.newWorkStealingPool();
        /**
         * call方法存在返回值futureTask的get方法可以获取这个返回值。
         * 使用此种方法实现线程的好处是当你创建的任务的结果不是立即就要时，
         * 你可以提交一个线程在后台执行，而你的程序仍可以正常运行下去，
         * 在需要执行结果时使用futureTask去获取即可。
         */
        List<Callable<String>> callableList = IntStream.range(0, 20).boxed().map(i -> (Callable<String>) () -> {
            TimeUnit.SECONDS.sleep(3);
            System.out.println(String.format("当前【%s】线程正在执行>>>", Thread.currentThread().getName()));
            return "callable type thread task：" + i;
        }).collect(Collectors.toList());

        executorService.submit(() -> null);

        // 执行给定的任务，返回持有他们的状态和结果的所有完成的期待列表。
        executorService.invokeAll(callableList).stream().map(futureTask -> {
            try {
                return futureTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(System.out::println);
    }

}
