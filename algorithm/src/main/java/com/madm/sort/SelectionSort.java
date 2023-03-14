package com.madm.sort;

/**
 * 选择排序 O(n2)复杂度
 * 算法描述
 * 1、在未排序序列中找到最小（大）元素，存放到排序序列的起始位置
 * 2、从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
 * 3、重复第二步，直到所有元素均排序完毕。
 *
 * 稳定性
 * 用数组实现的选择排序是不稳定的，用链表实现的选择排序是稳定的。
 * 不过，一般提到排序算法时，大家往往会默认是数组实现，所以选择排序是不稳定的。
 *
 * @author dongming.ma
 * @date 2023/3/14 11:42
 */
public class SelectionSort {
    public static void selectionSort(int[] arr) {
        int temp, min = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            min = i;
            // 循环查找最小值
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[min] > arr[j]) {
                    min = j;
                }
            }
            if (min != i) {
                temp = arr[i];
                arr[i] = arr[min];
                arr[min] = temp;
            }
        }
    }
}
