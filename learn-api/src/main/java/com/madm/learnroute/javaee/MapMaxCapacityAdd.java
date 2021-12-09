package com.madm.learnroute.javaee;

import com.madm.learnroute.netty.codec.User;
import org.apache.lucene.util.RamUsageEstimator;
import org.aspectj.weaver.ast.Var;

public class MapMaxCapacityAdd {
    private volatile Object myobj = new Object();
    private User user = new User();

    public static void main(String[] args) {
        User user = new User();
        long x = RamUsageEstimator.sizeOfObject(user);
        System.out.println(x);
    }
}
