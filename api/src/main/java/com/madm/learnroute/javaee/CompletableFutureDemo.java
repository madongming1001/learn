package com.madm.learnroute.javaee;

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompletableFutureDemo {
    static List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    public static void main(String[] args) {
        Shop shop = new Shop("BestPrice");
        System.out.println(shop.getPrice("product"));
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

        public String getPrice(String product) {
            double price = calculatePrice(product);
            Discount.Code code = Discount.Code.values()[
                    new Random().nextInt(Discount.Code.values().length)];
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

        public List<String> findPrices(String product) {
            List<CompletableFuture<String>> priceFutures = shops.stream().map(shop ->
                    CompletableFuture.supplyAsync(() -> shop.getName() + "price is " + shop.getPriceAsync(product))).collect(Collectors.toList());
            return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
        }
    }

}
