package com.example.learnroute.javaee;

import java.util.concurrent.CompletableFuture;

public class CompletableFuturePractice {

    public static void main(String[] args) {
        try {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                int i = 1/0;
                return 100;
            });
//            future.join();
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
