package com.madm.learnroute.javaee;

import com.mdm.pojo.User;

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
    }
}
