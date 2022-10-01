package com.madm.learnroute.javaee;

import com.mdm.utils.RandomGeneratorNumberUtil;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author dongming.ma
 * @date 2022/6/8 21:50
 */

/**
 * 特点：
 * 1、分裂的父线程不必等待子线程的结果，线程暂停
 * 2、工作窃取
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
            int mid = (first + last) >>> 1;
            ForkJoinPoolPractice left = new ForkJoinPoolPractice(first, mid, null);
            ForkJoinPoolPractice right = new ForkJoinPoolPractice(mid + 1, last, null);
            invokeAll(left, right);
            subCount = left.join();
            subCount += right.join();
        }
        return subCount;
    }

    public void setThreadShould(int threadShould) {
        this.threadShould = threadShould;
    }
}

class ForkJoinTestMain {
    public static void main(String[] args) {
        ForkJoinPoolPractice task = new ForkJoinPoolPractice(0, 999, RandomGeneratorNumberUtil.createArrayOf(1000));
        long startTime = System.currentTimeMillis();
        int result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join compare: " + result + " in " + (endTime - startTime) + " ms.");
    }
}
