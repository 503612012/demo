package com.oven.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig {

    /**
     * 异步记录日志线程线程池
     */
    @Bean("asyncExecLogExecutor")
    public Executor asyncExecLogExecutor() {
        log.info("asyncExecLogExecutor start");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(100);
        // 配置最大线程数
        executor.setMaxPoolSize(100);
        // 配置队列大小
        executor.setQueueCapacity(99999);
        // 配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-asyncExecLogExecutor-");
        // 执行初始化
        executor.initialize();
        return executor;
    }

}
