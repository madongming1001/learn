package com.madm.learnroute.javaee;

/**
 * 局部性原理
 * 为了提高效率，要尽量减少磁盘I/O。为了达到这个目的，磁盘往往不是严格按需读取，
 * 而是每 次都会预读，即使只需要一个字节，磁盘也会从这个位置开始，顺序向后读取一定长度的数据 放入内存，
 * 这个称之为预读。这样做的理论依据是计算机科学中著名的局部性原理： 当一个数据被用到时，其附近的数据也通常会马上被使用。
 */
public class LoopArray {

    public static void main(String[] args) {
        int[][] arr = new int[10000][10000];
        int sum = 0;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                sum += arr[i][j];
            }
        }
        System.out.println("按行耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        sum = 0;
        startTime = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                sum += arr[j][i];
            }
        }
        System.out.println("按列耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
