package com.example.learnroute.javaee;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapPractice {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Boolean> conMap = new ConcurrentHashMap<>();
        conMap.put("testbase",true);
        conMap.put("testbase",false);
        System.out.println("test rebase in master branch");
    }
}
