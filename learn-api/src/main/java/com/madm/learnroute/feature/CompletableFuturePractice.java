package com.madm.learnroute.feature;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFuturePractice {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("CompletableFuture 可以监控这个任务的执行");
                future.complete("任务返回结果");
            }
        }).start();
        System.out.println(future.get());
    }
}
