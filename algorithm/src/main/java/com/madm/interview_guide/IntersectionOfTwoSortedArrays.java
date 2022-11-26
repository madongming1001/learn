package com.madm.interview_guide;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 两个有序数组求交集（4种方式）
 （1）每次从数组 b 中 取一个数，遍历 a 中所有元素进行比较，若相同就保存，这样时间复杂度为O(m*n)；
 （2）由于数组是有序的，每次从数组 b 中 取一个数，对 a 中元素进行二分查找，若相同就保存，时间复杂度为O(nlog(m))；
 （3）将 a 中的元素 hash 存储（用map或者dict），遍历 b 中的每一个值看是否在这个hash 中，若存在就保存，时间复杂度是 O(m)，空间复杂度是O(n)；
 （4）a 和 b 两个数组的头部分别维护两个指针，若其中一个比另一个小，则向前移动，若遇到相等时保存，遍历直到其中一个数组的尾部，时间复杂度O(m+n)；
 *
 * @author dongming.ma
 * @date 2022/7/11 21:52
 */
public class IntersectionOfTwoSortedArrays {

    public static void main(String[] args) {
        int[] arr1 = {-1,2,4,7};
        int[] arr2 = {-1,3,6,7};
        int[] ints = solution4(arr1, arr2);
        System.out.println(Arrays.toString(ints));
    }

    private static int[] solution4(int[] arr1, int[] arr2) {
        HashSet<Integer> ansset = Sets.newHashSet();
        int p1 = arr1.length;
        int p2 = arr2.length;
        int i = 0,j = 0,p = 0;
        while(i < p1 && j < p2){
            if (arr1[i] == arr2[j]) {
                ansset.add(arr1[i]);
                i++;
                j++;
            }else if(arr1[i] < arr2[j]){
                i++;
            }else if(arr1[i] > arr2[j]){
                j++;
            }
        }
        return ansset.stream().mapToInt(Integer::intValue).toArray();
    }
}
