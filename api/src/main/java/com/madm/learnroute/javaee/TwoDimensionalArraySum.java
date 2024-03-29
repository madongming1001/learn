package com.madm.learnroute.javaee;

/**
 * 二维数组为长✖️宽
 * 相当于是第一个参数个第二个参数
 */
public class TwoDimensionalArraySum {
    private static final int RUNS = 100;
    private static final int DIMENSION_1 = 1024 * 1024;//1048576
    private static final int DIMENSION_2 = 6;
    private static long[][] longs;

    public static void main(String[] args) throws Exception {
        /*
         * 初始化数组
         */
        longs = new long[DIMENSION_1][];
        for (int i = 0; i < DIMENSION_1; i++) {
            longs[i] = new long[DIMENSION_2];
            for (int j = 0; j < DIMENSION_2; j++) {
                longs[i][j] = 1L;
            }
        }
        System.out.println("Array初始化完毕....");

        long sum = 0L;
        //不符合空间局部性
        long start = System.currentTimeMillis();
        for (int r = 0; r < RUNS; r++) {
            for (int i = 0; i < DIMENSION_1; i++) {//DIMENSION_1=1024*1024
                for (int j = 0; j < DIMENSION_2; j++) {//6
                    sum += longs[i][j];
                }
            }
        }
        System.out.println("spend time1:" + (System.currentTimeMillis() - start));
        System.out.println("sum1:" + sum);

        sum = 0L;
        //符合空间局部性
        start = System.currentTimeMillis();
        for (int r = 0; r < RUNS; r++) {
            for (int j = 0; j < DIMENSION_2; j++) {//6
                for (int i = 0; i < DIMENSION_1; i++) {//1024*1024
                    sum += longs[i][j];
                }
            }
        }
        System.out.println("spend time2:" + (System.currentTimeMillis() - start));
        System.out.println("sum2:" + sum);

        System.out.println(longs.length);
        System.out.println(longs[0].length);
    }
}
