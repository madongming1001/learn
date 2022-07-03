package com.madm.learnroute.jvm.juc.threadpool;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Slf4j
public class ThreadStatePractice {
    public static void main(String[] args) throws Exception {
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("1111");
                return 1;
            }
        });

//        System.out.println(futureTask.get());
        new Thread(futureTask).run();

//
        Thread thread = new Thread(() -> {
            System.out.println("利用runnbale方式创建线程");
        });
        //新建线程执行
        thread.start();
        //main线程执行 调用方法
        thread.run();
//
//
//        log.debug("线程状态：{}",thread.getState());
//
//
//        log.debug("线程状态：{}",thread.getState());
//        Thread.sleep(100);
//        log.debug("线程状态：{}",thread.getState());
//        LockSupport.unpark(thread);

    }
}
