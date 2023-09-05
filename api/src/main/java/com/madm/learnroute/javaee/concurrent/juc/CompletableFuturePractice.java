package com.madm.learnroute.javaee.concurrent.juc;

import com.madm.learnroute.javaee.Discount;
import lombok.Data;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CompletableFuturePractice {
    static List<Shop> shops = Arrays.asList(new Shop("BestPrice"), new Shop("LetsSaveBig"), new Shop("MyFavoriteShop"), new Shop("BuyItAll"));

    public static void main(String[] args) {
        Shop shop = new Shop("BestPrice");
        System.out.println(shop.getPrice("product"));
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> System.out.println(1001));
        CompletableFuture<Integer> objectCompletableFuture = CompletableFuture.supplyAsync(() -> 102);
        try {
            System.out.println(voidCompletableFuture.get());
            System.out.printf(String.valueOf(objectCompletableFuture.get()));
            //有异常类型 CancellationException ExecutionException InterruptedException 检查异常 强制抛出
            CompletableFuture.allOf(voidCompletableFuture, objectCompletableFuture).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //无异常类型 CancellationException CompletionException 未经检查异常 不强制抛出
        CompletableFuture<Void> allCompletedTasks = CompletableFuture.allOf(voidCompletableFuture, objectCompletableFuture);

        try {
            allCompletedTasks.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        List<CompletableFuture> futures = new ArrayList<>();
        futures.add(CompletableFuture.runAsync(() -> System.out.println(1001)));
        futures.add(CompletableFuture.runAsync(() -> System.out.println(1001)));
        CompletableFuture<List<Object>> listCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        //执行后运行，需不需要上一个返回值
        //thenRun(Runnable runnable)，任务 A 执行完执行 B，并且 B 不需要 A 的结果。
        //thenAccept(Consumer action)，任务 A 执行完执行 B，B 需要 A 的结果，但是任务 B 不返回值。
        //thenApply(Function fn)，任务 A 执行完执行 B，B 需要 A 的结果，同时任务 B 有返回值。
        resultDelivery();

        //异常处理
        //public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn);
        //public <U> CompletionStage<U> handle(BiFunction<? super T, Throwable, ? extends U> fn);

        //同时执行，组合结果，任务A 和任务B 同时执行，然后取它们的结果进行后续操作。这里强调的是任务之间的并行工作，没有先后执行顺序。
        //cfA.runAfterBoth(cfB, () -> {});
        //cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {}); 没有返回值
        //cfA.thenCombine(cfB, (resultA, resultB) -> "result A + B"); 有返回值
        //前面一个 CompletableFuture 实例的结果可以传递到下一个实例中，这就是 compose 和 combine 的主要区别。

        //取多个任务的结果
        //public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs){...}
        //public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs) {...}

        //两个结果之中的任意一个
        //cfA.acceptEither(cfB, result -> {});
        //cfA.acceptEitherAsync(cfB, result -> {});
        //cfA.acceptEitherAsync(cfB, result -> {}, executorService);

        //cfA.applyToEither(cfB, result -> result);
        //cfA.applyToEitherAsync(cfB, result -> result);
        //cfA.applyToEitherAsync(cfB, result -> result, executorService);

        //cfA.runAfterEither(cfA, () -> {});
        //cfA.runAfterEitherAsync(cfB, () -> {});
        //cfA.runAfterEitherAsync(cfB, () -> {}, executorService);

    }

    private static void testCompose() {
        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> {
            System.out.println("processing a...");
            return "hello";
        });

        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> {
            System.out.println("processing b...");
            return " world";
        });

        CompletableFuture<String> cfC = CompletableFuture.supplyAsync(() -> {
            System.out.println("processing c...");
            return ", I'm robot!";
        });

        cfA.thenCombine(cfB, (resultA, resultB) -> {
            System.out.println(resultA + resultB);  // hello world
            return resultA + resultB;
        }).thenCombine(cfC, (resultAB, resultC) -> {
            System.out.println(resultAB + resultC); // hello world, I'm robot!
            return resultAB + resultC;
        });

        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            // 第一个实例的结果
            return "hello";
        }).thenCompose(resultA -> CompletableFuture.supplyAsync(() -> {
            // 把上一个实例的结果传递到这里
            return resultA + " world";
        })).thenCompose(resultAB -> CompletableFuture.supplyAsync(() -> {
            // 到这里大家应该很清楚了
            return resultAB + ", I'm robot";
        }));

        System.out.println(result.join()); // hello world, I'm robot
    }

    private static void resultDelivery() {
        CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB").thenApply(resultB -> resultB + " resultC").thenApply(resultC -> resultC + " resultD");
    }

    private static void exceptionExecute() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "resultA").thenApply(resultA -> resultA + " resultB")
                // 任务 C 抛出异常
                .thenApply(resultB -> {
                    throw new RuntimeException();
                })
                // 处理任务 C 的返回值或异常
                .handle((re, throwable) -> {
                    if (throwable != null) {
                        return "errorResultC";
                    }
                    return re;
                }).thenApply(resultC -> resultC + " resultD");

        System.out.println(future.join());
    }


    private static void combineResult() {
        CompletableFuture<String> cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture<String> cfB = CompletableFuture.supplyAsync(() -> "resultB");

        cfA.thenAcceptBoth(cfB, (resultA, resultB) -> {
        });
        cfA.thenCombine(cfB, (resultA, resultB) -> "result A + B");
        cfA.runAfterBoth(cfB, () -> {
        });
    }

    private static void executeMultipleTasks() {
        CompletableFuture cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture cfB = CompletableFuture.supplyAsync(() -> 123);
        CompletableFuture cfC = CompletableFuture.supplyAsync(() -> "resultC");

        CompletableFuture<Void> future = CompletableFuture.allOf(cfA, cfB, cfC);
        // 所以这里的 join() 将阻塞，直到所有的任务执行结束
        future.join();
    }

    private static void executeTwoTasks() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CompletableFuture cfA = CompletableFuture.supplyAsync(() -> "resultA");
        CompletableFuture cfB = CompletableFuture.supplyAsync(() -> 123);
        CompletableFuture cfC = CompletableFuture.supplyAsync(() -> "resultC");

        cfA.acceptEither(cfB, result -> {
        });
        cfA.acceptEitherAsync(cfB, result -> {
        });
        cfA.acceptEitherAsync(cfB, result -> {
        }, executorService);

        cfA.applyToEither(cfB, result -> result);
        cfA.applyToEitherAsync(cfB, result -> result);
        cfA.applyToEitherAsync(cfB, result -> result, executorService);

        cfA.runAfterEither(cfA, () -> {
        });
        cfA.runAfterEitherAsync(cfB, () -> {
        });
        cfA.runAfterEitherAsync(cfB, () -> {
        }, executorService);
    }

    private static void composeResult() {
        CompletableFuture<String> result = CompletableFuture.supplyAsync(() -> {
            // 第一个实例的结果
            return "hello";
        }).thenCompose(resultA -> CompletableFuture.supplyAsync(() -> {
            // 把上一个实例的结果传递到这里
            return resultA + " world";
        })).thenCompose(resultAB -> CompletableFuture.supplyAsync(() -> {
            // 到这里大家应该很清楚了
            return resultAB + ", I'm robot";
        }));

        System.out.println(result.join()); // hello world, I'm robot
    }

    private static void completableFutureTest() {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");
        // 可以返回方法，等到要用到方法返回值的时候再去拿取，中间可以做其他的任务，如果完成则直接使用，如果未完成，则阻塞等待
//        doSomethingElse();
        try {
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

    private static void printStreamModeTime() {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        System.out.println(shop.findPrices("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    @Data
    static class Shop {

        String name;

        public Shop(String name) {
            this.name = name;
        }

        public static void delay() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private static Double calculatePrice(String product) {
            delay();
            return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
        }

        public String getPrice(String product) {
            double price = calculatePrice(product);
            Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
            return String.format("%s:%.2f:%s", name, price, code);
        }

        public Future<Double> getPriceAsync(String product) {
//            CompletableFuture futurePrice = new CompletableFuture();
//            new Thread(() -> {
//                try {
//                    double price = calculatePrice(product);
//                    futurePrice.complete(price);
//                }catch (Exception e){
//                    futurePrice.completeExceptionally(e);
//                }
//            });
//            return futurePrice;
            return CompletableFuture.supplyAsync(() -> calculatePrice(product));
        }

        public List<String> findPrices(String product) {
            List<CompletableFuture<String>> priceFutures = shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + "price is " + shop.getPriceAsync(product))).collect(Collectors.toList());
            return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        }
    }

}
