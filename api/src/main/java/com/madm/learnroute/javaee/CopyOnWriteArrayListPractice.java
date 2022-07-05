package com.madm.learnroute.javaee;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dongming.ma
 * @date 2022/7/5 16:33
 */
public class CopyOnWriteArrayListPractice {
    public static void main(String[] args) {
        //添加元素会获取一个ReentrantLock锁
        CopyOnWriteArrayList<String> cwList = new CopyOnWriteArrayList<String>();
        cwList.add("11");
    }
}
