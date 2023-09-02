package com.madm.learnroute.javaee.concurrent.blockqueue;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2023/9/2 13:21
 */
@Slf4j
public class SynchronousQueuePractice {

    private static final SynchronousQueue synchronousQueue = new SynchronousQueue();

    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable producer = () -> {
            Object object = new Object();
            try {
                synchronousQueue.put(object);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
            log.info("produced {}", object);
        };

        Runnable consumer = () -> {
            try {
                Object object = synchronousQueue.take();
                log.info("consumed {}", object);
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
            }
        };

        executor.submit(producer);
        executor.submit(consumer);

        executor.awaitTermination(50000, TimeUnit.MILLISECONDS);
        executor.shutdown();

    }
}
