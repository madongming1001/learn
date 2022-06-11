package com.madm.learnroute.javaee;

import com.madm.learnroute.technology.netty.codec.User;
import org.apache.lucene.util.RamUsageEstimator;

import java.util.HashMap;

public class MapMaxCapacityAdd {
    private volatile Object myobj = new Object();
    private User user = new User();

    public static void main(String[] args) {
        User user = new User();
        Object put = new HashMap<>().put("1", "1");
        System.out.println(RamUsageEstimator.shallowSizeOf(put));
    }
}
