package com.mdm.function;

import java.lang.reflect.Type;
import java.util.function.Function;

public class FunctionTest {
    public static void main(String[] args) {

        Type[] genericInterfaces = strHandler().getClass().getGenericInterfaces();
    }

    public static Function<Integer, Integer> strHandler() {
        return x -> (int) Math.pow(x, 10);
    }
}
