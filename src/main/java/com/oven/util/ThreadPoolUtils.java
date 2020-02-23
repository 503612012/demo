package com.oven.util;

import java.util.concurrent.*;

/**
 * 线程工具类
 *
 * @author Oven
 */
public class ThreadPoolUtils {

    private final ExecutorService service;

    private ThreadPoolUtils(int coreSize) {
        service = new ThreadPoolExecutor(coreSize,
                coreSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(Integer.MAX_VALUE),
                Executors.defaultThreadFactory());
    }

    static class ThreadPoolUtilsHolder {
        static ThreadPoolUtils instance = new ThreadPoolUtils(8);
    }

    public static ExecutorService getInstance() {
        return ThreadPoolUtilsHolder.instance.service;
    }

    public static void shutdown(ExecutorService service) {
        try {
            if (null != service) {
                service.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
