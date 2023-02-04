package com.madm.learnroute.common;

import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.MDC;

import static com.madm.learnroute.constant.TraceLogEnum.TRACE_LOG_ID;
import static com.madm.learnroute.technology.spring.interceptor.TraceLogInterceptor.getTraceId;

/**
 * @author dongming.ma
 * @date 2023/1/17 15:09
 */
public class ThreadMdcUtil {

    public static void setTraceIdIfAbsent() {
        if (MDC.get(TRACE_LOG_ID.getValue()) == null) {
            MDC.put(TRACE_LOG_ID.getValue(), getTraceId());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
