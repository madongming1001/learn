package com.madm.learnroute.jvm.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
class A implements Runnable{

    @Override
    public void run() {
        System.out.println(3);
    }
}
public class CyclicBarrierTest {
    static CyclicBarrier barrier = new CyclicBarrier(2,new A());

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(1);
        }).start();
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(2);
    }
}
