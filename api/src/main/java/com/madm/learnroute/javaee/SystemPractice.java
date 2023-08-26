package com.madm.learnroute.javaee;


public class SystemPractice {
    static SystemPractice javaTest = new SystemPractice();
    static int b = 200;

    static {
        System.out.println("1");
    }

    int a = 100;

    {
        System.out.println("2");
    }

    SystemPractice() {
        // init
        System.out.println("3");
        System.out.println("a=" + a + ", b=" + b);
    }

    public static void main(String[] args) {
        f1();
    }

    public static void f1() {
        System.out.println("4");
    }

}
