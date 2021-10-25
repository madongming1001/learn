package com.example.learnroute.javaee;

public class ClassLoaderPractice {
    public static void main(String[] args) {
        System.out.println(String.class.getClassLoader());
        System.out.println(ClassLoaderPractice.class.getClassLoader());
    }
}
