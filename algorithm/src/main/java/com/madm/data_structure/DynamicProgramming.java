package com.madm.data_structure;

/**
 * 背包问题 有限背包容量下 找到最多装多少件物品 物品n 重量各不相同
 */
public class DynamicProgramming {

    // 回溯算法实现。注意：我把输入的变量都定义成了成员变量。
    private int maxW = Integer.MIN_VALUE; // 结果放到maxW中
    private int[] weight = {2, 2, 4, 6, 3};  // 物品重量
    private int n = 5; // 物品个数
    private int w = 9; // 背包承受的最大重量
    private boolean[][] mem = new boolean[5][10];//备忘录,默认值false

    /**
     * 回溯算法+剪枝+备忘录
     */
    public void f(int i, int cw) { // 调用f(0, 0)
        if (cw == w || i == n) { // cw==w表示装满了,i==n表示物品都考察完了
            if (cw > maxW) maxW = cw;
            return;
        }
        if (mem[i][cw]) return; // 重复状态
        mem[i][cw] = true; // 记录(i, cw)这个状态
        f(i + 1, cw); // 选择不装第i个物品
        if (cw + weight[i] <= w) {
            f(i + 1, cw + weight[i]); // 选择装第i个物品
        }
    }


    /**
     * 动态规划
     *
     * @param weight 物品重量
     * @param n      物品个数
     * @param w      背包可承载重量
     */
    public int knapsack(int[] weight, int n, int w) {
        boolean[][] states = new boolean[n][w + 1]; // 默认值false
        states[0][0] = true;  // 第一行的数据要特殊处理,可以利用哨兵优化
        if (weight[0] <= w) {
            states[0][weight[0]] = true;
        }
        for (int i = 1; i < n; ++i) { // 动态规划状态转移
            for (int j = 0; j <= w; ++j) {// 只放自己到背包里面 和都不放在背包里面
                if (states[i - 1][j] == true) states[i][j] = states[i - 1][j];
            }
            for (int j = 0; j <= w - weight[i]; ++j) {//把第i个物品放入背包
                if (states[i - 1][j] == true) states[i][j + weight[i]] = true;
            }
        }
        for (int i = w; i >= 0; --i) { // 输出结果
            if (states[n - 1][i] == true) return i;
        }
        return 0;
    }


    public static int knapsack2(int[] items, int n, int w) {
        boolean[] states = new boolean[w + 1]; // 默认值false
        states[0] = true;  // 第一行的数据要特殊处理,可以利用哨兵优化
        if (items[0] <= w) {
            states[items[0]] = true;
        }
        for (int i = 1; i < n; ++i) { // 动态规划
            for (int j = w - items[i]; j >= 0; --j) {//把第i个物品放入背包
                if (states[j] == true) states[j + items[i]] = true;
            }
        }
        for (int i = w; i >= 0; --i) { // 输出结果
            if (states[i] == true) return i;
        }
        return 0;
    }


    private int maxV = Integer.MIN_VALUE; // 结果放到maxV中
    private int[] items = {2, 2, 4, 6, 3};  // 物品的重量
    private int[] value = {3, 4, 8, 9, 6}; // 物品的价值

    public void f(int i, int cw, int cv) { // 调用f(0, 0, 0)
        if (cw == w || i == n) { // cw==w表示装满了,i==n表示物品都考察完了
            if (cv > maxV) maxV = cv;
            return;
        }
        f(i + 1, cw, cv); // 选择不装第i个物品
        if (cw + weight[i] <= w) {
            f(i + 1, cw + weight[i], cv + value[i]); // 选择装第i个物品
        }
    }


    /**
     * @param weight
     * @param value
     * @param n
     * @param w
     * @return
     */
    public static int knapsack3(int[] weight, int[] value, int n, int w) {
        int[][] states = new int[n][w + 1];
        for (int i = 0; i < n; ++i) { // 初始化states
            for (int j = 0; j < w + 1; ++j) {
                states[i][j] = -1;
            }
        }
        states[0][0] = 0;
        if (weight[0] <= w) {
            states[0][weight[0]] = value[0];
        }
        for (int i = 1; i < n; ++i) { //动态规划，状态转移
            for (int j = 0; j <= w; ++j) { // 不选择第i个物品
                if (states[i - 1][j] >= 0) states[i][j] = states[i - 1][j];
            }
            for (int j = 0; j <= w - weight[i]; ++j) { // 选择第i个物品
                if (states[i - 1][j] >= 0) {
                    int v = states[i - 1][j] + value[i];
                    int bc = states[i][j + weight[i]];//Backpack Capacity after current goods are put in
                    if (v > bc) {
                        bc = v;
                    }
                }
            }
        }
        // 找出最大值
        int maxvalue = -1;
        for (int j = 0; j <= w; ++j) {
            if (states[n - 1][j] > maxvalue) maxvalue = states[n - 1][j];
        }
        return maxvalue;
    }


    // items商品价格，n商品个数, w表示满减条件，比如200
    public static void double11advance(int[] items, int n, int w) {
        boolean[][] states = new boolean[n][3*w+1];//超过3倍就没有薅羊毛的价值了
        states[0][0] = true;  // 第一行的数据要特殊处理
        if (items[0] <= 3*w) {
            states[0][items[0]] = true;
        }
        for (int i = 1; i < n; ++i) { // 动态规划
            for (int j = 0; j <= 3*w; ++j) {// 不购买第i个商品
                if (states[i-1][j] == true) states[i][j] = states[i-1][j];
            }
            for (int j = 0; j <= 3*w-items[i]; ++j) {//购买第i个商品
                if (states[i-1][j]==true) states[i][j+items[i]] = true;
            }
        }

        int j;
        for (j = w; j < 3*w+1; ++j) {
            if (states[n-1][j] == true) break; // 输出结果大于等于w的最小值
        }
        if (j == 3*w+1) return; // 没有可行解
        for (int i = n-1; i >= 1; --i) { // i表示二维数组中的行，j表示列
            if(j-items[i] >= 0 && states[i-1][j-items[i]] == true) {
                System.out.print(items[i] + " "); // 购买这个商品
                j = j - items[i];
            } // else 没有购买这个商品，j不变。
        }
        if (j != 0) System.out.print(items[0]);
    }


    public static void main(String[] args) {
        int[] weight = {2, 2, 4, 6, 3};
        DynamicProgramming dp = new DynamicProgramming();
        System.out.println(dp.knapsack2(weight, 5, 9));
    }
}
