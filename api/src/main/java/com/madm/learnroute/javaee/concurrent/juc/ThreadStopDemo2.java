package com.madm.learnroute.javaee.concurrent.juc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author  Fox
 */
@Slf4j
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
                log.debug("重新设置线程中断状态为true");
                //重新设置线程中断状态为true
//                Thread.currentThread().interrupt();
            }
        }
        System.out.println("线程停止： stop thread");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new ThreadStopDemo2());
        thread.start();
        Thread.sleep(5);
        while(thread.isAlive()){
            thread.interrupt();
        }
    }
}

