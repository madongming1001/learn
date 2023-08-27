package com.madm.learnroute.javaee;

import cn.hutool.core.util.PrimitiveArrayUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPractice {
    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            System.out.println(random.nextInt());
        }

        System.out.println(RandomUtils.nextDouble(0, 1000000000));
        System.out.println(ThreadLocalRandom.current().nextInt());

        char word = RandomUtil.randomChinese();
        System.out.println(word);

        int[] range = PrimitiveArrayUtil.range(100);
        System.out.println(Arrays.toString(range));

        int[] ints = RandomUtil.randomInts(100);
        System.out.println(Arrays.toString(ints));

        stop:
        while (true) {
            List<String> chinese1 = com.mdm.utils.RandomUtil.randomChinese2(4, 100);
            List<String> chinese2 = com.mdm.utils.RandomUtil.randomChinese2(4, 100);
            for (String s1 : chinese1) {
                for (String s2 : chinese2) {
                    if (s1.hashCode() == s2.hashCode() && !Objects.equals(s1, s2)) {
                        System.out.println("hashcode相等，equals不相等的情况下，字符串是1：" + s1 + " 字符串2是：" + s2);
                        break stop;
                    }
                }
            }
        }
        long startTime = System.currentTimeMillis();
        List<String> chinese1 = com.mdm.utils.RandomUtil.randomChinese1(4, 5248);
        long endTime = System.currentTimeMillis();
        System.out.println("数组访问的方式时间是：" + (endTime - startTime) + " 单词集合数量为：" + chinese1.size());

        System.out.println();
        startTime = System.currentTimeMillis();
        List<String> chinese2 = com.mdm.utils.RandomUtil.randomChinese2(4, 5248);
        endTime = System.currentTimeMillis();
        System.out.println("双for循环的方式时间是：" + (endTime - startTime) + " 单词集合数量为：" + chinese2.size());
    }
}
