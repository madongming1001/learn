package com.madm.learnroute.javaee;

import java.util.concurrent.CompletableFuture;

public class CompletableFuturePractice {

    public static void main(String[] args) {
        try {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                return 100;
            });
            System.out.println(future.get());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
