package com.madm.learnroute.javaee.concurrency.juc.threadpool;

import java.util.concurrent.*;

public class SimpleThreadFactory implements ThreadFactory {

    public Thread newThread(Runnable r) {
        return new Thread(r);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Thread tf = new SimpleThreadFactory().newThread(() -> System.out.println("利用线程工厂创建一个线程"));
//        System.out.println(tf.getName());
//        System.out.println(Thread.currentThread().getName());
//        tf.run();
//        System.out.println("ssss");
//        tf.start();
//        System.out.println("dddd");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<Integer> submit = executorService.submit(new CallableTask(1));
        System.out.println("方法调用："+submit.get());
    }
}
class CallableTask implements Callable<Integer>{

    private Integer id;

    public CallableTask(Integer id) {
        this.id = id;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("call方法被调用："+id);
        return id;
    }
}
