package com.madm.learnroute.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author dongming.ma
 * @date 2023/8/24 20:31
 */
@Configuration
@Slf4j
public class ExecutorConfig {

    @Bean(name = "normalExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("personal");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(30);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryDecorator(executor));
        executor.initialize();
        return executor;
    }

    @AllArgsConstructor
    class ThreadFactoryDecorator implements ThreadFactory {
        private ThreadFactory raw;

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = raw.newThread(r);
            thread.setUncaughtExceptionHandler((t1, t2) -> {
                log.error("Exception in thread {},原因是：", t1.getName(), ExceptionUtil.getMessage(t2));
            });
            return null;
        }
    }
}