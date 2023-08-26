package com.madm.sort;

import java.util.Arrays;


/**
 * 快速排序 平均情况下快速排序的时间复杂度是Θ(𝑛log𝑛)
 * ，最坏情况是𝑛2
 * ，但通过随机算法可以避免最坏情况。由于递归调用，快排的空间复杂度是Θ(log𝑛)
 * 。
 * 快排优化
 * 1. 三数取中法
 * 2. 随机法
 * <p>
 * 快速排序就是每一轮挑选一个基准元素，并让其他比它大的元素移动到数列一边，
 * 比它小的元素移动到数列的另一边，从而把数列拆解成两部分
 * <p>
 * 算法描述
 * 从数列中挑出一个元素，称为"基准"（pivot），
 * 重新排序数列，所有比基准值小的元素摆放在基准前面，所有比基准值大的元素摆在基准后面（相同的数可以到任何一边）。在这个分区结束之后，该基准就处于数列的中间位置。这个称为分区（partition）操作。
 * 递归地（recursively）把小于基准值元素的子数列和大于基准值元素的子数列排序。
 * <p>
 * 稳定性
 * 快速排序并不是稳定的。这是因为我们无法保证相等的数据按顺序被扫描到和按顺序存放。
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] ints = {6, 1, 2, 7, 9};
//        int[] ints = {3, 6, 5, 3, 1, 4, 5, 6, 7};
//        int[] ints = {3, 5, 4, 7};
//        int[] ints = {4, 4, 4, 6, 6};
        quick_sort1(ints);
        System.out.println(Arrays.toString(ints));
    }

    private static void quick_sort1(int[] arr) {
        quick_sort1(arr, 0, arr.length - 1);
    }

    /**
     * 递归循环
     *
     * @param arr
     * @param begin
     * @param end
     */
    private static void quick_sort1(int[] arr, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int pivot = arr[begin];
        int left = begin;
        int right = end;
        while (left < right) {
            while (arr[right] >= pivot && right > left) {
                right--;
            }
            while (arr[left] <= pivot && left < right) {
                left++;
            }
            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        arr[begin] = arr[left];
        arr[left] = pivot;
        quick_sort1(arr, begin, left - 1);
        quick_sort1(arr, left + 1, end);
    }

    /**
     * 双边循环发
     *
     * @param arr
     * @param startIndex
     * @param endIndex
     */
    public static void quick_sort2(int[] arr, int startIndex, int endIndex) {
        if (startIndex >= endIndex) {
            return;
        }
        int pivotIndex = partition2(arr, startIndex, endIndex);
        quick_sort2(arr, startIndex, pivotIndex - 1);
        quick_sort2(arr, pivotIndex + 1, endIndex);
    }

    private static int partition2(int[] arr, int startIndex, int endIndex) {
        int pivot = arr[startIndex];
        int left = startIndex;
        int right = endIndex;
        while (left != right) {
            while (arr[right] > pivot && left < right) {
                right--;
            }
            while (arr[left] < pivot && left < right) {
                left++;
            }
            if (left < right) {
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        arr[startIndex] = arr[left];
        arr[left] = pivot;
        return left;
    }

    /**
     * 单边循环法
     *
     * @param arr
     * @param startIndex
     * @param endIndex
     */
    public static void quick_sort3(int[] arr, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            //得到基准元素位置
            int pivotIndex = partition3(arr, startIndex, endIndex);
            //根据基准元素，分成两部分进行递归排序
            quick_sort3(arr, startIndex, pivotIndex - 1);
            quick_sort3(arr, pivotIndex + 1, endIndex);
        }
    }

    private static int partition3(int[] arr, int startIndex, int endIndex) {
        int pivot = arr[startIndex];
        int mark = startIndex;
        for (int i = startIndex + 1; i <= endIndex; i++) {
            if (arr[i] < pivot) {
                mark++;
                int p = arr[mark];
                arr[mark] = arr[i];
                arr[i] = p;
            }
        }
        arr[startIndex] = arr[mark];
        arr[mark] = pivot;
        return mark;
    }
}
