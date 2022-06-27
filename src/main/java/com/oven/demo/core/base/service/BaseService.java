package com.oven.demo.core.base.service;

import com.oven.demo.framework.cache.CacheService;
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
    private CacheService cacheService;

    /**
     * 读缓存
     */
    public <T> T get(String key) {
        return cacheService.get(key);
    }

    /**
     * 写缓存
     */
    public <T> void set(String key, T obj) {
        cacheService.set(key, obj);
    }

    /**
     * 批量移除缓存
     */
    public void batchRemove(String... key) {
        cacheService.batchRemove(key);
    }

}
