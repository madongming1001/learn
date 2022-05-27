package madm.leetcode;

import java.util.HashMap;

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
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) {
            return 0;
        }
        HashMap<Character, Integer> dist = new HashMap();
        int max = 0, left = 0;
        for (int i = 0; i < s.length(); i++) {
            if (dist.containsKey(s.charAt(i))) {
                left = Math.max(left, dist.get(s.charAt(i)) + 1);
            }
            dist.put(s.charAt(i), i);
            max = Math.max(max, i - left + 1);
        }
        return max;
    }

    public static void main(String[] args) {
        Topic_03 tp = new Topic_03();
        System.out.println(tp.lengthOfLongestSubstring("dvdf"));
    }
}
