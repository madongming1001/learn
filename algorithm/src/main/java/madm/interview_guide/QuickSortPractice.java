package madm.interview_guide;

import java.util.Arrays;


/**
 * 快排优化
 * 1. 三数取中法
 * 2. 随机法
 */
public class QuickSortPractice {
    public static void main(String[] args) {
//        int[] ints = {6, 1, 2, 7, 9};
//        int[] ints = {7, 6, 5, 3, 1, 4, 5, 6, 7};
        int[] ints = {3,5,4,7};
        quick_sort(ints, 0, ints.length - 1);
        Arrays.stream(ints).forEach(System.out::println);
    }

    private static void quick_sort(int[] arr, int begin, int end) {
        if (begin > end) {
            return;
        }
        int pivot = arr[begin];
        int left = begin;
        int right = end;
        while (left < right) {
            while (arr[left] <= pivot && left < right) {
                left++;
            }
            while (arr[right] >= pivot && right > left) {
                right--;
            }
            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        arr[begin] = arr[left];
        arr[left] = pivot;
        quick_sort(arr, begin, left - 1);
        quick_sort(arr, left + 1, end);
    }
}
