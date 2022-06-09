package com.madm.learnroute.javaee;

import java.util.Arrays;

/**
 * @author dongming.ma
 * @date 2022/6/8 23:00
 */
public class BitMapPractice {
    public static void main(String[] args) {
        int[] arr = new int[]{6, 2, 7, 14, 3};
        int maxArr = Arrays.stream(arr).max().getAsInt();
        char[] chars = new char[14 / 16 + 1];
        System.out.println(chars.length);
    }
}
