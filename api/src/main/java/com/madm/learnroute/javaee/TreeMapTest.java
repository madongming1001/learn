package com.madm.learnroute.javaee;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.ToIntFunction;

/**
 * TreeMap 默认排序规则:按照key的字典顺序来排序(升序) 当然,也可以自定义排序规则：
 * 1、实现Comparable接口
 * 2、在创建构造器的时候给一个Comparator实例。
 *
 * @Author: MaDongMing
 * @Date: 2022/3/28 5:39 PM
 */
public class TreeMapTest {
    public static void main(String[] args) {
        ToIntFunction hashCode = Object::hashCode;

        TreeMap<User, User> treeMap = new TreeMap<>(Comparator.comparingInt(hashCode));
        treeMap.put(new User(), new User());//key必须实现Comparable接口

//        generateSignature(null);
    }

    public static void generateSignature(TreeMap tm) {
        Set set = tm.entrySet();
        System.out.println(set);
    }
}
