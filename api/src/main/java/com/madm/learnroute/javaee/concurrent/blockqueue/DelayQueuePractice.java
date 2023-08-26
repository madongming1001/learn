package com.madm.learnroute.javaee.concurrent.blockqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2022/6/16 22:48
 */
public class DelayQueuePractice {
    public static void main(String[] args) throws InterruptedException {
        DelayQueue<Student> delayQueue = new DelayQueue<Student>();
        delayQueue.add(new Student("叫练1", 5));
        delayQueue.add(new Student("叫练2", 3));
        delayQueue.add(new Student("叫练3", 6));
        while (!delayQueue.isEmpty()) {
            System.out.println(delayQueue.take());
        }
    }


    private static class Student implements Delayed {
        private String name;
        //触发时间/秒
        private long time;

        public Student(String name, long time) {
            this.name = name;
            this.time = time * 1000 + System.currentTimeMillis();
        }

        public String getName() {
            return name;
        }

        public long getTime() {
            return time;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            //延迟时间小于0就出队列
            return time - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            //时间排序,从小到大排列
            Student student = (Student) o;
            return (int) (this.time - student.getTime());
        }

        @Override
        public String toString() {
            return "Student{" + "name='" + name + '\'' + ", time=" + time + '}';
        }
    }
}
