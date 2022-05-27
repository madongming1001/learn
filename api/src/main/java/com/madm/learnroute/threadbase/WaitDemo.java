package com.madm.learnroute.threadbase;

public class WaitDemo {

    private static Object lock = new Object();
    private static  boolean flag = true;

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    while (flag){
                        try {
                            System.out.println("wait start .......");
                            //等待
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("wait end ....... ");
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (flag){
                    synchronized (lock){
                        if (flag){

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //通知
                            lock.notifyAll();
                            System.out.println("notify .......");
                            flag = false;
                        }

                    }
                }
            }
        }).start();
    }
}