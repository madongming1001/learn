package com.madm.sort;

import java.util.Arrays;

import static cn.hutool.core.util.ArrayUtil.swap;

/**
 * 第一轮从左到右排序遍历一遍
 * 第二轮从右到左排序遍历一遍
 * 第三轮从左到右再排序遍历一遍
 * 在大部分元素已经有序的情况下
 *
 * @author dongming.ma
 * @Code int[] arr = {2,3,4,5,6,7,8,1};
 * <p>
 * <p>
 * 鸡尾酒排序的优点是能够在特定条件下，减少排序的回合数；而缺点也很明显，就是代码量几乎增加了一倍
 * 发挥场景就是大部分元素已经有序的情况下
 * @date 2023/3/13 22:14
 */
public class CocktailSort {

    public static void main(String[] args) {
        int[] ints = {2, 3, 4, 5, 6, 7, 8, 1};
        cocktail(ints);
        System.out.println(Arrays.toString(ints));

//        cocktailOptimizer(ints);
//        System.out.println(Arrays.toString(ints));
    }

    public static void cocktail(int[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            //有序标记，每一轮的初始是true
            boolean isSorted = true;
            //奇数轮，从左向右比较和交换
            for (int j = i; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    //有元素交换，所以不是有序，标记变为false
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
            //偶数轮之前，重新标记为true
            isSorted = true;
            //偶数轮，从右向左比较和交换
            for (int j = arr.length - i - 1; j > i; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                    //有元素交换，所以不是有序，标记变为false
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
    }

    private static void cocktailOptimizer(int arr[]) {
        int tmp = 0;
        //记录右侧最后一次交换的位置
        int lastRightExchangeIndex = 0;
        //记录左侧最后一次交换的位置
        int lastLeftExchangeIndex = 0;
        //无序数列的右边界，每次比较只需要比到这里为止
        int rightSortBorder = arr.length - 1;
        //无序数列的左边界，每次比较只需要比到这里为止
        int leftSortBorder = 0;
        for (int i = 0; i < arr.length / 2; i++) {
            //有序标记，每一轮的初始是true
            boolean isSorted = true;
            //奇数轮，从左向右比较和交换
            for (int j = leftSortBorder; j < rightSortBorder; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                    //有元素交换，所以不是有序，标记变为false
                    isSorted = false;
                    lastRightExchangeIndex = j;
                }
            }
            rightSortBorder = lastRightExchangeIndex;
            if (isSorted) {
                break;
            }
            //偶数轮之前，重新标记为true
            isSorted = true;
            //偶数轮，从右向左比较和交换
            for (int j = rightSortBorder; j > leftSortBorder; j--) {
                if (arr[j] < arr[j - 1]) {
                    swap(arr, j, j - 1);
                    //有元素交换，所以不是有序，标记变为false
                    isSorted = false;
                    lastLeftExchangeIndex = j;
                }
            }
            leftSortBorder = lastLeftExchangeIndex;
            if (isSorted) {
                break;
            }
        }
    }
}
