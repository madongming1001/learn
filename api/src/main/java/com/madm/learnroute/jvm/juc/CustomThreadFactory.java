package com.madm.learnroute.jvm.juc;

import java.util.concurrent.ThreadFactory;

/**
 * @author dongming.ma
 * @date 2022/7/12 00:13
 */
public class CustomThreadFactory implements ThreadFactory {
    private String name;

    public CustomThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(name);
    }
}
