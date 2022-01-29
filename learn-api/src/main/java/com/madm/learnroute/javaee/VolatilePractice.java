package com.madm.learnroute.javaee;

import org.openjdk.jol.info.ClassLayout;

public class VolatilePractice {
    public static void main(String[] args) {
        VolatilePractice volatilePractice = new VolatilePractice();
        System.out.println(ClassLayout.parseInstance(volatilePractice).toPrintable());
    }
}
