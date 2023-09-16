package com.madm.interview_guide;

import cn.hutool.core.math.Combination;
import cn.hutool.core.util.NumberUtil;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author dongming.ma
 * @date 2023/9/16 19:14
 */
public class CombinationPractice {
    public static void main(String[] args) {
        int n = 4;
        int count = countCombinations(n, 4);
        System.out.println("从1到" + n + "中，不定位数的组合共有：" + count + "种");
        Combination combination = new Combination(Arrays.stream(NumberUtil.range(1, 10)).mapToObj(Integer::toString).toArray(String[]::new));
//        combination.selectAll().forEach(x -> {
//            for (String s : x) {
//                System.out.print(s);
//            }
//            System.out.println();
//        });
        System.out.println(combination.selectAll().size());
        System.out.println(combination.count(10, 2));
        System.out.println(combination.count(10, 8));
        Integer[] integers = new Integer[0];
        Integer[] array = IntStream.rangeClosed(1, 100).mapToObj(Integer::valueOf).toArray(Integer[]::new);
    }

    //递归法
    private static int countCombinations(int n, int k) {
        if (k == 0 || k == n) return 1;
        else {
            return countCombinations(n - 1, k) + countCombinations(n - 1, k - 1);
        }
    }
}
