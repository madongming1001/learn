package com.madm.learnroute.javaee;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.mdm.pojo.Invitee;
import com.mdm.pojo.Teacher;
import com.mdm.pojo.Apple;
import com.mdm.pojo.User;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.curator.shaded.com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListStreamPractice {

    public static void main(String[] args) throws Exception {
        int init = 0;
        List<String> userIds = new ArrayList<>();
        userIds.add("11");
        userIds.add("2");
        System.out.println(userIds);
        List<String> strings = userIds.subList(0, 2);
        System.out.println(strings);
        List<String> subList = userIds.subList(init, 2);
        init = 2;
        List<String> subList1 = userIds.subList(init, userIds.size());
        long count = userIds.stream().filter(i -> Integer.parseInt(i) > 10).mapToInt(Integer::parseInt).count();
        System.out.println(count);

        Random generate = new Random();
        int nextInt = generate.nextInt(500);
        System.out.println("Next int value:" + nextInt);
        List<Long> list = Arrays.asList(1L, 2L, 3L, 4L, 1L);
        list.stream().forEach(System.out::println);
        List<Long> collect = list.stream().filter(m -> {
            return m < 30;
        }).collect(Collectors.toList());
        System.out.println(collect);
        Long a1 = 100L;
        Long a2 = 1200L;
        System.out.println(a1 < a2);


        List<Long> collect1 = list.stream().filter(distinctBuMasterId(Long::longValue)).collect(Collectors.toList());
        System.out.println(collect1);
        Long admissionNum = 10L;
        Teacher teacher = new Teacher();
        teacher.setAge(1);
        list.stream().forEach(i -> {
        });
        System.out.println(teacher);
        //存放apple对象集合

        Apple apple1 = new Apple(1, "苹果1", new BigDecimal("3.25"), 10);
        Apple apple2 = new Apple(2, "香蕉", new BigDecimal("2.89"), 30);
        Apple apple3 = new Apple(3, "荔枝", new BigDecimal("9.99"), 40);
        Apple apple4 = new Apple(4, "苹果2", new BigDecimal("1.35"), 20);

        //验证对过滤后的集合修改会影响原集合的内容
        affectTheOriginalCollection(apple1,apple2,apple3,apple4);

        List<Invitee> invitees = Lists.newArrayList(new Invitee("1", "1", "1"), new Invitee("2", "2", "2"));
        List<String> newInstance = Lists.newArrayList("1", "2", "3", "4", "5", "6", "1");
        List<String> oldInstance = Lists.newArrayList("3", "232", "35", "24", "5", "6", "1");

        //add：加、subtract：减、multiply：乘、divide：除
        System.out.println(CollectionUtils.union(newInstance, oldInstance));//并集
        System.out.println(CollectionUtils.intersection(newInstance, oldInstance));//交集
        System.out.println(CollectionUtils.disjunction(newInstance, oldInstance));//交集的补集
        System.out.println(CollectionUtils.subtract(newInstance, oldInstance));//集合相减


        System.out.println(CollectionUtils.isNotEmpty(newInstance));

        Map<String, Integer> collect4 = invitees.stream().collect(Collectors.toMap(key -> key.getUserId(), value -> 1, Integer::sum));
        System.out.println(collect4);

        if (!CollectionUtils.containsAny(null, 2)) {
            System.out.println("不包含");
        }

        System.out.println(JSONObject.toJSONString(invitees));
        List<Integer> participants = Lists.newArrayList(111);
        List<User> users = Lists.newArrayList(new User(111, "666"), new User(222, "777"), new User(333, "888"), new User(444, "999"), new User(555, "101"));
        List<User> userRps = users.stream().filter(iv -> !participants.contains(iv.getId())).map(User::new).collect(Collectors.toList());
        System.out.println(userRps);

        List<CompletableFuture> collect2 = invitees.stream().map(i -> parallelSleep(i)).collect(Collectors.toList());
        CompletableFuture.allOf(collect2.toArray(new CompletableFuture[collect2.size()]));
        List<String> str = Lists.newArrayList("1", "1", "1");
        HttpUtil.createPost("http://localhost:8081/saveMeeting").body("").execute();
        System.out.println(JSONObject.toJSONString(invitees));

        for (int i = 0; i < 10; i++) {
            int idAndName = new Double(Math.floor(Math.random() * 10 + 1)).intValue();
            users.add(new User(idAndName, idAndName + ""));
        }
        System.out.println(users);


        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if (user.getId() >= 5) {
                users.remove(i);
//                i--;
            }
        }

        System.out.println(users);
        List<Apple> appleList = new ArrayList();
        appleList.add(apple1);
        appleList.add(apple2);
        appleList.add(apple3);
        // 去重
        List<Integer> collect3 = appleList.stream().map(e -> e.getId()).distinct().collect(Collectors.toList());
        appleList.stream().collect(Collectors.groupingBy(x -> x.getId())).entrySet().stream().distinct().collect(Collectors.toList()).forEach(x -> System.out.println(x.getValue()));
        collect3.stream().forEach(System.out::println);

    }

    private static void affectTheOriginalCollection(Apple apple1, Apple apple2, Apple apple3, Apple apple4) {
        System.out.println("begin of method： affectTheOriginalCollection");
        ArrayList<Apple> originalCollection = Lists.newArrayList(apple1, apple2, apple3, apple4);
        List<Apple> currentCollection = originalCollection.stream().filter(apple -> apple.getId() > 2).collect(Collectors.toList());
        for (Apple apple : currentCollection) {
            apple.setName(apple.getName() + " 修改后的数据");
        }
        for (Apple apple : originalCollection) {
            System.out.println(apple.getName());
        }
        System.out.println("end of method： affectTheOriginalCollection");
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
