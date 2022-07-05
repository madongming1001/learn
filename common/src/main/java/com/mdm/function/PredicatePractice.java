package com.mdm.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class PredicatePractice {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("北京", "南京", "东京", "长安", "洛阳");
        List<String> strings = Collections.synchronizedList(list);
        strings.add("!");
        list = filterStr(list, s -> s.contains("京"));
        list.forEach(System.out::println);
    }

    public static List<String> filterStr(List<String> list, Predicate<String> predicate) {
        List<String> stringList = new ArrayList<>();
        for (String str : list) {
            if (predicate.test(str)) {
                stringList.add(str);
            }
        }
        return stringList;
    }
}
