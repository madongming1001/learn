package com.madm.learnroute.config;

import com.madm.learnroute.common.ThreadPoolExecutorMdcWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * @author dongming.ma
 * @date 2023/2/18 11:20
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public Executor requestExecutor() {
        ThreadPoolExecutorMdcWrapper threadPool = new ThreadPoolExecutorMdcWrapper("asyncCustomThread");
        return threadPool;
    }
}
