package com.madm.leetcode;

/**
 * 求最大公约数
 *
 * @author dongming.ma
 * @date 2023/8/18 12:27
 */
public class GreatestCommonDivisor {
    //辗转相除法，又叫作欧几里得算法
    //两个正整数a和b（a>b）,它们的最大公约数等于a除以b的余数c和b之间的最大公约数
    public static int getGreatestCommonDivisorV2(int a, int b) {
        int big = a > b ? a : b;
        int small = a < b ? a : b;
        if (big % small == 0) {
            return small;
        }
        return getGreatestCommonDivisorV2(big % small, small);
    }

    //更相减损法
    //两个正整数a和b（a>b），它们的最大公约数等于a-b的差值c和较小数b的最大公约数
    //更相减损法是不稳定的算法，当两数相差悬殊时，如计算10000和1的最大公约数，就要递归9999次
    public static int getGreatestCommonDivisorV3(int a, int b) {
        if (a == b) return a;
        int big = a > b ? a : b;
        int small = a < b ? a : b;
        return getGreatestCommonDivisorV2(big - small, small);
    }

    //even 偶数 odd 奇数
    //辗转相除法和更相减损法合并
    public static int getGreatestCommonDivisorV4(int a, int b) {
        if (a == b) return a;
        if (even(a) && even(b)) {
            return getGreatestCommonDivisorV4(a >> 1, b >> 1) << 1;
        } else if (even(a) && odd(b)) {
            return getGreatestCommonDivisorV4(a >> 1, b);
        } else if (odd(a) && even(b)) {
            return getGreatestCommonDivisorV4(a, b >> 1);
            //都为奇数的情况
        } else {
            int big = a > b ? a : b;
            int small = a < b ? a : b;
            return getGreatestCommonDivisorV4(big - small, small);
        }
    }

    /**
     * 偶数判断
     */
    public static boolean even(int num) {
        return (num & 1) == 0;
    }

    /**
     * 偶数判断
     */
    public static boolean odd(int num) {
        return (num & 1) != 0;
    }

    public static void main(String[] args) {
        System.out.println(getGreatestCommonDivisorV2(25, 5));
        System.out.println(getGreatestCommonDivisorV2(100, 80));
        System.out.println(getGreatestCommonDivisorV4(10, 10));
    }
}
