package com.madm.learnroute.funcationinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SupplierTest {
    public static void main(String[] args) {
        List<Double> list = getRandomValue(5, () -> Math.random() * 100);
        for (Double d : list) {
            System.out.println(d);
        }
    }

    private static List<Double> getRandomValue(int num, Supplier<Double> sup) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(sup.get());
        }
        return list;
    }
}
