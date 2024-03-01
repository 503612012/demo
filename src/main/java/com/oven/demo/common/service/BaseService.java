package com.oven.demo.common.service;

import com.oven.demo.common.redis.IRedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 基类服务
 *
 * @author Oven
 */
@Service
public class BaseService {

    @Resource
    private IRedisService redisService;

    /**
     * 读缓存
     */
    public <T> T get(String key) {
        return redisService.get(key);
    }

    /**
     * 写缓存
     */
    public <T> void set(String key, T obj) {
        redisService.set(key, obj);
    }

    /**
     * 移除缓存
     */
    public void remove(String key) {
        redisService.remove(key);
    }

    /**
     * 批量移除缓存
     */
    public void batchRemove(String... key) {
        redisService.batchRemove(key);
    }

}
