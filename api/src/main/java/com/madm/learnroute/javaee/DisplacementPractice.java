package com.madm.learnroute.javaee;

import java.util.Properties;

/**
 * @author dongming.ma
 * @date 2022/10/8 19:26
 */
public class DisplacementPractice {
    public static void main(String[] args) {
        Properties properties = System.getProperties();
        System.out.println(properties.getProperty("java.vm.name"));
        System.out.println(Integer.toBinaryString(2));
        System.out.println(Integer.toBinaryString(2 << 68));
    }
}
