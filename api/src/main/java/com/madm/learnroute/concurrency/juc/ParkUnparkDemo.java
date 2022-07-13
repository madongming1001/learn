package com.madm.learnroute.concurrency.juc;

import java.util.concurrent.locks.LockSupport;

public class ParkUnparkDemo {
    //包子铺对象
    volatile Object bozipu = null;
    //资源锁
    Object baozi_lock = new Object();

    public static void main(String[] args) throws Exception{
        System.out.println("主线程开始运行");
        //new ParkUnparkDemo().parkUnparkTest();
        new ParkUnparkDemo().parkUnparkExceptionTest();
        //new ParkUnparkDemo().moreUnparkTest();
        //new ParkUnparkDemo().moreParkTest();


    }

    /**
     * 测试park/unpark方法的使用
     */
   public  void parkUnparkTest() throws Exception {
        //开启消费者线程1：等待包子铺有包子之后，进行消费买包子
       Thread consumer =new Thread(() -> {
           //如果包子铺没有开业，则等待包子铺开业的"许可"
           System.out.println("等待包子铺开张。。。");
           //这里使用while进行是否被唤醒，不要使用if，因为会有伪唤醒的状态
           while (bozipu == null){
               LockSupport.park();
               System.out.println("包子已经买到，可以回家了！");
           }
       });
       consumer.start();

       //等待3秒钟开始创建包子铺
       Thread.sleep(3000);
       bozipu = new Object();
       //给线程consummer颁发许可
       LockSupport.unpark(consumer);
       System.out.println("已经通知消费者包子铺开张");
   }

    /**
     * park/unpark方法异常情况测试
     */
   public   void parkUnparkExceptionTest() throws Exception{
       //开启消费者线程1：等待包子铺有包子之后，进行消费买包子
       Thread consumer =new Thread(() -> {
           //如果包子铺没有开业，则等待包子铺开业的"许可"
           System.out.println("等待包子铺开张。。。");
           if(bozipu == null){
               //拿到baozi_lock锁
               synchronized (baozi_lock) {
                   LockSupport.park();
                   System.out.println("包子已经买到，可以回家了！");
               }
           }
       });
       consumer.start();

       //等待3秒钟开始创建包子铺
       Thread.sleep(3000);
       bozipu = new Object();
       //此时因为baozi_lock锁已经被消费者占有，无法继续执行
       synchronized (baozi_lock){
           //给线程consummer颁发许可
           LockSupport.unpark(consumer);
           System.out.println("已经通知消费者包子铺开张");
       }
   }

    /**
     * 多次调用unpark方法，调用一次park方法,线程会继续运行
     */
   public void moreUnparkTest(){
       LockSupport.unpark(Thread.currentThread());
       LockSupport.unpark(Thread.currentThread());
       LockSupport.unpark(Thread.currentThread());
       System.out.println("调用了三次unpark");
       LockSupport.park(Thread.currentThread());
       System.out.println("调用了一次park");
   }

    /**
     * 多次调用park方法，调用一次unpark方法，线程会进入等待状态
     */
    public void moreParkTest(){
        LockSupport.park(Thread.currentThread());
        System.out.println("调用了一次park");

        LockSupport.park(Thread.currentThread());
        LockSupport.park(Thread.currentThread());
        System.out.println("又调用了两次park");
        LockSupport.unpark(Thread.currentThread());
        System.out.println("调用了一次unpark方法");
    }
}
