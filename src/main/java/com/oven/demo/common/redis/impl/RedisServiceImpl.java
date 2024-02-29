package com.oven.demo.common.redis.impl;

import com.oven.demo.common.redis.IExecutor;
import com.oven.demo.common.redis.IRedisDao;
import com.oven.demo.common.redis.IRedisService;
import com.oven.demo.common.redis.task.RedisExecuteTask;
import com.oven.demo.common.redis.util.RedisThreadPoolTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis服务层实现类
 *
 * @author Oven
 */
@Service
public class RedisServiceImpl implements IRedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static final String SPRING_REDIS_CLUSTER_PIPELINE_MAX_NUM = "20000";
    private int maxNum;

    @Resource
    private IRedisDao redisDao;

    @Override
    public boolean setString(final String key, final String value) {
        return this.setString(key, value, -1L, true, 1, null);
    }

    @Override
    public boolean setString(final String key, final String value, final long expire) {
        return this.setString(key, value, expire, true, 1, null);
    }

    @Override
    public boolean setString(final String key, final String value, final boolean syn, final int retry, ExecutorService service) {
        return this.setString(key, value, -1L, syn, retry, service);
    }

    @Override
    public boolean setString(final String key, final String value, final long expire, final boolean syn, final int retry, final ExecutorService service) {
        // 重试次数默认为1次
        final int times = (retry <= 0) ? 1 : retry;
        // 任务执行器
        final IExecutor executor = redisDao -> {
            if (expire <= 0) {
                redisDao.setString(key, value);
            } else {
                redisDao.setString(key, value, expire);
            }
        };

        // 异步保存时，直接返回true，并调用线程池执行加载任务，不能保证一定加载成功
        if (!syn) {
            if (service == null) {
                RedisThreadPoolTools.getInstance().execute(new RedisExecuteTask(this.redisDao, executor, times));
            } else {
                service.execute(new RedisExecuteTask(this.redisDao, executor, times));
            }
            return true;
        }

        // 同步保存
        for (int i = 0; i < times; i++) {
            try {
                executor.execute(this.redisDao);
                return true;
            } catch (Exception e) {
                logger.error("保存key={}时出现异常，异常信息：", key, e);
            }

            if ((i + 1) < times) {
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        }
        logger.error("保存时出现异常，连续尝试{}次未能成功，参数为：key={}, value={}, expire={} ......", times, key, value, expire);
        return false;
    }

    @Override
    public boolean setObject(final String key, final Object value) {
        return this.setObject(key, value, -1L);
    }

    @Override
    public boolean setObject(final String key, final Object value, final long expire) {
        return this.setObject(key, value, expire, true, 1, null);
    }

    @Override
    public boolean setObject(final String key, final Object value, final boolean syn, final int retry, final ExecutorService service) {
        return this.setObject(key, value, -1L, syn, retry, service);
    }

    @Override
    public boolean setObject(final String key, final Object value, final long expire, final boolean syn, final int retry, final ExecutorService service) {
        // 重试次数默认为1次
        final int times = (retry <= 0) ? 1 : retry;
        // 任务执行器
        final IExecutor executor = redisDao -> {
            if (expire <= 0) {
                redisDao.setObject(key, value);
            } else {
                redisDao.setObject(key, value, expire);
            }
        };

        // 异步保存时，直接返回true，并调用线程池执行加载任务，不能保证一定加载成功
        if (!syn) {
            if (service == null) {
                RedisThreadPoolTools.getInstance().execute(new RedisExecuteTask(this.redisDao, executor, times));
            } else {
                service.execute(new RedisExecuteTask(this.redisDao, executor, times));
            }
            return true;
        }
        // 同步保存
        for (int i = 0; i < times; i++) {
            try {
                executor.execute(this.redisDao);
                return true;
            } catch (Exception e) {
                logger.error("保存key={}时出现异常，异常信息：", key, e);
            }

            if ((i + 1) < times) {
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        }
        logger.error("保存时出现异常，连续尝试{}次未能成功，参数为：key={}, value={}, expire={} ......", times, key, value, expire);
        return false;
    }

    @Override
    public String getString(final String key) {
        return this.getString(key, 1);
    }

    @Override
    public String getString(final String key, final int retry) {
        // 重试次数默认为1次
        final int times = (retry <= 0) ? 1 : retry;
        for (int i = 0; i < times; i++) {
            try {
                return this.redisDao.getString(key);
            } catch (Exception e) {
                logger.error("读取key={}时出现异常，异常信息：", key, e);
            }

            if ((i + 1) < times) {
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        }
        logger.error("读取时出现异常，连续尝试{}次未能成功，参数为：key={}, retry={} ......", times, key, retry);
        // 为了区分null和获取异常，这里抛出异常
        throw new RuntimeException("key=" + key + "读取redis数据时发生异常！");
    }

    @Override
    public Object getObject(final String key) {
        return this.getObject(key, 1);
    }

    @Override
    public Object getObject(final String key, final int retry) {
        // 重试次数默认为1次
        final int times = (retry <= 0) ? 1 : retry;
        for (int i = 0; i < times; i++) {
            try {
                return this.redisDao.getObject(key);
            } catch (Exception e) {
                logger.error("读取key={}时出现异常，异常信息：", key, e);
            }

            if ((i + 1) < times) {
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        }
        logger.error("读取时出现异常，连续尝试{}次未能成功，参数为：key={}, retry={} ......", times, key, retry);
        // 为了区分null和获取异常，这里抛出异常
        throw new RuntimeException("key=" + key + "读取redis数据时发生异常！");
    }

    @Override
    public boolean remove(final String key, final int retry) {
        // 重试次数默认为1次
        final int times = (retry <= 0) ? 1 : retry;

        // 同步删除
        for (int i = 0; i < times; i++) {
            try {
                this.redisDao.remove(key);
                return true;
            } catch (Exception e) {
                logger.error("删除key={}时出现异常，异常信息：", key, e);
            }
            if ((i + 1) < times) {
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        }
        logger.error("删除时出现异常，连续尝试{}次未能成功，要删除的参数为：key={}, retry={} ......", times, key, retry);
        return false;
    }

    @Override
    public boolean remove(final String key, final boolean syn, final int retry, final ExecutorService service) {
        // 重试次数默认为1次
        final int times = (retry <= 0) ? 1 : retry;
        // 任务执行器
        final IExecutor executor = redisDao -> redisDao.remove(key);

        // 异步删除时，直接返回true，并调用线程池执行删除任务，不能保证一定删除成功
        if (!syn) {
            if (service == null) {
                RedisThreadPoolTools.getInstance().execute(new RedisExecuteTask(this.redisDao, executor, times));
            } else {
                service.execute(new RedisExecuteTask(this.redisDao, executor, times));
            }
            return true;
        }
        // 同步删除
        for (int i = 0; i < times; i++) {
            try {
                executor.execute(this.redisDao);
                return true;
            } catch (Exception e) {
                logger.error("删除key={}时出现异常，异常信息：", key, e);
            }

            if ((i + 1) < times) {
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        }
        logger.error("删除时出现异常，连续尝试{}次未能成功，要删除的参数为：key={}, syn={} ......", times, key, syn);
        return false;
    }

    @Override
    public String acquireLock(final String key) {
        final String lockId = UUID.randomUUID().toString();
        try {
            boolean lock = this.redisDao.setIfAbsent(key, lockId);
            if (lock) {
                return lockId;
            }
        } catch (Exception e) {
            logger.error("获取锁key={}时出现异常，异常信息：", key, e);
        }
        return null;
    }

    @Override
    public String acquireLock(final String key, final long expire) {
        final String lockId = UUID.randomUUID().toString();
        try {
            boolean lock = this.redisDao.setIfAbsent(key, lockId, expire);
            if (lock) {
                return lockId;
            }
        } catch (Exception e) {
            logger.error("获取锁={}时出现异常，异常信息：", key, e);
        }
        return null;
    }

    @Override
    public String acquireLock(final String key, final long timeout, final TimeUnit unit) {
        final String lockId = UUID.randomUUID().toString();
        final long maxwait = unit.toMillis(timeout);
        final long start = System.currentTimeMillis();
        try {
            while ((System.currentTimeMillis() - start) < maxwait) {
                if (this.redisDao.setIfAbsent(key, lockId)) {
                    return lockId;
                }
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            logger.error("获取锁key={}时出现异常，异常信息：", key, e);
        }
        return null;
    }

    @Override
    public String acquireLock(final String key, final long expire, final long timeout, final TimeUnit unit) {
        final String lockId = UUID.randomUUID().toString();
        final long maxwait = unit.toMillis(timeout);
        final long start = System.currentTimeMillis();
        try {
            while ((System.currentTimeMillis() - start) < maxwait) {
                if (this.redisDao.setIfAbsent(key, lockId, expire)) {
                    return lockId;
                }
                waitFor(100, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            logger.error("获取锁key={}时出现异常，异常信息：", key, e);
        }
        return null;
    }

    @Override
    public boolean releaseLock(final String key) {
        try {
            this.redisDao.remove(key);
            return true;
        } catch (Exception e) {
            logger.error("释放锁key={}时出现异常，异常信息：", key, e);
        }
        return false;
    }

    @Override
    public boolean releaseLock(final String key, final String lockId) {
        return this.releaseLock(key, lockId, true, null);
    }

    @Override
    public boolean releaseLock(final String key, final String lockId, final boolean syn, final ExecutorService service) {
        try {
            // lockId为空时强制释放锁，强制使用同步方式
            if (StringUtils.isEmpty(lockId)) {
                return this.releaseLock(key);
            }

            Object currentLock = this.redisDao.getObject(key);
            // 当前锁为空直接返回
            if (null == currentLock) {
                return true;
            }

            // 锁id相同时，释放锁
            if (lockId.equals(currentLock)) {
                // 任务执行器
                final IExecutor executor = redisDao -> redisDao.remove(key);
                // 异步释放
                if (!syn) {
                    if (service == null) {
                        RedisThreadPoolTools.getInstance().execute(new RedisExecuteTask(this.redisDao, executor, 1));
                    } else {
                        service.execute(new RedisExecuteTask(this.redisDao, executor, 1));
                    }
                    return true;
                }

                // 同步释放
                executor.execute(this.redisDao);
                return true;
            }
            // 锁id不一样表示自己的锁已过期，锁被其他人使用，不需要释放
            return true;
        } catch (Exception e) {
            logger.error("释放锁key={}时出现异常，异常信息：", key, e);
        }
        return false;
    }

    @Override
    public boolean pipelineWrite(final Map<String, Object> map) {
        return this.pipelineWrite(map, -1L, true, null);
    }

    @Override
    public boolean pipelineWrite(final Map<String, Object> map, final long expire) {
        return this.pipelineWrite(map, expire, true, null);
    }

    @Override
    public boolean pipelineWrite(final Map<String, Object> map, final boolean syn, final ExecutorService service) {
        return this.pipelineWrite(map, -1L, syn, service);
    }

    @Override
    public boolean pipelineWrite(final Map<String, Object> map, final long expire, final boolean syn, final ExecutorService service) {
        if (map == null || map.isEmpty()) {
            return true;
        }

        try {
            // 拆分成1w条一组，防止一次性写太多导致redis崩溃
            final List<Map<String, Object>> list = this.splitMap(map);
            // 任务执行器
            final IExecutor executor = redisDao -> {
                for (Map<String, Object> update : list) {
                    if (expire <= 0L) {
                        redisDao.pipelineSet(update);
                    } else {
                        redisDao.pipelineSet(update, expire, TimeUnit.SECONDS);
                    }
                }
            };
            // 异步方式无法保证一定保存成功，返回true表示已启动线程进行处理
            if (!syn) {
                if (service == null) {
                    RedisThreadPoolTools.getInstance().execute(new RedisExecuteTask(this.redisDao, executor, 1));
                } else {
                    service.execute(new RedisExecuteTask(this.redisDao, executor, 1));
                }
                return true;
            }

            // 同步方式保存
            executor.execute(this.redisDao);
            return true;
        } catch (Exception e) {
            logger.error("pipeline批量保存出现异常，异常信息：", e);
        }
        return false;
    }

    /**
     * 拆分redis写数据集合，每1w条一个批次
     */
    private List<Map<String, Object>> splitMap(Map<String, Object> map) {
        try {
            maxNum = Integer.parseInt(SPRING_REDIS_CLUSTER_PIPELINE_MAX_NUM);
        } catch (Exception e) {
            logger.error("从数据字典获取相关配置失败", e);
        }

        final int maxPerPipeline = Math.max(this.maxNum, 1000);
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> temp = new HashMap<>(16);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (temp.size() % maxPerPipeline == 0) {
                temp = new HashMap<>(16);
                result.add(temp);
            }
            temp.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @Override
    public List<Object> pipelineRead(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        List<Object> result = new ArrayList<>();
        try {
            List<List<String>> keyList = this.splitList(list);
            for (List<String> readList : keyList) {
                result.addAll(this.redisDao.pipelineGet(readList));
            }
            return result;
        } catch (Exception e) {
            logger.error("pipeline批量读取出现异常，异常信息：", e);
        }
        return null;
    }

    @Override
    public void pipelineBatchSAdd(Map<String, Set<Object>> syncData) {
        if (syncData == null || syncData.isEmpty()) {
            return;
        }
        try {
            redisDao.pipelineBatchSAdd(syncData);
        } catch (Exception e) {
            logger.error("--pipelineBatchSAdd批量保存出现异常，异常信息：", e);
        }
    }

    @Override
    public Map<String, Set<Object>> pipelineBatchSGet(List<String> syncData) {
        Map<String, Set<Object>> resultMap = new HashMap<>();
        try {
            if (syncData == null || syncData.isEmpty()) {
                return resultMap;
            }
            List<List<String>> keyList = this.splitList(syncData);
            for (List<String> readList : keyList) {
                resultMap.putAll(this.redisDao.pipelineBatchSGet(readList));
            }
        } catch (Exception e) {
            logger.error("pipeline批量读取出现异常，异常信息：", e);
        }
        return resultMap;
    }

    @Override
    public void pipelineSrem(Map<String, Set<String>> value) {
        this.redisDao.pipelineSRem(value);
    }

    /**
     * 拆分redis读数据集合，每1w条一个批次
     */
    private List<List<String>> splitList(List<String> list) {
        try {
            maxNum = Integer.parseInt(SPRING_REDIS_CLUSTER_PIPELINE_MAX_NUM);
        } catch (Exception e) {
            logger.error("从数据字典获取相关配置失败", e);
        }

        final int maxPerPipeline = Math.max(this.maxNum, 1000);
        List<List<String>> result = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        for (String key : list) {
            if (temp.size() % maxPerPipeline == 0) {
                temp = new ArrayList<>();
                result.add(temp);
            }
            temp.add(key);
        }
        return result;
    }

    @Override
    public boolean multiSet(Map<String, Object> map) {
        return this.multiSet(map, -1L, true, null);
    }

    @Override
    public boolean multiSet(Map<String, Object> map, long expire) {
        return this.multiSet(map, expire, true, null);
    }

    @Override
    public boolean multiSet(Map<String, Object> map, boolean syn, ExecutorService service) {
        return this.multiSet(map, -1L, syn, service);
    }

    @Override
    public boolean multiSet(final Map<String, Object> map, final long expire, final boolean syn, final ExecutorService service) {
        if (map == null || map.isEmpty()) {
            return true;
        }

        try {
            // 任务执行器
            final IExecutor executor = redisDao -> {
                if (expire <= 0L) {
                    redisDao.multiSet(map);
                } else {
                    redisDao.multiSet(map, expire, TimeUnit.SECONDS);
                }
            };
            // 异步方式无法保证一定保存成功，返回true表示已启动线程进行处理
            if (!syn) {
                if (service == null) {
                    RedisThreadPoolTools.getInstance().execute(new RedisExecuteTask(this.redisDao, executor, 1));
                } else {
                    service.execute(new RedisExecuteTask(this.redisDao, executor, 1));
                }
                return true;
            }

            // 同步方式保存
            executor.execute(this.redisDao);
            return true;
        } catch (Exception e) {
            logger.error("multiSet批量保存出现异常，异常信息：", e);
        }
        return false;
    }

    @Override
    public List<Object> multiGet(List<String> list) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        try {
            return this.redisDao.multiGet(list);
        } catch (Exception e) {
            logger.error("multGet批量读取出现异常，异常信息：", e);
        }
        return null;
    }

    @Override
    public void trim(String key, long startIndex, long endIndex) {
        try {
            this.redisDao.trim(key, startIndex, endIndex);
        } catch (Exception e) {
            logger.error("调用redis list清空方法异常：", e);
        }
    }

    @Override
    public Long leftPush(String redisKey, Collection<String> value) {
        try {
            return this.redisDao.leftPush(redisKey, value);
        } catch (Exception e) {
            logger.error("调用redis list清空方法异常：", e);
            return 0L;
        }
    }

    @Override
    public void sRem(String key, Collection<String> data) {
        this.redisDao.sRemData(key, data);
    }

    @Override
    public boolean sIsMember(String key, String value) {
        return this.redisDao.sIsMember(key, value);
    }

    @Override
    public void pipelineSadd(Map<String, String> syncMap) {
        this.redisDao.pipelineSadd(syncMap);
    }

    @Override
    public void sAdd(String key, String value) {
        this.redisDao.sAdd(key, value);
    }

    @Override
    public void sRem(String key, String... values) {
        this.redisDao.sRem(key, values);
    }

    @Override
    public boolean hSetString(String key, String field, String value) {
        return this.redisDao.hSetString(key, field, value);
    }

    @Override
    public void hMSetString(String key, Map<String, String> map) {
        this.redisDao.hMSetString(key, map);
    }

    @Override
    public boolean hSet(String key, Object field, Object value) {
        return this.redisDao.hSet(key, field, value);
    }

    @Override
    public void hMSet(String key, Map<String, Object> map) {
        this.redisDao.hMSet(key, map);
    }

    @Override
    public void delKey(String redisKey) {
        this.redisDao.delKey(redisKey);
    }

    @Override
    public void hdel(String key, String field) {
        this.redisDao.hdel(key, field);
    }

    @Override
    public Map<String, Object> hGetAll(String key) {
        return this.redisDao.hGetAll(key);
    }

    @Override
    public Map<String, String> hGetAllString(String key) {
        return this.redisDao.hGetAllString(key);
    }

    @Override
    public void pipelineDel(List<String> keys) {
        this.redisDao.pipelineDel(keys);
    }

    /**
     * 设置过期时间
     *
     * @param expire 单位：秒
     */
    @Override
    public void expire(String key, long expire) {
        this.redisDao.expire(key, expire);
    }

    /**
     * 睡眠等待timeout
     */
    public static void waitFor(long timeout, TimeUnit unit) {
        if (timeout <= 0 || null == unit) {
            return;
        }
        try {
            TimeUnit.MILLISECONDS.sleep(unit.toMillis(timeout));
        } catch (Exception e) {
            logger.error("睡眠以后：", e);
        }
    }

}
