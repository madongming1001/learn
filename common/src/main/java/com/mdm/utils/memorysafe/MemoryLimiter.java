package com.mdm.utils.memorysafe;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author dongming.ma
 * @date 2022/6/11 11:40
 */
public class MemoryLimiter {
    private final Instrumentation inst;
    private final LongAdder memory = new LongAdder();
    private final ReentrantLock acquireLock = new ReentrantLock();
    private final Condition notLimited = acquireLock.newCondition();
    private final ReentrantLock releaseLock = new ReentrantLock();
    private final Condition notEmpty = releaseLock.newCondition();
    private long memoryLimit;

    public MemoryLimiter(Instrumentation inst) {
        this(Integer.MAX_VALUE, inst);
    }

    public MemoryLimiter(long memoryLimit, Instrumentation inst) {
        if (memoryLimit <= 0) {
            throw new IllegalArgumentException();
        }
        this.memoryLimit = memoryLimit;
        this.inst = inst;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        if (memoryLimit <= 0) {
            throw new IllegalArgumentException();
        }
        this.memoryLimit = memoryLimit;
    }

    public long getCurrentMemory() {
        return memory.sum();
    }

    public long getCurrentRemainMemory() {
        return getMemoryLimit() - getCurrentMemory();
    }

    private void signalNotEmpty() {
        releaseLock.lock();
        try {
            notEmpty.signal();
        } finally {
            releaseLock.unlock();
        }
    }

    private void signalNotLimited() {
        acquireLock.lock();
        try {
            notLimited.signal();
        } finally {
            acquireLock.unlock();
        }
    }

    /**
     * Locks to prevent both acquires and releases.
     */
    private void fullyLock() {
        acquireLock.lock();
        releaseLock.lock();
    }

    /**
     * Unlocks to allow both acquires and releases.
     */
    private void fullyUnlock() {
        releaseLock.unlock();
        acquireLock.unlock();
    }

    public boolean acquire(Object e) {
        if (e == null) {
            throw new NullPointerException();
        }
        if (memory.sum() >= memoryLimit) {
            return false;
        }
        acquireLock.lock();
        try {
            final long sum = memory.sum();
            final long objectSize = inst.getObjectSize(e);
            if (sum + objectSize >= memoryLimit) {
                return false;
            }
            memory.add(objectSize);
            // see https://github.com/apache/incubator-shenyu/pull/3356
            if (memory.sum() < memoryLimit) {
                notLimited.signal();
            }
        } finally {
            acquireLock.unlock();
        }
        if (memory.sum() > 0) {
            signalNotEmpty();
        }
        return true;
    }

    public void acquireInterruptibly(Object e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        acquireLock.lockInterruptibly();
        try {
            final long objectSize = inst.getObjectSize(e);
            // see https://github.com/apache/incubator-shenyu/pull/3335
            while (memory.sum() + objectSize >= memoryLimit) {
                notLimited.await();
            }
            memory.add(objectSize);
            if (memory.sum() < memoryLimit) {
                notLimited.signal();
            }
        } finally {
            acquireLock.unlock();
        }
        if (memory.sum() > 0) {
            signalNotEmpty();
        }
    }

    public boolean acquire(Object e, long timeout, TimeUnit unit) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        acquireLock.lockInterruptibly();
        try {
            final long objectSize = inst.getObjectSize(e);
            while (memory.sum() + objectSize >= memoryLimit) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = notLimited.awaitNanos(nanos);
            }
            memory.add(objectSize);
            if (memory.sum() < memoryLimit) {
                notLimited.signal();
            }
        } finally {
            acquireLock.unlock();
        }
        if (memory.sum() > 0) {
            signalNotEmpty();
        }
        return true;
    }

    public void release(Object e) {
        if (null == e) {
            return;
        }
        if (memory.sum() == 0) {
            return;
        }
        releaseLock.lock();
        try {
            final long objectSize = inst.getObjectSize(e);
            if (memory.sum() > 0) {
                memory.add(-objectSize);
                if (memory.sum() > 0) {
                    notEmpty.signal();
                }
            }
        } finally {
            releaseLock.unlock();
        }
        if (memory.sum() < memoryLimit) {
            signalNotLimited();
        }
    }

    public void releaseInterruptibly(Object e) throws InterruptedException {
        if (null == e) {
            return;
        }
        releaseLock.lockInterruptibly();
        try {
            final long objectSize = inst.getObjectSize(e);
            while (memory.sum() == 0) {
                notEmpty.await();
            }
            memory.add(-objectSize);
            if (memory.sum() > 0) {
                notEmpty.signal();
            }
        } finally {
            releaseLock.unlock();
        }
        if (memory.sum() < memoryLimit) {
            signalNotLimited();
        }
    }

    public void releaseInterruptibly(Object e, long timeout, TimeUnit unit) throws InterruptedException {
        if (null == e) {
            return;
        }
        long nanos = unit.toNanos(timeout);
        releaseLock.lockInterruptibly();
        try {
            final long objectSize = inst.getObjectSize(e);
            while (memory.sum() == 0) {
                if (nanos <= 0) {
                    return;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            memory.add(-objectSize);
            if (memory.sum() > 0) {
                notEmpty.signal();
            }
        } finally {
            releaseLock.unlock();
        }
        if (memory.sum() < memoryLimit) {
            signalNotLimited();
        }
    }

    public void clear() {
        fullyLock();
        try {
            if (memory.sumThenReset() < memoryLimit) {
                notLimited.signal();
            }
        } finally {
            fullyUnlock();
        }
    }
}
