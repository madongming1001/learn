package com.madm.learnroute.javaee.concurrent.juc;

import lombok.SneakyThrows;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class CyclicBarrierTest3 {
    static CyclicBarrier cb = new CyclicBarrier(2);

    @SneakyThrows
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        semaphore.release();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cb.await();
                } catch (Exception e) {

                }
            }
        }, "customThread");
        t.start();
//        t.interrupt();
        try {
            cb.await();
        } catch (Exception e) {
            System.out.println(cb.isBroken());
        }
    }
}
