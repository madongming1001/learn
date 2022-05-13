package com.madm.learnroute.javaee;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.madm.learnroute.pojo.Invitee;
import com.madm.learnroute.pojo.User;
import org.apache.curator.shaded.com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListStreamPractice {

    public static void main(String[] args) throws Exception {
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
//        List<Invitee> invitees = Lists.newArrayList(new Invitee("1", "1", "1"), new Invitee("2", "2", "2"));
        List<String> invitees = Lists.newArrayList("1", "2", "3", "4");
        System.out.println(JSONObject.toJSONString(invitees));
        List<Integer> participants = Lists.newArrayList(111);
        List<User> users = Lists.newArrayList(new User(111, "666"), new User(222, "777"), new User(333, "888"), new User(444, "999"), new User(555, "101"));
        List<User> userRps = users.stream().filter(iv -> !participants.contains(iv.getId())).map(User::new).collect(Collectors.toList());
        System.out.println(userRps);

//        List<CompletableFuture> collect = invitees.stream().map(i -> parallelSleep(i)).collect(Collectors.toList());
//        CompletableFuture.allOf(collect.toArray(new CompletableFuture[collect.size()]));
//        List<String> str = Lists.newArrayList("1", "1", "1");
//        HttpUtil.createPost("http://localhost:8081/saveMeeting").body("").execute();
//        System.out.println(JSONObject.toJSONString(invitees));

//        for (int i = 0; i < 10; i++) {
//            int idAndName = new Double(Math.floor(Math.random() * 10 + 1)).intValue();
//            users.add(new User(idAndName, idAndName + ""));
//        }
//        System.out.println(users);


//        for (int i = 0; i < users.size(); i++) {
//            User user = users.get(i);
//            if(user.getId() >= 5){
//                users.remove(i);
////                i--;
//            }
//        }
//
//        System.out.println(users);
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

    private static CompletableFuture parallelSleep(Invitee invitee) {
        return CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
        });
    }

    private static <T> Predicate<T> distinctBuMasterId(Function<? super T, ?> masterId) {
        ConcurrentMap<Object, Object> map = new ConcurrentHashMap<>();
        return m -> map.putIfAbsent(masterId.apply(m), Boolean.TRUE) == null;
    }


}
