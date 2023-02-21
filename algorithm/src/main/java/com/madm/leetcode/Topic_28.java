package com.madm.leetcode;

/**
 * 28. 找出字符串中第一个匹配项的下标
 * 给你两个字符串 haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。如果 needle 不是 haystack 的一部分，则返回  -1 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：haystack = "sadbutsad", needle = "sad"
 * 输出：0
 * 解释："sad" 在下标 0 和 6 处匹配。
 * 第一个匹配项的下标是 0 ，所以返回 0 。
 * 示例 2：
 * <p>
 * 输入：haystack = "leetcode", needle = "leeto"
 * 输出：-1
 * 解释："leeto" 没有在 "leetcode" 中出现，所以返回 -1 。
 *
 * @author dongming.ma
 * @date 2023/2/21 11:15
 */
public class Topic_28 {
    public static int rabinKarp(String str, String pattern) {
        //主串长度
        int m = str.length();
        //模式串的长度
        int n = pattern.length();
        //计算模式串的hash值
        int patternCode = hash(pattern);
        //计算主串当中第一个和模式串等长的子串hash值
        int strCode = hash(str.substring(0, n));
        //用模式串的hash值和主串的局部hash值比较。
        //如果匹配，则进行精确比较；如果不匹配，计算主串中相邻子串的hash值。
        for (int i = 0; i < (m - n + 1); i++) {
            if ((strCode == patternCode) && compareString(i, str, pattern)) {
                return i;
            }
            //如果不是最后一轮，更新主串从i到i+n的hash值
            if (i < (m - n)) {
                strCode = nextHash(str, strCode, i, n);
            }
        }
        return -1;
    }

    private static int hash(String str) {
        int hashcode = 0;
        //这里采用最简单的hashcode计算方式：
        //把a当做1，把b当中2，把c当中3.....然后按位相加
        for (int i = 0; i < str.length(); i++) {
            hashcode += (str.charAt(i) - 'a');
        }
        return hashcode;
    }

    private static int nextHash(String str, int hash, int index, int n) {
        hash -= (str.charAt(index) - 'a');
        hash += (str.charAt(index + n) - 'a');
        return hash;
    }

    private static boolean compareString(int i, String str, String pattern) {
        String strSub = str.substring(i, i + pattern.length());
        return strSub.equals(pattern);
    }

    public static void main(String[] args) {
        String str = "aacdesadsdfer";
        String pattern = "adsd";
        System.out.println("第一次出现的位置:" + rabinKarp(str, pattern));
    }
}