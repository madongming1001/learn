package com.madm.learnroute.javaee;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class MyAbstractQueueSynchronized extends AbstractQueuedSynchronizer {
    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }

    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }
}
