package com.madm.learnroute.javaee;


import com.madm.learnroute.util.FunctionalInterfaceHandleUtil;

public class CglibPractice {
    public static void main(String[] args) {
//        //逻辑核的核数
//        System.out.println(Runtime.getRuntime().availableProcessors());
//        System.out.println(Runtime.getRuntime().totalMemory() / (1024*1024));
//        FunctionalInterfaceHandleUtil.isTureOrFalse(true).trueOrFalseHandle(() -> System.out.println("true，俺要开始秀了"),() -> System.out.println("false，秀不动了，快跑"));
        FunctionalInterfaceHandleUtil.isBlankOrNoBlank("我是一个帅哥").presentOrElseHandle(System.out::println,() -> {
            System.out.println("空字符串");
        });
    }
}
