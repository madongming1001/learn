package com.madm.learnroute.javaee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dongming.ma
 * @date 2023/3/8 18:06
 */
public class DeleteListCircularlyTest {
    public static List<String> initList = Arrays.asList("张三", "李四", "周一", "刘四", "李强", "李白");

    public static void main(String[] args) {
//        remove1();//普通 for 循环删除（不可靠）
//        remove2();//普通 for 循环提取变量删除（抛异常）
//        remove3();//普通 for 循环倒序删除（可靠）
//        remove4();//增强 for 循环删除（抛异常）
//        remove5();//迭代器循环迭代器删除（可靠）
//        remove6();//迭代器循环集合删除（抛异常）
//        remove7();//集合 forEach 方法循环删除（抛异常）
        remove8();//stream filter 过滤（可靠）
    }

    /**
     * 1、普通 for 循环删除（不可靠）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove1() {
        List<String> list = new ArrayList(initList);
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            if (str.startsWith("李")) {
                list.remove(i);
            }
        }
        System.out.println(list);
    }

    /**
     * 2、普通 for 循环提取变量删除（抛异常）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove2() {
        List<String> list = new ArrayList(initList);
        int size = list.size();
        for (int i = 0; i < size; i++) {
            String str = list.get(i);
            if (str.startsWith("李")) {
                list.remove(i);
            }
        }
        System.out.println(list);
    }

    /**
     * 3、普通 for 循环倒序删除（可靠）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove3() {
        List<String> list = new ArrayList(initList);
        for (int i = list.size() - 1; i > 0; i--) {
            String str = list.get(i);
            if (str.startsWith("李")) {
                list.remove(i);
            }
        }
        System.out.println(list);
    }

    /**
     * 4、增强 for 循环删除（抛异常）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove4() {
        List<String> list = new ArrayList(initList);
        for (String element : list) {
            if (element.startsWith("李")) {
                list.remove(element);
            }
        }
        System.out.println(list);
    }


    /**
     * 5、迭代器循环迭代器删除（可靠）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove5() {
        List<String> list = new ArrayList(initList);
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext(); ) {
            String str = iterator.next();
            if (str.contains("李")) {
                iterator.remove();
            }
        }
        System.out.println(list);
    }


    /**
     * 6、迭代器循环集合删除（抛异常）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove6() {
        List<String> list = new ArrayList(initList);
        for (Iterator<String> ite = list.iterator(); ite.hasNext(); ) {
            String str = ite.next();
            if (str.contains("李")) {
                list.remove(str);
            }
        }
        System.out.println(list);
    }


    /**
     * 7、集合 forEach 方法循环删除（抛异常）
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove7() {
        List<String> list = new ArrayList(initList);
        list.forEach((e) -> {
            if (e.contains("李")) {
                list.remove(e);
            }
        });
        System.out.println(list);
    }


    /**
     * 8、stream filter 过滤（可靠）,如果是引用的话修改当前集合的引用内容，会导致原集合变化
     *
     * @author: 栈长
     * @from: Java技术栈
     */
    public static void remove8() {
        List<String> list = new ArrayList(initList);
        List<String> streamList = list.stream().filter(e -> !e.startsWith("李")).collect(Collectors.toList());
        for (String el : streamList) {
            el = "数据接口设计的反馈";
        }
        for (String originalList : list) {
            System.out.println(originalList);
        }
        System.out.println(list.get(0));
    }
}
