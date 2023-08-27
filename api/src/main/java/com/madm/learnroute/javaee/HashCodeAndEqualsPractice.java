package com.madm.learnroute.javaee;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.mdm.pojo.User;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author dongming.ma
 * @date 2022/7/13 19:03
 */
public class HashCodeAndEqualsPractice {
    public static void main(String[] args) {
        int c = 9;
        c += (c = 3);
        System.out.println(c);
        int d = 9;
        d = d + (d = 3);
        System.out.println(d);

        int i = 2;
        int j = (i = 3) * i;
        System.out.println(j);

        System.out.println("==============================================");

        User user1 = new User("通话");
        User user2 = new User("重地");

        String s1 = "帘懚";
        String s2 = "嵛碽";

        System.out.println(s1.hashCode() == s2.hashCode());
        System.out.println(s1.equals(s2));

        System.out.println(user1.hashCode());
        System.out.println(user1.hashCode() == user2.hashCode());//true
        System.out.println(user1.equals(user2));//false
        System.out.println(UUID.randomUUID());


        int[] ints = new int[5];
        System.out.println(System.identityHashCode(user2) % ints.length);
        System.out.println(System.identityHashCode(user2) & ints.length);


        System.out.println("===========================");
        Long a = 1L;
        int b = 1;
        System.out.println(Objects.equals(b, a));//false
        System.out.println(Objects.equals(a, b));//false
        System.out.println(a == b);//true

        List<String> equalsLists = Lists.newArrayList();
        stop:
        while (true) {
            char c1 = RandomUtil.randomChinese();
            List<String> chinese1 = com.mdm.utils.RandomUtil.randomChinese1(2, 10496);
            List<String> chinese2 = com.mdm.utils.RandomUtil.randomChinese1(2, 10496);
            for (String s1s : chinese1) {
                for (String s2s : chinese2) {
                    if (s1.hashCode() == s2.hashCode() && !Objects.equals(s1, s2)) {
                        System.out.println("hashcode相等，equals不相等的情况下，字符串是1：" + s1 + " 字符串2是：" + s2);
                        equalsLists.add(s1);
                        equalsLists.add(s2);
                        if (equalsLists.contains(s1) && equalsLists.contains(s2)) {
                            continue;
                        }
                        if (equalsLists.size() / 2 > 6) {
                            break stop;
                        }
                    }
                }
            }
        }
    }
}
