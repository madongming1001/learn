package com.madm.learnroute.funcationinterface;

import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {

        String str = strHandler("一花一世界，一叶一菩提！", s -> s.substring(2,5));
        System.out.println(str);
    }
    public static String strHandler(String str, Function<String, String> fun) {
        return fun.apply(str);
    }
}
