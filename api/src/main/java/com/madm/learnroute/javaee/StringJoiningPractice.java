package com.madm.learnroute.javaee;

import java.util.StringJoiner;

public class StringJoiningPractice {
    public static void main(String[] args) {
        StringJoiner sj = new StringJoiner(",", "start ", " end");
        sj.add("1").add("222").add("222");
        System.out.println(sj);
//        List<String> strings = Arrays.asList("abc", "", "de", "efg", "abcd", "", "jkl");
//        String mergeString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(","));
//        System.err.println("合并字符串 : "+mergeString);
    }
}
