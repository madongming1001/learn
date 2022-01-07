package com.madm.learnroute.javaee;

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
