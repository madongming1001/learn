package com.madm.learnroute.javaee;

/**
 * 交叉打印
 */
class ThreadPrintDemo2 {
    private volatile boolean lock = false;

    public static void main(String[] args) {
        final ThreadPrintDemo2 demo2 = new ThreadPrintDemo2();
        Thread t1 = new Thread(demo2::print2);
        Thread t2 = new Thread(demo2::print1);
        t2.start();
        t1.start();
    }

    public synchronized void print1() {
        while (!lock) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 1; i <= 100; i += 2) {
            System.out.println(i);
            this.notify();
            try {
                this.wait();
                Thread.sleep(100);// 防止打印速度过快导致混乱
            } catch (InterruptedException e) {        // NO
            }
        }
    }

    public synchronized void print2() {
        lock = true;
        for (int i = 0; i <= 100; i += 2) {
            System.out.println(i);
            this.notify();
            try {
                this.wait();
                Thread.sleep(100);// 防止打印速度过快导致混乱
            } catch (InterruptedException e) {        // NO

            }
        }
    }
}
