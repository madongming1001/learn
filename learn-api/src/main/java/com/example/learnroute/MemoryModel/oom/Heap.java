package com.example.learnroute.MemoryModel.oom;

import java.util.ArrayList;

/**
 * vm args: -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
 */
public class Heap {
    static class OOMObject{

    }

    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        while (true){
            list.add(new OOMObject());
        }
    }
}
