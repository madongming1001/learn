package com.madm.learnroute.javaee;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dongming.ma
 * @date 2022/7/5 16:33
 */
public class CopyOnWriteArrayListPractice {
    public static void main(String[] args) {
        //添加元素会获取一个 ReentrantLock 锁
        CopyOnWriteArrayList<String> cwList = new CopyOnWriteArrayList<String>();
        cwList.add("11");

        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>(new Integer[]{1, 2, 3});
        System.out.println(list); //[1, 2, 3]
        //Get iterator 1
        Iterator<Integer> itr1 = list.iterator();
        //Add one element and verify list is updated
        list.add(4);
        System.out.println(list); //[1, 2, 3, 4]
        //Get iterator 2
        Iterator<Integer> itr2 = list.iterator();
        System.out.println("====Verify Iterator 1 content====");
        itr1.forEachRemaining(System.out::println); //1,2,3
        System.out.println("====Verify Iterator 2 content====");
        itr2.forEachRemaining(System.out::println); //1,2,3,4
    }
}
