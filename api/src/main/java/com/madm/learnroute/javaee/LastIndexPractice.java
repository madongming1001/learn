package com.madm.learnroute.javaee;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LastIndexPractice {
    public static void main(String[] args) {
//        String scheduleName = "谁都别动我科目-167-20210804";
        List<String> longs = Arrays.asList("1L", "2L", "3L", "4L");
        String collect = longs.stream().collect(Collectors.joining(","));
        System.out.println(collect);

//        String substring = scheduleName.substring(0,scheduleName.lastIndexOf("-"));
//        System.out.println(scheduleName.substring(0, substring.lastIndexOf("-")));
        String currentScheduleName = "国画-005-20211011,书法-012-20211109";
//        String classOrder = Arrays.stream(currentScheduleName.split(",")).filter(f -> f.indexOf("国画") != -1 ? true : false).map(a -> {
//            int barF = a.indexOf("-");
//            int barS = a.lastIndexOf("-");
//            return a.substring(barF + 1, barS);
//        }).collect(Collectors.joining());
        String scheduleTime = Arrays.stream(currentScheduleName.split(",")).filter(f -> f.indexOf("国画") != -1 ? true : false).map(a -> {
            int barL = a.lastIndexOf("-");
            return a.substring(barL + 1);
        }).collect(Collectors.joining());
        System.out.println(scheduleTime);
//        List<String> s = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            s.add(i+"");
//        }
//        List<String> s1 = new ArrayList<>(s);
//        s1.remove(2);
//        s.forEach(System.out::println);
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            list.add(i + "");
//        }
//        List<String> strings = list.subList(10, list.size());
//        List<String> collect = strings.stream().filter(m -> {
//            return Long.valueOf(m) > 1;
//        }).collect(Collectors.toList());
//        System.out.println(collect);
//        if(strings.isEmpty()){
//            System.out.println("是空的");
//        }
//        System.out.println(strings);
    }
}
