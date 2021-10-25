package com.example.learnroute.javaee;

import cn.hutool.core.util.ReUtil;

import java.util.ArrayList;
import java.util.Objects;

/**
 * jdk6:‐Xms6M ‐Xmx6M ‐XX:PermSize=6M ‐XX:MaxPermSize=6M
 * jdk8:‐Xms6M ‐Xmx6M ‐XX:MetaspaceSize=6M ‐XX:MaxMetaspaceSize=6M
 * VM Args: -Xms10m -Xmx10m -XX:+PrintGcDetail
 *
 */
public class RuntimeConstantPoolOOMPractice {
    public static void main(String[] args) {
//        ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < 10000000; i++) {
//            String str = String.valueOf(i).intern();
//            list.add(str);
//        }
        if(Objects.equals(1338, new Long(1338))){
            System.out.println("所穿参数相等");
        }


    }
}
