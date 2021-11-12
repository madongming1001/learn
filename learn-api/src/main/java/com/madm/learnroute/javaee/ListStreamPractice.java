package com.madm.learnroute.javaee;

import cn.hutool.core.lang.func.Func1;
import com.madm.pojo.Teacher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListStreamPractice {

    public static void main(String[] args) {
        for (;;){
            Func1<Teacher, Integer> getAge = Teacher::getAge;
        }
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
    }

    private static <T> Predicate<T> distinctBuMasterId(Function<? super T,?> masterId) {
        ConcurrentMap<Object, Object> map = new ConcurrentHashMap<>();
        return m -> map.putIfAbsent(masterId.apply(m),Boolean.TRUE) == null;
    }
}
