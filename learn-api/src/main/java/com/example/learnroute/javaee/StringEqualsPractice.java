package com.example.learnroute.javaee;


import org.openjdk.jol.info.ClassLayout;

import java.util.Objects;

/**
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly
 */
public class StringEqualsPractice {
    public static void main(String[] args) {
//        String abc = new String("abc");
//        String abc1 = new String("abc");
//        System.out.println(abc == abc1);
        String str = "null";
        if(Objects.isNull(str)){
            System.out.println("结果是null");
        }


//        System.out.println(abc1 == abc.intern());
    }
}
