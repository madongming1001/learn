package com.madm.learnroute.javaee;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author madongming
 */
public class HashMapPractice {

    private static final AtomicInteger userNumber = new AtomicInteger();
    static final HashMap<String, String> map = new HashMap<String, String>(2);

    public static void main(String[] args) {
//        Map<String, Long> masterId = new HashMap<>();
//        ArrayList<Map<String, Long>> mapArrayList = new ArrayList<Map<String, Long>>();
//        HashMap<String, Long> hashMap1 = new HashMap<>();
//        hashMap1.put("1", 1L);
//        HashMap<String, Long> hashMap2 = new HashMap<>();
//        hashMap2.put("2", 2L);
//        HashMap<String, Long> hashMap3 = new HashMap<>();
//        hashMap3.put("3", 3L);
//        HashMap<String, Long> hashMap4 = new HashMap<>();
//        hashMap4.put("4", 4L);
//        mapArrayList.add(hashMap1);
//        mapArrayList.add(hashMap2);
//        mapArrayList.add(hashMap3);
//        mapArrayList.add(hashMap4);
//
//        mapArrayList.stream().forEach(m -> {
//            masterId.putAll(m);
//        });
//        System.out.println(masterId);

        threadSafeQuestion();
    }

    private static void threadSafeQuestion() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            map.put(UUID.randomUUID().toString(), "");
                        }
                    }, "ftf" + i).start();
                }
            }
        }, "ftf");
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printIntegerDefaultValue() {
        userNumber.getAndIncrement();
        System.out.println(userNumber);
    }
}

