package com.madm.learnroute.jvm.juc;

/**
 * @author Fox
 * 一个简单卖票程序:多个窗口卖票
 */
public class SellTicketDemo implements Runnable {
    /**
     * 车票
     */
    private int ticket;

    public SellTicketDemo() {
        this.ticket = 1000;
    }

    @Override
    public void run() {
        while (ticket > 0) {
            synchronized (this) {
                if (ticket > 0) {
                    try {
                        // 线程进入暂时的休眠
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 获取到当前正在执行的程序的名称，打印余票
                    System.out.println(Thread.currentThread().getName()
                            + ":正在执行操作，余票:" + ticket--);
                }
            }
            Thread.yield();
        }
    }

    public static void main(String[] args) {
        SellTicketDemo demo = new SellTicketDemo();

        Thread thread1 = new Thread(demo,"thread1");
        Thread thread2 = new Thread(demo,"thread2");
        Thread thread3 = new Thread(demo,"thread3");
        Thread thread4 = new Thread(demo,"thread4");
        //priority优先级默认是5，最低1，最高10
        thread1.setPriority(Thread.MIN_PRIORITY);
        thread2.setPriority(Thread.MAX_PRIORITY);
        thread3.setPriority(Thread.MIN_PRIORITY);
        thread4.setPriority(Thread.MAX_PRIORITY);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}