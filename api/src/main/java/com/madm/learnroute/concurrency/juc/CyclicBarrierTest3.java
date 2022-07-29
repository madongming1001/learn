package com.madm.learnroute.concurrency.juc;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest3 {
    static CyclicBarrier cb = new CyclicBarrier(2);

    public static void main(String[] args) {
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
