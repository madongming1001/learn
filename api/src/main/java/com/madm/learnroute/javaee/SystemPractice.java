package com.madm.learnroute.javaee;


public class SystemPractice {
    static SystemPractice javaTest = new SystemPractice();

    public static void main(String[] args){
        f1();
    }

    {
        System.out.println("2");
    }

    SystemPractice(){
        // init
        System.out.println("3");
        System.out.println("a=" + a + ", b=" + b);
    }

    static {
        System.out.println("1");
    }

    public static void f1(){
        System.out.println("4");
    }

    int a = 100;
    static int b = 200;

}
