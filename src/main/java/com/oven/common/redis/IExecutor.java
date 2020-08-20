package com.oven.common.redis;

/**
 * redis回调接口
 *
 * @author Oven
 */
public interface IExecutor {

    /**
     * 回调方法
     */
    void execute(IRedisDao redisDao);

}
