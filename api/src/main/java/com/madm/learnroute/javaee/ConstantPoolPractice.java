package com.madm.learnroute.javaee;


public class ConstantPoolPractice {
    public static void main(String[] args) {
        String s0 = "madm";
        String s1 = new String("madm");
        String s2 = "ma" + new String("dm");
        System.out.println(s0 == s1);
        System.out.println(s0 == s2);
        System.out.println(s1 == s2);
    }
}