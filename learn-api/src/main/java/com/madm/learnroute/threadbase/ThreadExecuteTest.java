package com.madm.learnroute.threadbase;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Fox
 */
@Slf4j
public class ThreadExecuteTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                log.debug("通过Runnable方式执行任务");
//            }
//        };

        // 操作系统创建线程
        // java Thread --> jvm JavaThread---->os Thread

        // new Thread(runnable).start();
        // new Object()--->jvm JavaThread

//        new Thread(runnable).start();
        // 这是一个真正的线程吗？  普通对象的方法调用
//        new Thread(runnable).run();
//        runnable.run();


//        FutureTask task = new FutureTask(new Callable() {
//            @Override
//            public Object call() throws Exception {
//                log.debug("通过Callable方式执行任务");
//                Thread.sleep(3000);
//                return "返回任务结果";
//            }
//        });
//
//        new Thread(task).start();
//        log.debug("结果：{}",task.get());


        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(() -> {
            System.out.println("测试execute方法");
        });
//        log.debug("monkey下班回家做饭");
//        Future<String> future = executor.submit(new Callable<String>() {
//            @Override
//            public String call() {
//                //真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
//                log.debug("开始煮饭");
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return "饭熟了";
//            }
//        });
//        Future<String> future2 = executor.submit(new Callable<String>() {
//            @Override
//            public String call() {
//                //真正的任务在这里执行，这里的返回值类型为String，可以为任意类型
//                log.debug("开始做菜");
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return "菜好了";
//            }
//        });
//
//        log.debug("{},{},monkey开始吃饭",future.get(), future2.get());
//
//        executor.shutdown();

    }
}
