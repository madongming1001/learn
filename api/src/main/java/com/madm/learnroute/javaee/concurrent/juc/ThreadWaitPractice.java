package com.madm.learnroute.javaee.concurrent.juc;

/**
 * notify()或者notifyAll()方法并不是立刻释放锁，必须等到synchronized方法或者语法块执行完才真正释放锁
 *
 * @author dongming.ma
 * @date 2023/9/14 18:42
 */
public class ThreadWaitPractice {
    public static void main(String[] args) throws Exception {
        notifyFailThreadWaiting();
    }

    private static void notifyFailThreadWaiting() {
        final Object obj=new Object();
        new Thread(() -> {
            try {
                synchronized (obj) {
                    System.out.println("thread3 get lock");
                    obj.notifyAll(); //此时唤醒没有作用，没有线程等待
                    Thread.sleep(2000);
                    System.out.println("thread3 really release lock");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synchronized (obj) {
                    System.out.println("thread1 get lock");
                    obj.wait();//主动释放掉sum对象锁
                    System.out.println("thread1 release lock");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                synchronized (obj) {
                    System.out.println("thread2 get lock");
                    obj.wait();  //释放sum的对象锁，等待其他对象唤醒（其他对象释放sum锁）
                    System.out.println("thread2 release lock");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
