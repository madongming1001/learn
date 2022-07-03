package com.madm.learnroute.javaee;

import java.util.concurrent.TimeUnit;

public class ThreadDamonPractice {
    public static void main(String[] args) {
        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是一个用户线程");
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1L);
                    } catch (InterruptedException e) {
                    }
                }

            }
        });
        Thread daemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mainThread.isAlive()) {
                    System.out.println("我是一个守护线程");
                }
            }
        });
        daemonThread.setDaemon(true);
        mainThread.start();
        daemonThread.start();
    }
}
