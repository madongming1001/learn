package com.madm.learnroute.javaee;

public class ThreadDamonPractice {
    public static void main(String[] args) {
        Thread mainThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是一个用户线程");
            }
        });
        Thread daemonThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(mainThread.isAlive()){
                    System.out.println("我是一个守护线程");
                }
            }
        });
        daemonThread.setDaemon(true);
        mainThread.start();
        daemonThread.start();
    }
}
