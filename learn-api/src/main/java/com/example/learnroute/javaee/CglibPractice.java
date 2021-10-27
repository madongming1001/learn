package com.example.learnroute.javaee;


public class CglibPractice {
    public static void main(String[] args) {
        //逻辑核的核数
        System.out.println(Runtime.getRuntime().availableProcessors());
        System.out.println(Runtime.getRuntime().totalMemory() / (1024*1024));
    }
}
