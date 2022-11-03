package com.madm.learnroute.javaee;

import org.openjdk.jol.info.ClassLayout;



class MyCustomClassLoader {
    private Long initStatus;
}

public class ThreadLocalRandomPractice {

    static Object obj = new Object();

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}
