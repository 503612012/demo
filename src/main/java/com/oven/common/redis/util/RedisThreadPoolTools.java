package com.oven.common.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Redis线程池
 *
 * @author Oven
 */
public class RedisThreadPoolTools {

    private static final Logger logger = LoggerFactory.getLogger(RedisThreadPoolTools.class);

    private final ExecutorService service;

    private RedisThreadPoolTools() {
        this.service = Executors.newFixedThreadPool(10);
    }

    private RedisThreadPoolTools(int size) {
        this.service = Executors.newFixedThreadPool(size);
    }

    static class RedisThreadPoolHolder {
        static RedisThreadPoolTools INSTANCE = new RedisThreadPoolTools();
    }

    /**
     * 并发发送线程池
     */
    public static RedisThreadPoolTools getInstance() {
        return RedisThreadPoolHolder.INSTANCE;
    }

    public void execute(Runnable r) {
        try {
            this.service.execute(r);
        } catch (Exception e) {
            logger.error("线程调度发生异常，异常信息如下：", e);
        }
    }

}
