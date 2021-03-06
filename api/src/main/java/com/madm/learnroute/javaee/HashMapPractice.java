package com.madm.learnroute.javaee;

import com.mdm.pojo.AuthParam;
import com.mdm.pojo.User;
import com.mdm.utils.GsonObject;
import org.apache.curator.shaded.com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author madongming
 */
public class HashMapPractice {

    private static final AtomicInteger userNumber = new AtomicInteger();
    static final HashMap<String, String> map = new HashMap<String, String>(2);

    public static void main(String[] args) {
        List<User> lists = Arrays.asList(new User(1, "group 1", Lists.newArrayList()), new User(2, "group 1", Lists.newArrayList()), new User(3, "group 2", Lists.newArrayList()), new User(4, "group 2", Lists.newArrayList()));
        Map<Integer, User> userMap = lists.stream().collect(Collectors.toConcurrentMap(x -> x.getId(), Function.identity(), (k, v) -> k));
        System.out.println(GsonObject.createGson().toJson(userMap));
        userMap.get(1).setAuth(Arrays.asList(new AuthParam("1", "2")));
        System.out.println(GsonObject.createGson().toJson(userMap));

        Collection<List<User>> values = lists.stream().collect(Collectors.groupingBy(s -> {
            return s.getName();
        })).values();
        values.stream().forEach(System.out::println);
        System.out.println(values.size());

        ArrayList<Map<String, Long>> mapArrayList = new ArrayList();
        HashMap<String, Long> hashMap1 = new HashMap<>();
        hashMap1.put("1", 1L);
        HashMap<String, Long> hashMap2 = new HashMap<>();
        hashMap2.put("2", 2L);
        HashMap<String, Long> hashMap3 = new HashMap<>();
        hashMap3.put("3", 3L);
        HashMap<String, Long> hashMap4 = new HashMap<>();
        hashMap4.put("4", 4L);
        mapArrayList.add(hashMap1);
        mapArrayList.add(hashMap2);
        mapArrayList.add(hashMap3);
        mapArrayList.add(hashMap4);

        Map<String, Long> masterId = new HashMap<>();
        mapArrayList.stream().forEach(e -> {
            for (Map.Entry<String, Long> map : e.entrySet()) {
                String key = map.getKey();
                Long value = map.getValue();
                if (Long.parseLong(key) == value) {

                }
            }
        });
        System.out.println(masterId);

//        threadSafeQuestion();
    }

    private static void threadSafeQuestion() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    new Thread(() -> map.put(UUID.randomUUID().toString(), ""), "ftf" + i).start();
                }
            }
        }, "ftf");
        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printIntegerDefaultValue() {
        userNumber.getAndIncrement();
        System.out.println(userNumber);
    }
}

