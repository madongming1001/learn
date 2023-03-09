package com.madm.learnroute.javaee;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class SemaphorePractice {

    public static void main(String[] args) throws InterruptedException {
        test02();
    }

    public static void test02() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);

        CountDownLatch countDownLatch = new CountDownLatch(3);
        AtomicBoolean release = new AtomicBoolean(false);
        System.out.println(release.get());
        for (int i = 0; i < 1; i++) {
            boolean acquire = semaphore.tryAcquire(3000, TimeUnit.MILLISECONDS);
            if (acquire) {
                CompletableFuture<Object> supplyAsync = CompletableFuture.supplyAsync(() -> doSomeThing());
                supplyAsync.whenCompleteAsync((result, t) -> {
                    System.out.println("whencomplete");
                    if (t != null) {
                        t.printStackTrace();
                    } else {
                        // 处理结束流程
                    }
                    semaphore.release();
                    countDownLatch.countDown();
                });
            } else {

            }
        }
        boolean timeout = !countDownLatch.await(5000, TimeUnit.MILLISECONDS);
        if (timeout) {
            System.out.println("!11");
        }
    }

    private static Integer doSomeThing() {
        return 10;
//        throw new IllegalArgumentException();
    }
}
