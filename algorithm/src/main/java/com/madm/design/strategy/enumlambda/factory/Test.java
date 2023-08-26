package com.madm.design.strategy.enumlambda.factory;

import java.util.*;
import java.util.stream.Collectors;

//Java8中方便又实用的Map函数
public class Test {

    static List<User> users = Arrays.asList(
            new User(1, 23),
            new User(2, 25),
            new User(3, 45),
            new User(1, 49));

    public static void main(String[] args) {

        Map<Integer, List<User>> userMap = new HashMap<>();
        for (User user : users) {
            if (!userMap.containsKey(user.getPayType())) {
                userMap.put(user.getPayType(), new ArrayList<>());
            }
            userMap.get(user.getPayType()).add(user);
        }
        System.out.println(userMap);


        Map<Integer, List<User>> collectMap = users.
                stream().
                collect(Collectors.groupingBy(item -> item.getPayType()));
        System.out.println(collectMap);


        Map<Integer, List<User>> userMapNew = new HashMap<>();
        for (User user : users) {
            userMapNew.computeIfAbsent(user.getPayType(), k -> new ArrayList<>()).
                    add(user);
        }


        List<User> userNews = users;
        Map<Integer, List<User>> userTypeMap = new DefaultHashMap<>(ArrayList::new);
        for (User userNew : userNews) {
            userTypeMap.get(userNew.getPayType())
                    .add(userNew);
        }


    }

}
