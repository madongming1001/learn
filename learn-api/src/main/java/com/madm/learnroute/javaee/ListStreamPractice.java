package com.madm.learnroute.javaee;

import com.madm.learnroute.pojo.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListStreamPractice {

    public static void main(String[] args) {
//        int init = 0;
//        List<String> userIds = new ArrayList<>();
//        userIds.add("11");
//        userIds.add("2");
//        System.out.println(userIds);
//        List<String> strings = userIds.subList(0, 2);
//        System.out.println(strings);
//        List<String> subList = userIds.subList(init, 2);
//        init = 2;
//        List<String> subList1 = userIds.subList(init, userIds.size());
//        long count = userIds.stream().filter(i -> Integer.parseInt(i) > 10).mapToInt(Integer::parseInt).count();
//        System.out.println(count);
//
//        Random generate = new Random();
//        int nextInt = generate.nextInt(500);
//        System.out.println("Next int value:"+nextInt);
//        List<Long> list = Arrays.asList(1L,2L,3L,4L,1L);
//        list.stream().forEach(System.out::println);
//        List<Long> collect = list.stream().filter(m -> {
//            return m < 30;
//        }).collect(Collectors.toList());
//        System.out.println(collect);
//        Long a1 = 100L;
//        Long a2 = 1200L;
//        System.out.println(a1<a2);


//        List<Long> collect1 = list.stream().filter(distinctBuMasterId(Long::longValue)).collect(Collectors.toList());
//        System.out.println(collect1);
//        Long admissionNum = 10L;
//        Teacher teacher = new Teacher();
//        teacher.setAge(1);
//        list.stream().forEach(i -> {
//        });
//        System.out.println(teacher);
        //存放apple对象集合

//        Apple apple1 = new Apple(1, "苹果1", new BigDecimal("3.25"), 10);
//        Apple apple12 = new Apple(1, "苹果2", new BigDecimal("1.35"), 20);
//        Apple apple2 = new Apple(2, "香蕉", new BigDecimal("2.89"), 30);
//        Apple apple3 = new Apple(3, "荔枝", new BigDecimal("9.99"), 40);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User(new Double(Math.floor(Math.random() * 10 + 1)).intValue(), "user"));
        }
        System.out.println(users);
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if(user.getId() > 5){
                users.remove(i);
                i--;
            }
        }
        System.out.println(users);
//        List<Apple> appleList = new ArrayList();
//        appleList.add(apple1);
//        appleList.add(apple12);
//        appleList.add(apple2);
//        appleList.add(apple3);
//        // 去重
//        List<Integer> collect = appleList.stream().map(list -> list.getId()).distinct().collect(Collectors.toList());
//        appleList.stream().collect(Collectors.groupingBy(x -> x.getId())).entrySet().stream().distinct().collect(Collectors.toList()).forEach(x -> System.out.println(x.getValue()));
//        collect.stream().forEach(System.out::println);

    }

    private static <T> Predicate<T> distinctBuMasterId(Function<? super T, ?> masterId) {
        ConcurrentMap<Object, Object> map = new ConcurrentHashMap<>();
        return m -> map.putIfAbsent(masterId.apply(m), Boolean.TRUE) == null;
    }
}
