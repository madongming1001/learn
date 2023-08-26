package com.madm.learnroute.javaee;


import java.util.concurrent.TimeUnit;

/**
 * 类说明：ThreadLocal的线程不安全演示
 * 多执行几次方法,看控制台输出,十个线程输出结果不全是 10 而且也没有做到线程隔离,这就是线程不安全的问题.
 */
public class ThreadLocalUnsafe<T> implements Runnable {
    //因为是static的,就被几个线程共享了,所以就产生了线程不安全的情况
    public static Number number = new Number(0);
    //把static去掉就正确了,让每个线程都拥有自己独立的number.此时十个线程的输出都是1
//    public Number number = new Number(0);
    public static ThreadLocal<Number> value = new ThreadLocal();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new ThreadLocalUnsafe()).start();
        }
    }

    @Override
    public void run() {
        //每个线程计数加一
        number.setNum(number.getNum() + 1);
        //将其存储到ThreadLocal中
        value.set(number);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }
        //输出num值
        System.out.println(Thread.currentThread().getName() + "=" + value.get().getNum());
    }

    private static class Number {
        private int num;

        public Number(int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        @Override
        public String toString() {
            return "Number [num=" + num + "]";
        }
    }

}


