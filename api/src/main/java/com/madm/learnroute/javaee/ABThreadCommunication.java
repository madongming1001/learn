package com.madm.learnroute.javaee;

import java.util.concurrent.TimeUnit;

public class ABThreadCommunication {

    private volatile static boolean a = false;
    private volatile static boolean b = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println("发送a线程的信号");
            a = true;
            while (!b) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("收到b线程的信号");
        });
        Thread t2 = new Thread(() -> {
            System.out.println("收到a线程的信号");
            while (!a) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("发送b线程的信号");
            b = true;
        });
        t1.start();
        t2.start();
    }

}
