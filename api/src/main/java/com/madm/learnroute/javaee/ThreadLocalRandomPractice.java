package com.madm.learnroute.javaee;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ThreadLocalRandomPractice {

    public static void main(String[] args) {
        //多线程的情况下会导致大量线程重试
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            System.out.println(random.nextInt(10));
        }

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (int i = 0; i < 5; i++) {
            System.out.println(threadLocalRandom.nextInt(10));
        }
    }
}
