package com.example.learnroute.javaee;

import com.sun.media.sound.SoftTuning;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LastIndexPractice {
    public static void main(String[] args) {
//        String scheduleName = "谁都别动我科目-167-20210804";
//        String substring = scheduleName.substring(0,scheduleName.lastIndexOf("-"));
//        System.out.println(scheduleName.substring(0, substring.lastIndexOf("-")));
//
//        List<String> s = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            s.add(i+"");
//        }
//        List<String> s1 = new ArrayList<>(s);
//        s1.remove(2);
//        s.forEach(System.out::println);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }
        List<String> strings = list.subList(10, list.size());
        List<String> collect = strings.stream().filter(m -> {
            return Long.valueOf(m) > 1;
        }).collect(Collectors.toList());
        System.out.println(collect);
        if(strings.isEmpty()){
            System.out.println("是空的");
        }
        System.out.println(strings);
    }
}
