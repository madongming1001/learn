package com.mdm.function;

import org.springframework.beans.factory.BeanFactory;

import java.util.function.Function;

public class FunctionTest {
//    public static void main(String[] args) {
//
//        String str = strHandler("一花一世界，一叶一菩提！", s -> s.substring(BeanFactory.FACTORY_BEAN_PREFIX.length()));
//        System.out.println(str);
//    }
    public static String strHandler(String str, Function<String, String> fun) {
        return str = fun.apply(str);
    }
}
