package com.madm.learnroute.jvm.juc;

/**
 * @author Fox
 * 中断机制
 */
public class ThreadInterruptTest {

    static int i = 0;

    public static void main(String[] args) {
        System.out.println("begin");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    i++;
                    System.out.println(i);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //Thread.interrupted()  清除中断标志位 boolean
                    //Thread.currentThread().isInterrupted() 不会清除中断标志位 验证 boolean
                    //Thread.currentThread().interrupt(); 重置标志位 void
                    if (Thread.interrupted()) {
                        System.out.println("=========");
                    }
                    if (i == 10) {
                        break;
                    }

                }
            }
        });

        t1.start();
        //不会停止线程t1,只会设置一个中断标志位 flag=true
        t1.interrupt();

    }
}
