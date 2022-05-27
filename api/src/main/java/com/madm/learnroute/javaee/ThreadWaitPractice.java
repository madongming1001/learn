package com.madm.learnroute.javaee;

import java.util.concurrent.TimeUnit;

public class ThreadWaitPractice {

    public static  void main(String[] args) throws InterruptedException {
        ThreadWaitPractice threadWaitPractice = new ThreadWaitPractice();
        Thread thread = new Thread(() -> {
            try {
                test1(threadWaitPractice);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(111);
        TimeUnit.MILLISECONDS.sleep(2000);
        test2(thread,threadWaitPractice);
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println(222);
    }

    private static synchronized void test2(Thread thread,ThreadWaitPractice t) throws InterruptedException {
        thread.start();
        test1(t);
    }

    public static synchronized void test1(ThreadWaitPractice t) throws InterruptedException {
//        synchronized (t){
            t.wait();
//        }
        System.out.println(333);
    }

}
