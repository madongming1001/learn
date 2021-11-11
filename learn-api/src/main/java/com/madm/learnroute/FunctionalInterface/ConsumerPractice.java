package com.madm.learnroute.FunctionalInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ConsumerPractice {

    public static void main(String[] args) {
//        test();
//        test02();
        test03();
    }


    //Consumer消费者
    private static <T> void accept(T t, Consumer<T> con) {
        con.accept(t);
    }

    //default Consumer<T> andThen(Consumer<? super T> after)
    private static <T> void accept(T t, Consumer<T> con1, Consumer<T> con2) {
        con1.andThen(con2).accept(t);//先执行con1再执行con2
    }

    private static <Map> void invoke(Map map, Supplier<Map> sup, Consumer<Map> con) {
        con.accept(sup.get());
    }

    private static void invoke(Map<String, Integer> map) {
        invoke(map, () -> {
            Map<String, Integer> newMap = new HashMap<>();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Integer value = map.get(key);
                if (value >= 60) {//过滤掉成绩低于60分的人
                    newMap.put(key, value);
                }
            }
            map.clear();
            map.putAll(newMap);
            return map;
        }, stringIntegerMap -> {
            int avg = 0;
            Integer sum = 0;
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Integer value = map.get(key);
                sum += value;
            }
            avg = sum / map.size();
            System.out.println(avg);
        });
    }

    public static void test() {
        //计算班上所有同学的平均成绩
        Map<String, Integer> map = new HashMap() {{
            put("zhangsan", 100);
            put("lisi", 80);
            put("wangwu", 60);
        }};
        accept(map, m -> {
            Set<String> keySet = map.keySet();
            int avg = 0;
            int sum = 0;
            for (String key : keySet) {
                Integer value = map.get(key);
                sum += value;
            }
            avg = sum / map.size();
            System.out.println(avg);
        });
    }

    public static void test02() {
        //计算班上所有同学的平均成绩,再计算学生个数
        Map<String, Integer> map = new HashMap() {{
            put("zhangsan", 100);
            put("lisi", 80);
            put("wangwu", 60);
        }};
        accept(map, (m1) -> {
            Set<String> keySet = map.keySet();
            int avg = 0;
            int sum = 0;
            for (String key : keySet) {
                Integer value = map.get(key);
                sum += value;
            }
            avg = sum / map.size();
            System.out.println(avg);
        }, (m2) -> System.out.println(map.size()));

    }

    public static void test03() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("jack", 100);
        map.put("mike", 80);
        map.put("tom", 60);
        map.put("jerry", 40);
        invoke(map);
    }
}





