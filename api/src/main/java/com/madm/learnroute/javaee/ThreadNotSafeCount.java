package com.madm.learnroute.javaee;

public class ThreadNotSafeCount {
    private Long value;

    public Long getCount() {
        return value;
    }

    public void inc() {
        ++value;
    }
}
