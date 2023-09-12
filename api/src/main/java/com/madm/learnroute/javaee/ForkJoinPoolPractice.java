package com.madm.learnroute.javaee;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author dongming.ma
 * @date 2022/6/8 21:50
 */

/**
 * 特点：
 * 1、分裂的父线程不必等待子线程的结果，线程暂停
 * 2、工作窃取,每个线程都有自己的工作队列，对于工作线程来说执行任务是后进先出，窃取线程执行任务是先进先出
 */
@Data
public class ForkJoinPoolPractice extends RecursiveTask<Integer> {

    private static int[] dou;
    private int first;
    private int last;
    private int threadShould = 10;
    public ForkJoinPoolPractice(int first, int last, @Nullable int[] dou) {
        this.first = first;
        this.last = last;
        if (Objects.nonNull(dou)) {
            this.dou = dou;
        }
    }

    public static void main(String[] args) {
        ForkJoinPoolPractice task = new ForkJoinPoolPractice(0, 999, RandomUtil.randomInts(1000));
        long startTime = System.nanoTime();
        ForkJoinTask<Integer> submit = ForkJoinPool.commonPool().submit(task);
        long endTime = System.nanoTime();
        System.out.println("Forkjoin compare: " + submit.join() + " in " + (endTime - startTime) / 1000 / 1000 + " ms.");


        test();
    }

    public static void test() {
        //声明数据源集合
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            //添加100个元素到集合中
            list.add(i);
        }
        //添加数据的集合,使用CopyOnWriteArrayList替换ArrayList
        List<Integer> list2 = new CopyOnWriteArrayList<>();
        //使用parallelStream的遍历方法来添加元素到新的集合
        list.parallelStream().forEach(i -> {
            list2.add(i);
        });
        //打印添加元素之后的集合长度
        System.out.println(list2.size());
    }


    @Override
    protected Integer compute() {
        int subCount = 0;
        if (last - first < threadShould) {
            for (int i = first; i <= last; i++) {
                try {
                    if (dou[i] > 500) {
                        subCount++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return subCount;
        } else {
            int mid = (first + last) >> 1;
            ForkJoinPoolPractice left = new ForkJoinPoolPractice(first, mid, null);
            ForkJoinPoolPractice right = new ForkJoinPoolPractice(mid + 1, last, null);
            invokeAll(left, right);
            subCount = left.join();
            subCount += right.join();
        }
        return subCount;
    }

}