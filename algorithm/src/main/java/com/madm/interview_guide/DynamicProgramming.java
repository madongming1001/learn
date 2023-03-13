package com.madm.interview_guide;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * 动态规划有三个核心元素：
 * 1、最优子结构
 * 2、状态转移方程
 * 3、边界
 *
 * @author dongming.ma
 * @date 2023/3/13 23:35
 */
public class DynamicProgramming {

    public static void main(String[] args) {
        Integer[] ints = Arrays.stream(ArrayUtil.range(1, 100)).boxed().toArray(Integer[]::new);
        BigDecimal summarize = NumberUtil.add(ints);
        System.out.println(summarize);
        int w = 10;
        int[] g = {400, 500, 200, 300, 350};
        int[] p = {5, 5, 3, 4, 3};
        System.out.println("最优收益：" + getBestGoldMining(g.length, w, g, p));
    }

    /**
     * @param n 金矿数量
     * @param w 工人数量
     * @param g 金矿含金量
     * @param p 金矿所需开采人员
     */
    public static int getBestGoldMining(int n, int w, int[] g, int[] p) {
        if (n == 0 || w == 0) {
            return 0;
        }
        if (w < p[n - 1]) {
            return getBestGoldMining(n - 1, w, g, p);
        }
        return Math.max(getBestGoldMining(n - 1, w, g, p), getBestGoldMining(n - 1, w - p[n - 1], g, p) + g[n - 1]);
    }
}
