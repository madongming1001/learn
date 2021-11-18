package com.madm.learnroute.ThreadPool;

import java.util.concurrent.*;

public class ThreadPoolExecutorPractor {
    public static void main(String[] args) {
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
        // DiscardPolicy 第五种什么也没做
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        });
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Long.valueOf("1001010001010101",2));
            }
        });
    }
}
