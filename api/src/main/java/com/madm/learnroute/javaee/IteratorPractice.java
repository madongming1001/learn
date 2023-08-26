package com.madm.learnroute.javaee;

import com.google.common.collect.Lists;

import java.util.Iterator;

/**
 * @author dongming.ma
 * @date 2022/7/14 23:02
 */
public class IteratorPractice {
    public static void main(String[] args) {
        Iterator<String> iterator = Lists.newArrayList("1", "2").iterator();
        System.out.println(iterator.next());
        System.out.println(iterator.next());
    }
}
