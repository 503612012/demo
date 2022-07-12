package com.oven.demo.common.util;

import com.oven.demo.core.log.entity.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列工具类
 *
 * @author Oven
 */
public class LogQueueUtils {

    private final static Integer POP_MAX_COUNT = 1000;

    private final BlockingQueue<Log> logQueue = new LinkedBlockingQueue<>();

    private LogQueueUtils() {
    }

    private static class LogQueueUtilsHolder {
        static LogQueueUtils instance = new LogQueueUtils();
    }

    public static LogQueueUtils getInstance() {
        return LogQueueUtilsHolder.instance;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void offer(Log log) {
        this.logQueue.offer(log);
    }

    public List<Log> drainTo(Integer max) {
        List<Log> list = new ArrayList<>();
        if (max == null || max <= 0) {
            max = POP_MAX_COUNT;
        }
        this.logQueue.drainTo(list, max);
        return list;
    }

}
