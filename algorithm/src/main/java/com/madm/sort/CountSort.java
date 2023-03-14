package com.madm.sort;

import java.util.Arrays;

/**
 * 计数排序
 * 适用于一定范围内的整数排序，在取值范围不是很大的情况下，它的性能甚至快过那些时间复杂度为O(nlogn)的排序
 * <p>
 * 局限性
 * 1、当数列最大值和最小值差距过大时，并不适用计数排序
 * 2、当数列元素不是整数时，也不适合用计数排序
 *
 * @author dongming.ma
 * @date 2023/3/14 14:44
 */
public class CountSort {
    public static void main(String[] args) {
        int[] arr = new int[]{4, 4, 6, 5, 3, 2, 8, 1, 7, 5, 6, 0, 10};
        int[] sortedArr = countSort(arr);
        System.out.println(Arrays.toString(sortedArr));

        int[] optimizerArr = {95, 94, 91, 98, 99, 90, 99, 91, 93, 92};
        int[] sortedOptimizerArr = countSortOptimizerStabilize(optimizerArr);
        System.out.println(Arrays.toString(sortedOptimizerArr));
    }

    public static int[] countSort(int[] arr) {
        //1.得到数列的最大值
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        //2.根据数列最大值确定统计数组的长度
        int[] countArr = new int[max + 1];
        //3.遍历数列，填充统计数组
        for (int i = 0; i < arr.length; i++) {
            countArr[arr[i]]++;
        }
        //4.遍历统计数组，输出结果
        int index = 0;
        int[] sortedArr = new int[arr.length];
        for (int i = 0; i < countArr.length; i++) {
            for (int j = 0; j < countArr[i]; j++) {
                sortedArr[index++] = i;
            }
        }
        return sortedArr;
    }

    /**
     * 计数排序优化稳定版本
     *
     * @param arr
     * @return
     */
    public static int[] countSortOptimizerStabilize(int[] arr) {
        //1.得到数列的最大值和最小值，并算出差值d
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        int d = max - min;
        //2.创建统计数组并统计对应元素的个数
        int[] countArr = new int[d + 1];
        for (int i = 0; i < arr.length; i++) {
            countArr[arr[i] - min]++;
        }

        //3.统计数组做变形，后面的元素等于前面的元素之和
        for (int i = 1; i < countArr.length; i++) {
            countArr[i] += countArr[i - 1];
        }
        //4.倒序遍历原始数列，从统计数组找到正确位置，输出到结果数组
        int[] sortedArr = new int[arr.length];
        for (int i = arr.length - 1; i >= 0; i--) {
            sortedArr[countArr[arr[i] - min] - 1] = arr[i];
            countArr[arr[i] - min]--;
        }
        return sortedArr;
    }
}
