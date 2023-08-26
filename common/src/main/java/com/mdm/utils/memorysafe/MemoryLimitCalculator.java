package com.mdm.utils.memorysafe;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dongming.ma
 * @date 2022/6/11 11:39
 */
public class MemoryLimitCalculator {
    // 计算对象所占内存大小的两种方式
    // 1、Instrumentation
    // 它可以更加方便的做字节码增强操作，允许我们对已经加载甚至还没有被加载的类进行修改的操作，实现类似于性能监控的功能
    // 2、ManagementFactory，JConsole的页面信息就是从 ManagementFactory 里面拿的
    private static final MemoryMXBean MX_BEAN = ManagementFactory.getMemoryMXBean();
    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();
    private static volatile long maxAvailable;

    static {
        // immediately refresh when this class is loaded to prevent maxAvailable from being 0
        refresh();
        // check every 50 ms to improve performance
        SCHEDULER.scheduleWithFixedDelay(MemoryLimitCalculator::refresh, 50, 50, TimeUnit.MILLISECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(SCHEDULER::shutdown));
    }

    private static void refresh() {
        final MemoryUsage usage = MX_BEAN.getHeapMemoryUsage();
        maxAvailable = usage.getCommitted();
    }

    /**
     * Get the maximum available memory of the current JVM.
     *
     * @return maximum available memory
     */
    public static long maxAvailable() {
        return maxAvailable;
    }

    /**
     * Take the current JVM's maximum available memory
     * as a percentage of the result as the limit.
     *
     * @param percentage percentage
     * @return available memory
     */
    public static long calculate(final float percentage) {
        if (percentage <= 0 || percentage > 1) {
            throw new IllegalArgumentException();
        }
        return (long) (maxAvailable() * percentage);
    }

    /**
     * By default, it takes 80% of the maximum available memory of the current JVM.
     *
     * @return available memory
     */
    public static long defaultLimit() {
        return (long) (maxAvailable() * 0.8);
    }
}
