package com.madm.sort;

import java.util.Arrays;

public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {5, 8, 6, 3, 9, 2, 1, 7};
//        int[] arr = {5, 2, 3, 1, 4};
        group(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void merge2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int step = 1;
        int N = arr.length;
        while (step < N) {
            int L = 0;
            while (L < N) {
                int M;
                if (N - L >= step) {
                    M = L + step - 1;
                } else {
                    M = N - 1;
                }
                if (M == N - 1) {
                    break;
                }
                int R;
                if (N - 1 - M >= step) {
                    R = M + step;
                } else {
                    R = N - 1;
                }
                merge(arr, L, M, R);
                if (R == N - 1) {
                    break;
                } else {
                    L = R + 1;
                }
                if (step > N / 2) {
                    break;
                }
                step *= 2;
            }
        }
    }


    public static int[] group(int[] arr) {
        return group(arr, 0, arr.length - 1);
    }

    private static int[] group(int[] arr, int start, int end) {
        if (start < end) {
            //middle
            //int mid = (start + end) / 2;
            int mid = start + ((end - start) >> 1);
            group(arr, start, mid);
            group(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
        return arr;
    }

    private static void merge(int[] arr, int start, int mid, int end) {
        int[] help = new int[end - start + 1];//传的是下标位置所以需要加1
        int p = 0;
        int p1 = start;
        int p2 = mid + 1;
        while (p1 <= mid && p2 <= end) {
            help[p++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) help[p++] = arr[p1++];
        while (p2 <= end) help[p++] = arr[p2++];
        //把排完序的数值写回原数组
        for (int i = 0; i < help.length; i++) {
            arr[i + start] = help[i];
        }
    }
}
