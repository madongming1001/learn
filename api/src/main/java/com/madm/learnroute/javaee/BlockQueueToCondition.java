package com.madm.learnroute.javaee;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class BlockQueueToCondition {
    public static void main(String[] args) {
        final BoudedBuffer bf = new BoudedBuffer();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bf.put("zhouzhixiang[" + i + "]");
                }
            }).start();
        });

        IntStream.rangeClosed(1, 5).forEach(i -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    bf.take();
                }
            }).start();
        });

    }

    static class BoudedBuffer {
        final Lock lock = new ReentrantLock();
        final Condition notEmpty = lock.newCondition();
        final Condition notFull = lock.newCondition();
        private Object[] buffer = new Object[5];
        private int input_index, take_index, count;

        public void put(Object x) {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " ready to put .....");
                while (count == buffer.length) {
                    System.err.println("put data to buffer[" + input_index + "] : error : buffer full cannot insert ....");
                    notFull.await();
                }
                buffer[input_index] = x;
                System.out.println("put data to buffer[" + input_index + "] : success : buffer insert data" + String.valueOf(x));
                if (++input_index == buffer.length) {
                    input_index = 0;
                }
                count++;
                notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public Object take() {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + " ready to take .....");
                while (count == 0) {
                    System.err.print("take from buffer[" + take_index + "] : error : buffer is empty now");
                    notEmpty.await();
                }
                System.out.println("take from buffer[" + take_index + "] : success : take data is " + String.valueOf(buffer[take_index]));
                if (++take_index == buffer.length) {
                    take_index = 0;
                }
                count--;
                notFull.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return buffer[take_index];
        }

    }
}
