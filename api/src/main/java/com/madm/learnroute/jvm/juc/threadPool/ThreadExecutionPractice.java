package com.madm.learnroute.jvm.juc.threadPool;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ThreadExecutionPractice extends Thread {

    private String name;
    private Runnable task;

    public ThreadExecutionPractice(Runnable task) {
        this.task = task;
    }

    @Override
    public void run() {
        System.out.println(name);
        task.run();
    }
}
