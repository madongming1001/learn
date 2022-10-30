package com.madm.learnroute.javaee.concurrent.blockqueue;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author dongming.ma
 * @date 2022/6/16 22:48
 */
public class PriorityBlockingQueuePractice {
    public static void main(String[] args) {
        m2();
    }

    public static void m2() {
        PriorityBlockingQueue<Student> priorityBlockingQueue = new PriorityBlockingQueue<>();
        priorityBlockingQueue.add(new Student("叫练1", 22));
        priorityBlockingQueue.add(new Student("叫练2", 21));
        priorityBlockingQueue.add(new Student("叫练3", 23));
        while (!priorityBlockingQueue.isEmpty()) {
            Student student = null;
            try {
                student = priorityBlockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(student);
        }
    }

    private static class Student implements Comparable<Student> {
        private String name;
        private int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Student{" + "name='" + name + '\'' + ", age=" + age + '}';
        }

        @Override
        public int compareTo(Student o) {
            //从大到小排队
            return this.age - o.getAge();
        }
    }
}
