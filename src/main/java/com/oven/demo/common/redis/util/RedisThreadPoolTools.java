package com.oven.demo.common.redis.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Redis线程池
 *
 * @author Oven
 */
@Slf4j
public class RedisThreadPoolTools {

    private final ExecutorService service;

    private RedisThreadPoolTools() {
        this.service = Executors.newFixedThreadPool(10);
    }

    private RedisThreadPoolTools(int size) {
        this.service = Executors.newFixedThreadPool(size);
    }

    static class RedisThreadPoolHolder {
        static RedisThreadPoolTools instance = new RedisThreadPoolTools();
    }

    /**
     * 并发发送线程池
     */
    public static RedisThreadPoolTools getInstance() {
        return RedisThreadPoolHolder.instance;
    }

    public void execute(Runnable r) {
        try {
            this.service.execute(r);
        } catch (Exception e) {
            log.error("线程调度发生异常，异常信息如下：", e);
        }
    }

}
