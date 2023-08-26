package com.madm.leetcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * <p>
 * 请你设计并实现时间复杂度为O(n) 的算法解决此问题。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 示例 2：
 * <p>
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 */
public class Topic_128 {
    public int longestConsecutive(int[] nums) {
        Set<Integer> hash = new HashSet<Integer>(Arrays.stream(nums).boxed().collect(Collectors.toList()));
        for (int x : nums) hash.add(x);    //放入hash表中
        int res = 0;
        for (int x : hash) {
            if (!hash.contains(x - 1)) {
                int y = x;   //以当前数x向后枚举
                while (hash.contains(y + 1)) y++;
                res = Math.max(res, y - x + 1);  //更新答案
            }
        }
        return res;
    }
}
