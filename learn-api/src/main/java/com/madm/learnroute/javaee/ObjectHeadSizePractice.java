package com.madm.learnroute.javaee;

import com.madm.pojo.Teacher;
import org.openjdk.jol.info.ClassLayout;

public class ObjectHeadSizePractice {

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseInstance(new Teacher()).toPrintable());
    }
}
