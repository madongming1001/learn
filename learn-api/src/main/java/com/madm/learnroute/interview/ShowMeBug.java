package com.madm.learnroute.interview;

public class ShowMeBug {
    public static void main(String[] args) {
        String line = "sa12cdsafasf";
        System.out.println(search(line));
    }

    private static Object search(String line) {
        char[] chars = line.toCharArray();
        // 于上处第一点分析，256长度足以。
        int[] charArray = new int[256];
        // 1、计算出现字符次数
        for (char c : chars) {
            charArray[(int) c]++;
        }
        // 2、按照字符出现顺序查找出现次数，返回第一个为1的字符。
        for (char c : chars) {
            if (charArray[(int) c] == 1) {
                return c;
            }
        }
        return 0;
    }


}
