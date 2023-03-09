package com.madm.learnroute.javaee;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.mdm.pojo.AuthParam;
import com.mdm.pojo.User;
import com.mdm.utils.GsonObject;
import org.apache.commons.collections4.map.MultiKeyMap;
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
        HashBasedTable<String, String, String> table = HashBasedTable.create();
        table.put("1", "2", "3");

        MultiKeyMap multiKeyMap = new MultiKeyMap();
        multiKeyMap.put("1", "2", "3", "1", "2", "3");
        Object o = multiKeyMap.get("1");
        System.out.println("多key" + o);


        HashMap<String, Integer> prices = new HashMap<>(2);
        for (int i = 0; i < 64; i++) {
            prices.put("Shoes" + (int) (Math.random() * 10000) + 1, 200);
        }
        // 往HashMap中添加映射项

//        prices.put("Shoes", 200);
//        prices.put("Bag", 300);
//        prices.put("Pant", 150);
//        prices.put("111", 150);
        prices.put("Shoes" + (int) (Math.random() * 80) + 1, 200);
//        System.out.println("HashMap: " + prices);
//        prices.forEach((value, value) -> {
//            System.out.println("value : " + value + "value :" + value);
//        });

        System.out.println(prices.values());


        // new EntryIterator() extends HashIterator 构造方法就会设置 expectedModCount = modCount;
        Iterator<Map.Entry<String, Integer>> iterator = prices.entrySet().iterator();


//        List<Integer> uniqueId = new ArrayList<>();
//        int newId = 0;
//        do {
//            newId = newId++;
//        } while (!uniqueId.add(newId));
//        System.out.println(uniqueId);
//        System.out.println("runner");
        List<User> lists = Arrays.asList(new User(1, "group 1"), new User(2, "group 2"), new User(3, "group 3"), new User(4, "group 4"));
        Map<Integer, User> userMap = lists.stream().collect(Collectors.toConcurrentMap(x -> x.getId(), Function.identity(), (k1, k2) -> k1));
        userMap.computeIfAbsent(1, key -> {
            System.out.println(key);
            return new User(1000, "group 1000");
        });
//        System.out.println(userMap);
        userMap.compute(1, (key, oldValue) -> {
            if (key == 1) {
                return new User();
            }
            return new User();
        });

        int newPrice = prices.compute("Shoes2", (k, v) -> {
            if (v == null) {
                return 20;
            }
            return v - v * 10 / 100;
        });
//
//        //不存在添加 存在重新计算remappingFunction
//        //创建一个 HashMap
//        HashMap<String, Integer> prices = new HashMap<>(2);
//
//        // 往HashMap中添加映射项
//        prices.put("Shoes", 200);
//        prices.put("Bag", 300);
//        prices.put("Pant", 150);
//        prices.put("111", 150);
//        System.out.println("HashMap: " + prices);
//
//        Integer value = prices.putIfAbsent("111", 32);
//        System.out.println("HashMap: " + prices);
//        System.out.println("之前值是："+value);
//
//        Iterator<Map.Entry<String, Integer>> iterator = prices.entrySet().iterator();
//
        // 重新计算鞋子打了10%折扣后的值
//
//        // 输出更新后的HashMap
//        System.out.println("Updated HashMap: " + prices);

//        System.out.println(GsonObject.createGson().toJson(userMap));
//        userMap.get(1).setAuth(Arrays.asList(new AuthParam("1", "2")));
//        System.out.println(GsonObject.createGson().toJson(userMap));
//
//        Collection<List<User>> values = lists.stream().collect(Collectors.groupingBy(s -> {
//            return s.getName();
//        })).values();
//        values.stream().forEach(System.out::println);
//        System.out.println(values.size());
//
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


//
//        Map<String, Long> masterId = new HashMap<>();
//        mapArrayList.stream().forEach(e -> {
//            for (Map.Entry<String, Long> map : e.entrySet()) {
//                String value = map.getKey();
//                Long value = map.getValue();
//                if (Long.parseLong(value) == value) {
//
//                }
//            }
//        });
//        System.out.println(masterId);

//        threadSafeQuestion();
        Map<String, Object> map1 = Maps.newHashMap();
        List<String> list = Lists.newArrayList("1", "2", "3", "2", "3", "2");
        Object value = map1.get("value");
        if (value == null) {
            value = new Object();
            map1.put("key", value);
            //如果key对应的value值存在，进行相应的操作
            // java8之后，上面的操作可以简化为一行，若key对应的value为空，会将第二个参数的返回值存入并返回
        } else {
            Object key2 = map1.computeIfAbsent("key1", key -> new Object());
            System.out.println(map1);        //输出：{key1=java.lang.Object@708f5957, value=java.lang.Object@68999068}    }    /**
        }
    }

    private static void threadSafeQuestion() {
        Thread t = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                new Thread(() -> map.put(UUID.randomUUID().toString(), ""), "ftf" + i).start();
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
//        System.out.println(userNumber);
    }
}

