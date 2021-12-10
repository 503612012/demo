package com.oven.demo.common.redis.task;

import com.oven.demo.common.redis.IExecutor;
import com.oven.demo.common.redis.IRedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Redis执行任务
 *
 * @author Oven
 */
public class RedisExecuteTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RedisExecuteTask.class);

    private final IRedisDao redisDao;
    private final IExecutor executor;
    private final int retry;

    public RedisExecuteTask(IRedisDao redisDao, IExecutor executor, int retry) {
        this.redisDao = redisDao;
        this.executor = executor;
        this.retry = retry;
    }

    @Override
    public void run() {
        for (int i = 0; i < this.retry; i++) {
            try {
                this.executor.execute(this.redisDao);
                return;
            } catch (Exception e) {
                logger.error("执行redis异步任务时出现异常，异常信息：", e);
            }
            if ((i + 1) < this.retry) {
                this.waitFor();
            }
        }
        logger.error("执行redis异步任务时出现异常，连续尝试{}次未能成功 ......", this.retry);
    }

    /**
     * 睡眠等待timeout
     */
    private void waitFor() {
        try {
            TimeUnit.MILLISECONDS.sleep(TimeUnit.MILLISECONDS.toMillis(100));
        } catch (Exception e) {
            logger.error("睡眠异常：", e);
        }
    }

}
