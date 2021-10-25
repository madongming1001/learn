package com.example.learnroute.javaee;

import org.openjdk.jol.info.ClassLayout;

class MyCustomClassLoader {
    private Long initStatus;
}
public class ThreadLocalRandomPractice {
    public static void main(String[] args) {
        MyCustomClassLoader classLoader = new MyCustomClassLoader();
        System.out.println(ClassLayout.parseInstance(classLoader).toPrintable());
    }
}
