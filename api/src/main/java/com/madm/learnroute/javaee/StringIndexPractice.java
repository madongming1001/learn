package com.madm.learnroute.javaee;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class StringIndexPractice {
    public static void main(String[] args) {
        String classOrder = "国画-005-20211011,书法-012-20211109";

        char[] chars = classOrder.toCharArray();
        char[] charsCopy = Arrays.copyOfRange(chars, 0, chars.length - 1);
        System.out.println(Arrays.toString(chars));
        System.out.println(Arrays.toString(charsCopy));

        classOrder.replace("$", StringUtils.EMPTY);
        int s1 = classOrder.indexOf("国画");
        int s2 = classOrder.indexOf("书法");
//        System.out.println(classOrder.indexOf("-", s1));
//        System.out.println(classOrder.indexOf("-", s2));

        int first1 = classOrder.indexOf("-", s1);
        classOrder.substring(first1 + 1, classOrder.indexOf("-", first1 + 1));
    }
}