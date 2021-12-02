package com.madm.learnroute.javaee;


public class ThreadLocalPractice {
    private static ThreadLocal threadLocal = new ThreadLocal<>();
    public static void main(String[] args) {
        threadLocal.set("threadLocal对象赋值");
    }
}
