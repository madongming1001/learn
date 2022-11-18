package com.madm.learnroute.javaee.concurrent.util;

import lombok.SneakyThrows;

import java.util.HashMap;

/**
 * @author dongming.ma
 * @date 2022/11/18 17:19
 */
public class NoSafeHashMapPractice {
    public static final HashMap<String, String> hashMap = new HashMap();

    @SneakyThrows
    public static void main(String[] args) {

        Thread t1 = new Thread(new PutRunnable(hashMap, 0, 25));
        Thread t2 = new Thread(new PutRunnable(hashMap, 25, 50));

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        for (int i = 0; i < 50; i++) {
            System.out.println(i + "/" + hashMap.get(i + ""));
        }
    }
}

class PutRunnable implements Runnable {
    HashMap hashMap;
    int begin;
    int end;

    public PutRunnable(HashMap hashMap, int begin, int end) {
        this.hashMap = hashMap;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void run() {
        for (int i = begin; i < end; i++) {
            hashMap.put(i + "", i + "");
        }
    }
}
