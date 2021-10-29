package com.example.learnroute.os;

import org.apache.lucene.util.RamUsageEstimator;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author madongming
 */
public class MemoryBarrier {

    /**
     * 问这个代码最后输出什么？申请多大的内存空间，都在什么位置申请的
     * @param args
     */
    public static void main(String[] args) {
        int[] a = new int[1024];
        System.out.println(RamUsageEstimator.sizeOf(a));
        System.out.println(ClassLayout.parseClass(MemoryBarrier.class).toPrintable());
    }

}
