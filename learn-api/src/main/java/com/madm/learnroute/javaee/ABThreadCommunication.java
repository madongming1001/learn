package com.madm.learnroute.javaee;

public class ABThreadCommunication {

    private volatile static boolean a = false;
    private volatile static boolean b = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("发送a线程的信号");
                a = true;
                while (!b) {
                }
                System.out.println("收到b线程的信号");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!a) {
                }
                System.out.println("收到a线程的信号");
                System.out.println("发送b线程的信号");
                b = true;
            }
        });
        t1.start();
        t2.start();
    }

}
