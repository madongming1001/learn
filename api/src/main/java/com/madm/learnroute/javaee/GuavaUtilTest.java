package com.madm.learnroute.javaee;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author dongming.ma
 * @date 2022/11/11 11:52
 */
public class GuavaUtilTest {
    public static void main(String[] args) {
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

        Collection<String> coll = myMultimap.replaceValues("Fruits", Arrays.asList("178","910",""));

        System.out.println("coll="+coll);
        System.out.println("myMultimap="+myMultimap);

        // Remove all values for a key
        myMultimap.removeAll("Fruits");
        System.out.println(myMultimap.get("Fruits"));
    }
}
