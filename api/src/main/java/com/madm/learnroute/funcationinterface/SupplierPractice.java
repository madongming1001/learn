package com.madm.learnroute.funcationinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;


public class SupplierPractice {
    public static void main(String[] args) {
//        List<Double> list = getRandomValue(5, () -> Math.random() * 100);
//        for (Double d : list) {
//            System.out.println(d);
//        }
        String a = "1";
        boolean b = false;

        Function function = Objects.equals(a, "2") ? (s) -> {
            s = true;
            return 1;
        } : (s) -> 2;

        System.out.println(function.apply(b));


    }

    private static List<Double> getRandomValue(int num, Supplier<Double> sup) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(sup.get());
        }
        return list;
    }
}
