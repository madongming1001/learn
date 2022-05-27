package com.madm.learnroute.javaee;

public class DeadLockPractice {

    private static Object lock1 = new Object();
    private static Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock1){
            System.out.println("thread1 begin");
            try {Thread.sleep(5000L);} catch (InterruptedException e) {e.printStackTrace();}
            synchronized (lock2) { System.out.println("thread1 end"); }
            }
        }).start();
        new Thread(() -> {
            synchronized (lock2){
                System.out.println("thread2 begin");
                try {Thread.sleep(5000L);} catch (InterruptedException e) {e.printStackTrace();}
                synchronized (lock1) { System.out.println("thread2 end"); }
            }
        }).start();
    }
}