package com.madm.learnroute.javaee;

import org.apache.commons.lang3.StringUtils;

import java.util.Hashtable;

/**
 * 不允许插入空值
 *
 * @author dongming.ma
 * @date 2022/11/16 21:16
 */
public class HashTablePractice {
    public static void main(String[] args) {
        Hashtable<String, String> hashtable = new Hashtable<>();
        System.out.println(hashtable.put(null, StringUtils.EMPTY));
    }
}
