package com.madm.leetcode;

public class Topic_Niuke_140 {
    public static void main(String[] args) {
        Topic_Niuke_140 t = new Topic_Niuke_140();
        int[] ints = {5, 2, 3, 1, 4};
        t.group(ints, 0, ints.length - 1);
    }

    public int[] group(int[] arr, int start, int end) {
        if (start < end) {
            int mid = start + ((end - start) >> 1);
            group(arr, start, mid);
            group(arr, mid + 1, end);
            mergeSort(arr, start, mid, end);
        }
        return arr;
    }

    public void mergeSort(int[] arr, int start, int mid, int end) {
        int[] cond = new int[end - start + 1];
        int p1 = start;
        int p2 = mid;
        int p = 0;
        while (p1 <= mid && p2 <= end) {
            if (arr[p1] <= arr[p2]) {
                cond[p++] = arr[p1++];
            } else {
                cond[p++] = arr[p2++];
            }
        }
        while (p1 <= mid) cond[p1++] = arr[p1];
        while (p2 <= end) cond[p2++] = arr[p2];
        for (int i = 0; i < cond.length; i++) {
            arr[i + start] = cond[i];
        }
    }
}
