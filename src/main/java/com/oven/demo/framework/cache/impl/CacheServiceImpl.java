package com.oven.demo.framework.cache.impl;

import com.oven.demo.framework.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 缓存实现类
 *
 * @author Oven
 */
@Slf4j
@Service
public class CacheServiceImpl implements CacheService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 查询缓存
     *
     * @param key 缓存键 不可为空
     */
    @Override
    public <T> T get(String key) {
        return get(key, null, null, null);
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     */
    @Override
    public <T> T get(String key, Function<String, T> function) {
        return get(key, function, key, null);
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm function函数的调用参数
     */
    @Override
    public <T, X> T get(String key, Function<X, T> function, X funcParm) {
        return get(key, function, funcParm, null);
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    @Override
    public <T> T get(String key, Function<String, T> function, Long expireTime) {
        return get(key, function, key, expireTime);
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm   function函数的调用参数
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, X> T get(String key, Function<X, T> function, X funcParm, Long expireTime) {
        T obj = null;
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            obj = (T) operations.get(key);
            if (function != null && obj == null) {
                obj = function.apply(funcParm);
                if (obj != null) {
                    set(key, obj, expireTime); // 设置缓存信息
                }
            }
        } catch (Exception e) {
            log.error("读取redis缓存异常：", e);
        }
        return obj;
    }

    /**
     * 设置缓存键值 直接向缓存中插入值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key 缓存键 不可为空
     * @param obj 缓存值 不可为空
     */
    @Override
    public <T> void set(String key, T obj) {
        set(key, obj, null);
    }

    /**
     * 设置缓存键值 直接向缓存中插入值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key        缓存键 不可为空
     * @param obj        缓存值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    @Override
    public <T> void set(String key, T obj, Long expireTime) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        if (obj == null) {
            return;
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, obj);
        if (null != expireTime) {
            redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键 不可为空
     */
    @Override
    public void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        redisTemplate.delete(key);
    }

    /**
     * 是否存在缓存
     *
     * @param key 缓存键 不可为空
     */
    @Override
    public boolean contains(String key) {
        boolean exists = false;
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }
        return exists;
    }

    /**
     * 批量移除缓存
     *
     * @param key 缓存键 不可为空
     */
    @Override
    public void batchRemove(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.localBatchRemove(key[0]);
            } else {
                @SuppressWarnings("rawtypes") List keys = CollectionUtils.arrayToList(key);
                for (Object item : keys) {
                    this.localBatchRemove((String) item);
                }
            }
        }

    }

    private void localBatchRemove(String key) {
        Set<String> set = redisTemplate.keys(key + "*");
        if (set != null) {
            for (String item : set) {
                redisTemplate.delete(item);
            }
        }
    }

}
