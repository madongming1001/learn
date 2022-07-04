package com.madm.learnroute.javaee.blockqueue;

import java.util.concurrent.ArrayBlockingQueue;

public class ArrayBlockingQueuePractice {
    public static void main(String[] args) {
        ArrayBlockingQueue<String> blockingQueue = new ArrayBlockingQueue<String>(1);
        blockingQueue.add("11");
        try {
            String take = blockingQueue.take();
            System.out.println(blockingQueue.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
