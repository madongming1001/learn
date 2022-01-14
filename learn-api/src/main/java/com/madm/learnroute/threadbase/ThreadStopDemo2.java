package com.madm.learnroute.threadbase;

/**
 * @author  Fox
 */
public class ThreadStopDemo2 implements Runnable {

    @Override
    public void run() {
        int count = 0;
        while (!Thread.currentThread().isInterrupted() && count < 1000) {
            System.out.println("count = " + count++);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //重新设置线程中断状态为true
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("线程停止： stop thread");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new ThreadStopDemo2());
        thread.start();
        Thread.sleep(5);
        thread.interrupt();
    }
}

