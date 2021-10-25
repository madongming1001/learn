package com.example.learnroute.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * -Xms100m -Xmx100m -XX:+UseSerialGC
 */
public class OOMObject {
    /**
     * 内存占位符对象，一个OOMObject大约占64KB
     */
    public byte[] placeholder = new byte[64 * 1024];

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
        // 稍作延时，令监视曲线的变化更加明显
         Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws Exception {
//        fillHeap(1000);
        int runtimeCount = 0;
        for (long pid : new long[]{1338L,1337L,1336L,1335L}) {
            runtimeCount++;
            if (Objects.equals(pid, 1338L)) {
                System.out.println("相等");
            }
        }
        System.out.println("一共运行了:"+runtimeCount);
    }

}


