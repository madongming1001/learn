package com.example.learnroute.javaee;

import com.example.learnroute.pojo.Teacher;
import org.openjdk.jol.info.ClassLayout;

public class ObjectHeadSizePractice {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new Teacher()).toPrintable());
    }
}
