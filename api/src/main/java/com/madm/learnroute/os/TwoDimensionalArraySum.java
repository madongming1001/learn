package com.madm.learnroute.os;

import static java.lang.System.currentTimeMillis;

public class TwoDimensionalArraySum {

    private static final int RUNS = 100;
    private static final int DIMENSION_1 = 1048576;
    private static final int DIMENSION_2 = 6;
    private static long[][] longs;

    public static void main(String[] args) throws Exception {
        longs = new long[DIMENSION_1][];
        for (int i = 0; i < DIMENSION_1; i++) {
            longs[i] = new long[DIMENSION_2];
            for (int j = 0; j < DIMENSION_2; j++) {
                longs[i][j] = 1L;
            }
        }
        System.out.println("Array初始化完毕....");

        long sum = 0L;
        long start = currentTimeMillis();
        for (int r = 0; r < RUNS; r++) {
            for (int i = 0; i < DIMENSION_1; i++) {//DIMENSION_1=1024*1024
                for (int j = 0; j < DIMENSION_2; j++) {//6
                    sum += longs[i][j];
                }
            }
        }
//        带有高速缓存的CPU执行计算的流程
//        1. 程序以及数据被加载到主内存
//        2. 指令和数据被加载到CPU的高速缓存
//        3. CPU执行指令，把结果写到高速缓存
//        4. 高速缓存中的数据写回主内存
//        CPU运行安全等级 CPU有4个运行级别，分别为:
//        ring0 内核态
//        ring1
//        ring2
//        ring3 用户态
//        Linux与Windows只用到了2个级别:ring0、ring3，操作系统内部内部程序指令通常运 行在ring0级别，
//        操作系统以外的第三方程序运行在ring3级别，第三方程序如果要调用操作 系统内部函数功能，
//        由于运行安全级别不够,必须切换CPU运行状态，从ring3切换到ring0, 然后执行系统函数，
//        说到这里相信同学们明白为什么JVM创建线程，线程阻塞唤醒是重型操 作了，因为CPU要切换运行状态。
//        下面我大概梳理一下JVM创建线程CPU的工作过程 step1:CPU从ring3切换ring0创建线程
        System.out.println("spend time1:" + (System.currentTimeMillis() - start));
        System.out.println("sum1:" + sum);
        sum = 0L;
        start = currentTimeMillis();
        for (int r = 0; r < RUNS; r++) {
            for (int j = 0; j < DIMENSION_2; j++) {//6
                for (int i = 0; i < DIMENSION_1; i++) {//1024*1024
                    sum += longs[i][j];
                }
            }
        }
        System.out.println("spend time2:" + (System.currentTimeMillis() - start));
        System.out.println("sum2:" + sum);
    }

}
