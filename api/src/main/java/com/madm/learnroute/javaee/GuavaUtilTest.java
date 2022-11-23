package com.madm.learnroute.javaee;

import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/11/11 11:52
 */
public class GuavaUtilTest {
    public static void main(String[] args) {
        //内部实现 k List<V>
        multimapTest();
        //内部实现了两个map 一个k v 一个v k
        bimapTest();
        //重写了add方法
        immutableListTest();
    }

    private static void immutableListTest() {
        List list = new ArrayList();
        list.add("wyp");
        list.add("good");
//        ImmutableList immutableList = ImmutableList
//                .builder().addAll(list).add("add").build();
        ImmutableList<String> immutableList = ImmutableList.of("1", "2");
        System.out.println(immutableList);
    }

    private static void multimapTest() {
        Multimap<String, String> myMultimap = ArrayListMultimap.create();

        // Adding some key/value
        myMultimap.put("Fruits", "Bannana");
        myMultimap.put("Fruits", "Apple");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Fruits", "Pear");
        myMultimap.put("Vegetables", "Carrot");

        // Getting the size
        int size = myMultimap.size();
        System.out.println(size); // 5

        // Getting values
        Collection<String> fruits = myMultimap.get("Fruits");
        System.out.println(fruits);//  [Bannana, Apple, Pear, Pear]
        System.out.println(ImmutableSet.copyOf(fruits));
        // [Bannana, Apple, Pear]
        // Set<Foo> set = Sets.newHashSet(list);
        // Set<Foo> foo = new HashSet<Foo>(myList);

        Collection<String> vegetables = myMultimap.get("Vegetables");
        System.out.println(vegetables); // [Carrot]

        // Iterating over entire Mutlimap
        for (String value : myMultimap.values()) {
            System.out.println(value);
        }

        // Removing a single value
        myMultimap.remove("Fruits", "Pear");
        System.out.println(myMultimap.get("Fruits"));
        // [Bannana, Apple, Pear]

        Collection<String> coll = myMultimap.replaceValues("Fruits", Arrays.asList("178", "910", ""));

        System.out.println("coll=" + coll);
        System.out.println("myMultimap=" + myMultimap);

        // Remove all values for a key
        myMultimap.removeAll("Fruits");
        System.out.println(myMultimap.get("Fruits"));
    }

    public static void bimapTest() {
        BiMap<Integer, String> logfileMap = HashBiMap.create();
        logfileMap.put(1, "a.log");
        logfileMap.put(2, "b.log");
        logfileMap.put(3, "c.log");
        System.out.println("logfileMap:" + logfileMap);
        BiMap<String, Integer> filelogMap = logfileMap.inverse();
        System.out.println("filelogMap:" + filelogMap);

        logfileMap.put(4, "d.log");

        System.out.println("logfileMap:" + logfileMap);
        System.out.println("filelogMap:" + filelogMap);
    }
}
