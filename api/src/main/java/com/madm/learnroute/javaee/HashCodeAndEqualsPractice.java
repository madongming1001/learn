package com.madm.learnroute.javaee;

import com.mdm.pojo.User;

import java.util.Objects;

/**
 * @author dongming.ma
 * @date 2022/7/13 19:03
 */
public class HashCodeAndEqualsPractice {
    public static void main(String[] args) {
        User user1 = new User("通话");
        User user2 = new User("重地");
        System.out.println(user1.hashCode() == user2.hashCode());//true
        System.out.println(user1.equals(user2));//false

        System.out.println("===========================");
        Long a = 1L;
        int b = 1;
        System.out.println(Objects.equals(b,a));//false
        System.out.println(Objects.equals(a,b));//false
        System.out.println(a==b);//true
    }
}
