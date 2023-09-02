package com.madm.learnroute.javaee.concurrent.blockqueue;

import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;

/**
 * @author dongming.ma
 * @date 2023/9/2 17:15
 */
public class LinkedTransferQueuePractice {
    private static int threadInitNumber;

    public static void main(String[] args) {
        System.out.println(threadInitNumber++);
        TransferQueue<String> queue = new LinkedTransferQueue<>();
        Thread producer = new Thread(new Producer(queue));
        producer.setDaemon(true); //设置为守护进程使得线程执行结束后程序自动结束运行
        producer.start();
        for (int i = 0; i < 10; i++) {
            Thread consumer = new Thread(new Consumer(queue));
            consumer.setDaemon(true);
            consumer.start();
            try {
                // 消费者进程休眠一秒钟，以便以便生产者获得CPU，从而生产产品
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
    }
}

class Producer implements Runnable {
    private final TransferQueue<String> queue;

    public Producer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    private String produce() {
        return " your lucky number " + (RandomUtil.randomInt(100));
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (queue.hasWaitingConsumer()) {
                    queue.transfer(produce());
                }
                TimeUnit.SECONDS.sleep(1);//生产者睡眠一秒钟,这样可以看出程序的执行过程
            }
        } catch (InterruptedException e) {
        }
    }
}

class Consumer implements Runnable {
    private final TransferQueue<String> queue;

    public Consumer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            System.out.println(" Consumer " + Thread.currentThread().getName() + queue.take());
        } catch (InterruptedException e) {
        }
    }
}