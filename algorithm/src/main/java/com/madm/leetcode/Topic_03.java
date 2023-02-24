package com.madm.leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
 * <p>
 * <p>
 * 示例1:
 * <p>
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 */
public class Topic_03 {

    public int lengthOfLongestSubstring01(String str) {
        int left = 0, right = 0, max = 0, len = 0;
        Set<Character> set = new HashSet();
        while (right < str.length()) {
            if (!set.contains(str.charAt(right))) {
                set.add(str.charAt(right++));
                if (++len > max) {
                    max = len;
                }
            } else {
                while (set.contains(str.charAt(right))) {
                    set.remove(str.charAt(left));
                    left++;
                    len--;
                }
                set.add(str.charAt(right));
                len++;
                right++;
            }
        }
        return max;
    }

    public int lengthOfLongestSubstring02(String s) {
        HashMap<Character, Integer> map = new HashMap<>();
        int max = 0, start = 0;
        for (int end = 0; end < s.length(); end++) {
            char ch = s.charAt(end);
            if (map.containsKey(ch)) {
                start = Math.max(map.get(ch) + 1, start);//更新滑动窗口开始位置
            }
            max = Math.max(max, end - start + 1);//最少是一位
            map.put(ch, end);
        }
        return max;
    }

    public int lengthOfLongestSubstring03(String s) {
        // 记录字符上一次出现的位置
        int[] last = new int[128];
        Arrays.fill(last, -1);
        int max = 0;
        int start = 0; // 窗口开始位置
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i);
            start = Math.max(start, last[index] + 1);//上一次字符出现的位置
            max = Math.max(max, i - start + 1);
            last[index] = i;
        }

        return max;
    }

    public static void main(String[] args) {
        Topic_03 tp = new Topic_03();
        System.out.println(tp.lengthOfLongestSubstring01("abcabcbb"));
        System.out.println(tp.lengthOfLongestSubstring02("abcabcbb"));
        System.out.println(tp.lengthOfLongestSubstring03("abcabcbb"));//最优
    }
}
