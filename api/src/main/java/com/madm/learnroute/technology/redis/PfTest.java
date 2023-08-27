package com.madm.learnroute.technology.redis;

import java.util.concurrent.ThreadLocalRandom;

/**
 * HyperLogLog 是最早由Flajolet及其同事在 2007 年提出的一种估算基数的近似最优算法。
 * 基数统计(Cardinality Counting) 通常是用来统计一个集合中不重复的元素个数。
 * 实际上目前还没有发现更好的在大数据场景中准确计算基数的高效算法，因此在不追求绝对精确的情
 * 况下，使用概率算法算是一个不错的解决方案.
 * 概率算法不直接存储数据集合本身，通过一定的概率统计方法预估基数值，这种方法可以大大节省内
 * 存，同时保证误差控制在一定范围内。
 * Linear Counting(LC)：早期的基数估计算法，LC在空间复杂度方面并不算优秀，实际上LC的空
 * 间复杂度与上文中简单 bitmap 方法是一样的（但是有个常数项级别的降低），都是 O(Nmax)
 * LogLog Counting(LLC)：LogLog Counting 相比于 LC 更加节省内存，空间复杂度只有O(log2(log2(Nmax)))
 * HyperLogLog Counting(HLL)：HyperLogLog Counting 是基于 LLC 的优化和改进，在同样空间
 * 复杂度情况下，能够比 LLC 的基数估计误差更小
 * 其中，HyperLogLog 的表现是惊人的，上面我们简单计算过用 bitmap 存储 1 个亿 统计数据大概需要
 * 12 M 内存，而在 HyperLoglog 中，只需要不到 1 K 内存就能够做到！在 Redis 中实现的
 * HyperLoglog 也只需要 12 K 内存，在 标准误差 0.81% 的前提下，能够统计 264 个数据！
 *
 * 会发现 K 和 N 的对数之间存在显著的线性相关性：N 约等于 2k
 */
public class PfTest {
    static class BitKeeper {
        private int maxbit;

        public void random() {
            long value = ThreadLocalRandom.current().nextLong(2L << 32);
            int bit = lowZeros(value);
            if (bit > this.maxbit) {
                this.maxbit = bit;
            }
        }

        private int lowZeros(long value) {
            int i = 0;
            for (; i < 32; i++) {
                if (value >> i << i != value) {
                    break;
                }
            }
            return i - 1;
        }
    }

    static class Experiment {
        private int n;
        private BitKeeper keeper;

        public Experiment(int n) {
            this.n = n;
            this.keeper = new BitKeeper();
        }

        public void work() {
            for (int i = 0; i < n; i++) {
                this.keeper.random();
            }
        }

        public void debug() {
            System.out.printf("%d %.2f %d\n", this.n, Math.log(this.n) / Math.log(2), this.keeper.maxbit);
        }
    }

    public static void main(String[] args) {
        for (int i = 1000; i < 100000; i += 100) {
            Experiment exp = new Experiment(i);
            exp.work();
            exp.debug();
        }
    }
}
