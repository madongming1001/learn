package com.madm.learnroute.javaee;

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
public class ForkJoinPoolPractice {
    private static double[] d;

    private class ForkJoinTask extends RecursiveTask<Integer> {
        private int first;
        private int last;

        public ForkJoinTask(int first, int last) {
            this.first = first;
            this.last = last;
        }

        @Override
        protected Integer compute() {
            int subCount = 0;
            if (last - first < 10) {
                for (int i = first; i <= last; i++) {
                    if (d[i] < 0.5) {
                        subCount++;
                    }
                }
                return subCount;
            } else {
                int mid = (first + last) >>> 1;
                ForkJoinTask left = new ForkJoinTask(first, mid);
                left.fork();
                ForkJoinTask right = new ForkJoinTask(mid + 1, last);
                right.fork();
                subCount = left.join();
                subCount += right.join();
            }
            return subCount;
        }
    }
}
