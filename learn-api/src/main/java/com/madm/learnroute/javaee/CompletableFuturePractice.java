package com.madm.learnroute.javaee;

import java.util.concurrent.CompletableFuture;

public class CompletableFuturePractice {

    public static void main(String[] args) {
        try {
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> System.out.println(1001));
            CompletableFuture<Object> objectCompletableFuture = CompletableFuture.supplyAsync(() -> 102);
            System.out.println(voidCompletableFuture.get());
            System.out.printf((String) objectCompletableFuture.get());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
