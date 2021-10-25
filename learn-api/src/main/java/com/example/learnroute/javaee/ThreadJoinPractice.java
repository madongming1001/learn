package com.example.learnroute.javaee;

/**
 * @author madongming
 */
public class ThreadJoinPractice {
    public static void main(String[] args) {
        final Thread currentThread = Thread.currentThread();
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程1启动");
                for (; ; ) {
                }
            }
        });

        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("线程2启动");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    currentThread.interrupt();
                }
            }
        });
        threadOne.start();
        System.out.println("threadone start");
        System.out.println("1");
        threadTwo.start();
        System.out.println("2");
        System.out.println("threadTwo start");
        try {
            threadOne.join();
        } catch (InterruptedException e) {
            System.out.println("main thread:" + e);
        }
    }
}
