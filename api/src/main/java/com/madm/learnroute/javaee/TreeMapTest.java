package com.madm.learnroute.javaee;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Author: MaDongMing
 * @Date: 2022/3/28 5:39 PM
 */
public class TreeMapTest {
    public static void main(String[] args) {
        generateSignature(null);
    }
    public static void generateSignature(TreeMap tm){

        Set set = tm.entrySet();
        System.out.println(set);
    }
}
