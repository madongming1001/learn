package com.madm.learnroute.javaee;

public class VolatileBarrierExample {

    public static void main(String[] args) {
        System.out.println(52 / 62);
    }

    int a;

    volatile int v1 = 1;
    volatile int v2 = 2;

    public void readAndWrite() {
        int i = v1;// 第一个volatile读
        int j = v2;// 第二个volatile读
        a = i + j; // 普通写
        v1 = i + 1;// 第一个volatile写
        v2 = j * 2;// 第二个 volatile写
    }
}
