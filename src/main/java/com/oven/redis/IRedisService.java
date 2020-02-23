package com.oven.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * redis服务层接口
 *
 * @author Oven
 */
@SuppressWarnings("unused")
public interface IRedisService {

    /**
     * 保存字符串值
     */
    boolean setString(String key, String value);

    /**
     * 保存字符串值，设置过期时间
     *
     * @param expire 过期时间，单位秒
     */
    boolean setString(String key, String value, long expire);

    /**
     * 保存字符串值，设置同步/异步方式、重试次数
     *
     * @param syn     true=同步保存、false=异步保存
     * @param retry   获取不到redis连接时，重试次数
     * @param service 异步使用的线程池，syn=false时才有效，syn=true时该参数可为null
     */
    boolean setString(String key, String value, boolean syn, int retry, ExecutorService service);

    /**
     * 保存字符串值，设置过期时间、同步/异步方式、重试次数
     *
     * @param expire  过期时间，单位秒
     * @param syn     true=同步保存、false=异步保存
     * @param retry   获取不到redis连接时，重试次数
     * @param service 异步使用的线程池，syn=false时才有效，syn=true时该参数可为null
     */
    boolean setString(String key, String value, long expire, boolean syn, int retry, ExecutorService service);

    /**
     * 保存对象值
     */
    boolean setObject(String key, Object value);

    /**
     * 保存对象值，设置过期时间
     *
     * @param expire 过期时间，单位秒
     */
    boolean setObject(String key, Object value, long expire);

    /**
     * 保存对象值，设置同步/异步方式、重试次数
     *
     * @param syn     true=同步保存、false=异步保存
     * @param retry   获取不到redis连接时，重试次数
     * @param service 异步使用的线程池，syn=false时才有效，syn=true时该参数可为null
     */
    boolean setObject(String key, Object value, boolean syn, int retry, ExecutorService service);

    /**
     * 保存对象值，设置过期时间、同步/异步方式、重试次数
     *
     * @param expire  过期时间，单位秒
     * @param syn     true=同步保存、false=异步保存
     * @param retry   获取不到redis连接时，重试次数
     * @param service 异步使用的线程池，syn=false时才有效，syn=true时该参数可为null
     */
    boolean setObject(String key, Object value, long expire, boolean syn, int retry, ExecutorService service);

    /**
     * 对象值获取方法
     */
    Object getObject(String key);

    /**
     * 对象值获取方法，设置重试次数
     */
    Object getObject(String key, int retry);

    /**
     * 字符串值获取方法
     */
    String getString(String key);

    /**
     * 字符串值获取方法，设置重试次数
     */
    String getString(String key, int retry);

    /**
     * 删除方法
     */
    boolean remove(String key, int retry);

    /**
     * 删除方法，设置同步/异步方式、重试次数，异步方式时不保证一定能删除，返回true表示已开启线程进行删除。
     *
     * @param syn     true=同步删除、false=异步删除
     * @param retry   获取不到redis连接时，重试次数
     * @param service 异步使用的线程池，syn=false时才有效，syn=true时该参数可为null
     */
    boolean remove(String key, boolean syn, int retry, ExecutorService service);

    /**
     * 获取锁，成功返回锁id（用于释放锁），失败返回null
     *
     * @param key 锁名称
     */
    String acquireLock(String key);

    /**
     * 获取锁，成功返回锁id（用于释放锁），失败返回null
     *
     * @param key    锁名称
     * @param expire 锁的过期时间（占用时间，超过自动释放），单位秒
     * @return 成功时返回锁id，失败时返回null
     */
    String acquireLock(String key, long expire);

    /**
     * 获取锁，一直获取直到超出timeout指定的时间，成功返回锁id（用于释放锁），失败返回null
     *
     * @param key     锁名称
     * @param timeout 获取锁的超时时间
     * @param unit    超时时间单位
     * @return 成功时返回锁id，失败时返回null
     */
    String acquireLock(String key, long timeout, TimeUnit unit);

    /**
     * 获取锁，一直获取直到超出timeout指定的时间，成功返回锁id（用于释放锁），失败返回null
     *
     * @param key     锁名称
     * @param expire  锁的过期时间（占用时间，超过自动释放），单位秒
     * @param timeout 获取锁的超时时间
     * @param unit    超时时间单位
     * @return 成功时返回锁id，失败时返回null
     */
    String acquireLock(String key, long expire, long timeout, TimeUnit unit);

    /**
     * 强制释放锁，该方法有风险（如果锁已过期，又被其他用户获取时，该方法会释放锁，可能会造成并发bug），不提供异步方式
     *
     * @param key 锁名称
     * @return 释放成功返回true，失败返回false
     */
    boolean releaseLock(String key);

    /**
     * 释放锁，只释放指定id的锁
     *
     * @param key    锁名称
     * @param lockId 锁id（为空时表示强制释放，不为空时必须与当前锁的值一样才会释放）
     * @return 释放成功返回true，失败返回false
     */
    boolean releaseLock(String key, String lockId);

    /**
     * 释放锁，只释放指定id的锁，提供同步/异步方式，通常异步方式用于有过期时间的锁（没有设置过期时间的锁不建议用异步方式）
     *
     * @param key     锁名称
     * @param lockId  锁id（为空时表示强制释放，此时异步失效，不为空时必须与当前锁的值一样才会释放）
     * @param syn     true=同步，false=异步
     * @param service 只在syn=false时有效，指定线程池
     */
    boolean releaseLock(String key, String lockId, boolean syn, ExecutorService service);

    /**
     * 通过pipeline方式批量写
     *
     * @param map 批量写的key-value集合
     */
    boolean pipelineWrite(Map<String, Object> map);

    /**
     * 通过pipeline方式批量写，指定过期时间，单位秒
     *
     * @param map    批量写的key-value集合
     * @param expire 过期时间
     */
    boolean pipelineWrite(Map<String, Object> map, long expire);

    /**
     * 通过pipeline方式批量写，指定同步/异步方式写
     *
     * @param map     批量写的key-value集合
     * @param syn     true=同步，false=异步
     * @param service 只在syn=false时有效，指定线程池
     */
    boolean pipelineWrite(Map<String, Object> map, boolean syn, ExecutorService service);

    /**
     * 通过pipeline方式批量写，指定过期时间（单位秒），同步/异步方式写
     *
     * @param map     批量写的key-value集合
     * @param expire  过期时间，单位秒
     * @param syn     true=同步，false=异步
     * @param service 只在syn=false时有效，指定线程池
     */
    boolean pipelineWrite(Map<String, Object> map, long expire, boolean syn, ExecutorService service);

    /**
     * 通过pipeline方式批量读，适合超大key列表，spring-data还没实现集群的pipeline
     *
     * @param list 要批量读取的key列表
     */
    List<Object> pipelineRead(List<String> list);


    /**
     * 管道批量添加集合数据(SET)
     *
     * @param syncData 同步数据
     */
    void pipelineBatchSAdd(Map<String, Set<Object>> syncData);

    /**
     * 管道批量获取集合数据(SET)
     *
     * @param syncData 同步数据
     */
    Map<String, Set<Object>> pipelineBatchSGet(List<String> syncData);

    /**
     * 管道删除数据(Set类型)
     */
    void pipelineSrem(Map<String, Set<String>> value);

    /**
     * spring-redis的multiSet方法
     *
     * @param map 要批量插入的key-value
     */
    boolean multiSet(Map<String, Object> map);

    /**
     * spring-redis的multiSet方法
     *
     * @param map    要批量插入的key-value
     * @param expire 过期时间，单位秒
     */
    boolean multiSet(Map<String, Object> map, long expire);

    /**
     * spring-redis的multiSet方法，支持异步
     *
     * @param map     要批量插入的key-value
     * @param syn     true=同步、false=异步
     * @param service 只在syn=false时有效，指定线程池
     */
    boolean multiSet(Map<String, Object> map, boolean syn, ExecutorService service);

    /**
     * spring-redis的multiSet方法，支持异步
     *
     * @param map     要批量插入的key-value
     * @param expire  过期时间，单位秒
     * @param syn     true=同步、false=异步
     * @param service 只在syn=false时有效，指定线程池
     */
    boolean multiSet(Map<String, Object> map, long expire, boolean syn, ExecutorService service);

    /**
     * spring-redis的multiGet方法
     *
     * @param list 要批量读取的key列表
     */
    List<Object> multiGet(List<String> list);

    /**
     * 清空指定key对应的全部数据(list结构)
     */
    void trim(String key, long startIndex, long endIndex);

    /**
     * 设置集合数据至redis list结构
     */
    Long leftPush(String redisKey, Collection<String> data);


    /**
     * 根据key来清空数据 set结构
     */
    void sRem(String key, Collection<String> data);

    /**
     * 校验值是否存在
     *
     * @param key   redisKey
     * @param value redisValue
     */
    boolean sIsMember(String key, String value);

    /**
     * 管道添加集合数据(set)
     */
    void pipelineSadd(Map<String, String> syncMap);

    /**
     * 添加集合数据(set)
     *
     * @param key   redisKey
     * @param value redisValue
     */
    void sAdd(String key, String value);

    /**
     * 删除集合数据(Set)
     *
     * @param key    redisKey
     * @param values redisValue
     */
    void sRem(String key, String... values);


    /**
     * 添加String类型map集合的一个字段
     */
    boolean hSetString(String key, String field, String value);

    /**
     * 添加String类型map集合
     */
    void hMSetString(String key, Map<String, String> map);

    /**
     * 添加Object类型map集合的一个字段
     */
    boolean hSet(String key, Object field, Object value);

    /**
     * 添加Object类型map集合
     */
    void hMSet(String key, Map<String, Object> map);

    /**
     * 清空指定key的数据
     */
    void delKey(String redisKey);

    /**
     * 删除hash指定域
     *
     * @param key   rediskey
     * @param field hash域
     */
    void hdel(String key, String field);

    /**
     * 获取Object类型map集合的所有字段内容
     */
    Map<String, Object> hGetAll(String key);

    /**
     * 获取String类型map集合的所有字段内容
     */
    Map<String, String> hGetAllString(String key);

    /**
     * 管道清空数据
     */
    void pipelineDel(List<String> keys);

    /**
     * 设置过期时间
     *
     * @param expire 单位：秒
     */
    void expire(String key, long expire);

}
