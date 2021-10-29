package com.example.learnroute.javaee;

import java.util.Arrays;

public class QuickSortPractice {
    public static void main(String[] args) {
        int[] ints = {6, 1, 2, 7, 9, 3, 4, 5, 10, 8};
        quick_sort(ints, 0, ints.length - 1);
        Arrays.stream(ints).forEach(System.out::print);
    }

    private static void quick_sort(int[] arr, int begin, int end) {
        if (begin > end) {
            return;
        }
        int tempbegin = arr[begin];
        int i = begin;
        int j = end;
        while (i < j) {
            while (arr[j] >= tempbegin && i < j) {
                j--;
            }
            while (arr[i] <= tempbegin && i < j) {
                i++;
            }
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        arr[begin] = arr[i];
        arr[i] = tempbegin;
        quick_sort(arr, begin, i - 1);
        quick_sort(arr, i + 1, end);
    }
}
