package com.madm.learnroute.ThreadPool;

import lombok.Data;

public class ThreadExecutionPractice extends Thread{

    private String name;

    public ThreadExecutionPractice(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name);
    }

    public static void main(String[] args) {
        // 方法调用
//        new ThreadExecutionPractice("thread1").run();
        // 线程启动
        new ThreadExecutionPractice("thread2").start();
    }
}
