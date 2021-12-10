package com.oven.demo.common.util;

import com.oven.demo.common.requestLog.RequestLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列工具类
 *
 * @author Oven
 */
public class RequestLogQueueUtils {

    private final static Integer POP_MAX_COUNT = 10000;

    private final BlockingQueue<RequestLog> reqeustLogQueue = new LinkedBlockingQueue<>();

    private RequestLogQueueUtils() {
    }

    private static class LogQueueUtilsHolder {
        static RequestLogQueueUtils instance = new RequestLogQueueUtils();
    }

    public static RequestLogQueueUtils getInstance() {
        return LogQueueUtilsHolder.instance;
    }

    public void offer(RequestLog log) {
        this.reqeustLogQueue.offer(log);
    }

    public List<RequestLog> drainTo(Integer max) {
        List<RequestLog> list = new ArrayList<>();
        if (max == null || max <= 0) {
            max = POP_MAX_COUNT;
        }
        this.reqeustLogQueue.drainTo(list, max);
        return list;
    }

}
