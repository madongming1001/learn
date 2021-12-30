package com.madm.learnroute.algorithm;

import com.madm.learnroute.util.PrintUtils;

public class MergeSortPractice {
    public static void main(String[] args) {
//        int[] arr = {5,8,6,3,9,2,1,7};
        int[] arr = {5, 2, 3, 1, 4};
        group(arr, 0, arr.length - 1);
        PrintUtils.printArray(arr);
    }

    public static int[] group(int[] arr, int start, int end) {
        if (start < end) {
            int mid = start + ((end - start) >> 1);
            group(arr, start, mid);
            group(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
        return arr;
    }

    public static void merge(int[] array, int start, int mid, int end) {
        int[] cond = new int[end - start + 1];
        int p1 = start;
        int p2 = mid + 1;
        //他是数组的地址
        int p = 0;
        while (p1 <= mid && p2 <= end) {
            if (array[p1] <= array[p2]) {
                cond[p++] = array[p1++];
            } else {
                cond[p++] = array[p2++];
            }
        }
        while (p1 <= mid) cond[p++] = array[p1++];
        while (p2 <= end) cond[p++] = array[p2++];
        for (int i = 0; i < cond.length; i++) {
            array[i + start] = cond[i];
        }
    }
}
