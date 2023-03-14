package com.madm.sort;

import java.util.Arrays;

/**
 * @author dongming.ma
 * @date 2023/3/14 13:08
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] arr = {5, 3, 9, 12, 6, 1, 7, 2, 4, 11, 8, 10};
        shellSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void shellSort(int[] arr) {
        int len = arr.length;
        while (len > 1) {
            len = len >> 1;
            for (int i = 0; i < len; i++) {
                for (int j = i + len; j < arr.length; j = j + len) {
                    int temp = arr[j];
                    int k;
                    for (k = j - len; (k >= 0) && (arr[k] > temp); k = k - len) {
                        arr[k + len] = arr[k];
                    }
                    arr[k + len] = temp;
                }
            }
        }
    }
}
