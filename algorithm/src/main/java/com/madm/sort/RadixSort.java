package com.madm.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 基数排序
 * 排序工作拆分成多个阶段，每一个阶段只根据一个字符进行计数排序，一共排序k轮（可是字符串长度）
 * 像这样把字符串元素按位拆分，每一位进行一次计数排序的算法，就是基数排序（Radix Sort）
 * 基数排序既可以从高位优先进行排序（Most Significant Digit first，简称MSD），也可
 * 以从低位优先进行排序（Least Significant Digit first，简称LSD）
 *
 * @author dongming.ma
 * @date 2023/3/14 18:31
 */
public class RadixSort {
    //ASCII码的取值范围
    public static final int ASCII_RANGE = 128;

    public static void main(String[] args) {
        String[] arr = {"bda", "cfd", "qwe", "yui", "abc", "rrr", "uue"};
        System.out.println(Arrays.toString(radixSortLSD(arr, 3)));
        List<String> list = Arrays.asList(arr);
        Collections.sort(list, String::compareTo);//升序
        Collections.sort(list, Comparator.comparing(String::valueOf));//升序
        System.out.println(list);
    }

    public static String[] radixSortLSD(String[] arr, int maxLength) {
        //排序结果数组，用于存储每一次按位排序的临时结果
        String[] sortedArr = new String[arr.length];
        //从个位开始比较，一直比较到最高位
        for (int i = maxLength - 1; i >= 0; i--) {
            //计数排序的过程，分为3步：
            //1.创建辅助排序的统计数组，并把带排序的字符对号入座，
            //这里为了代码简洁，直接使用ASCII码范围作为数组长度
            int[] count = new int[ASCII_RANGE];
            for (int j = 0; j < arr.length; j++) {
                int index = charIndex(arr[j], i);
                count[index]++;
            }
            //2.统计数组做变形，后面的元素等于前面的元素之和
            for (int j = 1; j < count.length; j++) {
                count[j] = count[j] + count[j - 1];
            }
            //3.倒序遍历原始数列，从统计数组找到正确位置，输出到结果数组
            for (int j = arr.length - 1; j >= 0; j--) {
                int index = charIndex(arr[j], i);
                int sortedIndex = count[index] - 1;
                sortedArr[sortedIndex] = arr[j];
                count[index]--;
            }
            //下一轮排序需要以上一轮的排序结果为基础，因此把结果复制给arr
            arr = sortedArr.clone();
        }
        return arr;
    }

    private static int charIndex(String str, int j) {
        //如果字符串长度小于j，直接返回0，相当于给不存在的位置补0
        if (str.length() < (j + 1)) return 0;
        return str.charAt(j);
    }

}
