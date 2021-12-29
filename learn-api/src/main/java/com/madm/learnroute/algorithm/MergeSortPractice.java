package com.madm.learnroute.algorithm;

import com.madm.learnroute.util.PrintUtils;

public class MergeSortPractice {
    public static void main(String[] args) {
//        int[] arr = {5,8,6,3,9,2,1,7};
        int[] arr = {7, 6, 5, 3, 1, 4, 5, 6, 7};
        sort(arr, 0, arr.length - 1);
        PrintUtils.printArray(arr);
    }

    public static void sort(int[] array, int start, int end) {
        if (start < end) {
            int mid = start + ((end - start) >> 1);
            sort(array, start, mid);
            sort(array, mid + 1, end);
            mergeSort(array, start, mid, end);
        }
    }

    /**
     * 在排序好的基础上进行合并
     *
     * @param array
     * @param start
     * @param mid
     * @param end
     */
    private static void mergeSort(int[] array, int start, int mid, int end) {
        int[] temp = new int[end - start + 1];
        int p1 = start;
        int p2 = mid + 1;
        int p = 0;
        while (p1 <= mid && p2 <= end) {
            if (array[p1] <= array[p2]) {
                temp[p++] = array[p1++];
            } else {
                temp[p++] = array[p2++];
            }
        }
        while (p1 <= mid) temp[p++] = array[p1++];
        while (p2 <= end) temp[p++] = array[p2++];
        for (int i = 0; i < temp.length; i++) {
            array[i + start] = temp[i];
        }
    }
}
