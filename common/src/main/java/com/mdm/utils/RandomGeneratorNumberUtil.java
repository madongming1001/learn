package com.mdm.utils;

public class RandomGeneratorNumberUtil {

    public static int[] createArrayOf(int size) {
        int[] result = new int[size];
        int max = 0;
        for (int i = 0; i < size; i++) {
            result[i] = (int) (Math.random() * size) + 1;
            if (max < result[i]) {
                max = result[i];
            }
        }
        return result;
    }
}
