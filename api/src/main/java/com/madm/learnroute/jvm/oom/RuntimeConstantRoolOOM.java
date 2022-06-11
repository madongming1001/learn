package com.madm.learnroute.jvm.oom;

import java.util.HashSet;

/**
 * VM Args: 1.6 -XX:PermSize=6m -XX:MaxPermSize=6m
 * VM Args: 1.8 -Xmx6m
 */
public class RuntimeConstantRoolOOM {
    public static void main(String[] args) {
        HashSet<String> set = new HashSet<String>();
        short i = 0;
        while (true){
            set.add(String.valueOf(i++).intern());
        }
    }
}
