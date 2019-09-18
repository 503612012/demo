package com.oven.util;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 消息队列工具类
 *
 * @author Oven
 */
public class QueueUtils {

    private final BlockingDeque<String> logQueue = new LinkedBlockingDeque<>();

    private QueueUtils() {
    }

    private static class LogQueueUtilsHolder {
        static QueueUtils instance = new QueueUtils();
    }

    public static QueueUtils getInstance() {
        return LogQueueUtilsHolder.instance;
    }

    public void offer(String info) {
        this.logQueue.offer(info);
    }

    public String poll() {
        return this.logQueue.poll();
    }

}
