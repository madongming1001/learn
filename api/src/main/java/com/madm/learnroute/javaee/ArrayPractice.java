package com.madm.learnroute.javaee;

import org.apache.curator.shaded.com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author dongming.ma
 * @date 2022/7/1 21:11
 */
public class ArrayPractice {


    public static void main(String[] args) {
        ArrayList<String> nums = Lists.newArrayList("1", "2", "3");
        System.out.println(nums);
        System.out.println(Arrays.toString(nums.toArray(new String[0])));
    }
}
