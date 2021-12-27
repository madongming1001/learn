package madm.data_structure.leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 请从字符串中找出一个最长的不包含重复字符的子字符串，计算该最长子字符串的长度。
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 */
public class Topic_48 {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>(); //存放窗口状态
        int n = s.length();
        int start = 0, end = 0; //窗口左闭右开
        int ans = 0;
        while (end < n) {
            char c = s.charAt(end); //当前元素
            if (!set.contains(c)) { //当前元素可添加，则窗口扩张，同时更新最大长度
                set.add(c);
                end++;
                ans = Math.max(ans, end - start);
            } else {
                while (set.contains(c)) { //窗口一直收缩到可添加当前元素
                    set.remove(s.charAt(start++));
                }
            }
        }
        return ans;
    }
}
