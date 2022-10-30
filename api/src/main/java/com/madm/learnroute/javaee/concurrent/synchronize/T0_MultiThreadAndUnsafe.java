package com.madm.learnroute.javaee.concurrent.synchronize;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ：杨过
 * @date ：Created in 2020/7/3
 * @version: V1.0
 * @slogan: 天下风云出我辈，一入代码岁月催
 * @description: 竞争
 **/
public class T0_MultiThreadAndUnsafe {

    private static int total = 0;
    private static Object object = new Object();
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        for(int i=0;i<10;i++){
            new Thread(()->{
                try {
                    countDownLatch.await();
                    for(int j=0;j<1000;j++){
                        try {
                            lock.lock();
                            //synchronized (object){
                            total++;
                            //}

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            lock.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Thread.sleep(1000);

        countDownLatch.countDown();

        Thread.sleep(2000);

        System.out.println(total);
    }
}
