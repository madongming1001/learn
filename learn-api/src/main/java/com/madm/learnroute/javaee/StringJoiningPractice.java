package com.madm.learnroute.javaee;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringJoiningPractice {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("abc", "", "de", "efg", "abcd", "", "jkl");

        String mergeString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(","));

        System.err.println("合并字符串 : "+mergeString);
    }
}
