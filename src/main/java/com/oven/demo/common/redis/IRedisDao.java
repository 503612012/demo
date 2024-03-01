package com.oven.demo.common.redis;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * redis缓存操作接口
 *
 * @author Oven
 */
@SuppressWarnings("unused")
public interface IRedisDao {

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParam  function函数的调用参数
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    <T, X> T get(String key, Function<X, T> function, X funcParam, Long expireTime);

    /**
     * 设置缓存键值
     *
     * @param key        缓存键 不可为空
     * @param obj        缓存值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     */
    <T> void set(String key, T obj, Long expireTime);

    /**
     * 批量移除缓存
     *
     * @param key 缓存键 不可为空
     */
    void batchRemove(String... key);

    /**
     * 保存指定key和String类型值到redis缓存
     *
     * @param key   要保存的key
     * @param value 要保存的String类型值
     */
    void setString(String key, String value);

    /**
     * 保存指定key和String类型值到redis缓存，指定过期时间和单位
     *
     * @param key     要保存的key
     * @param value   要保存的String类型值
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    void setString(String key, String value, long timeout, TimeUnit unit);

    /**
     * 保存指定key和String类型值到redis缓存，指定过期时间，单位秒
     *
     * @param key    要保存的key
     * @param value  要保存的String类型值
     * @param expire 过期时间
     */
    void setString(String key, String value, long expire);

    /**
     * 保存指定key和String类型值到redis缓存，指定过期日期
     *
     * @param key   要保存的key
     * @param value 要保存的String类型值
     * @param date  过期日期
     */
    void setString(String key, String value, Date date);

    /**
     * 保存指定key到redis缓存
     *
     * @param key   要保存的key
     * @param value 要保存的值
     */
    void setObject(String key, Object value);

    /**
     * 保存指定key到redis缓存，指定过期时间和单位
     *
     * @param key     要保存的key
     * @param value   要保存的值
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    void setObject(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 保存指定key到redis缓存，指定过期时间，单位秒
     *
     * @param key    要保存的key
     * @param value  要保存的值
     * @param expire 过期时间
     */
    void setObject(String key, Object value, long expire);

    /**
     * 保存指定key到redis缓存，指定过期日期
     *
     * @param key   要保存的key
     * @param value 要保存的值
     * @param date  过期日期
     */
    void setObject(String key, Object value, Date date);

    /**
     * cas锁实现，不设置过期时间（永久锁，必须主动释放）
     *
     * @param key   要获取锁的名称
     * @param value 锁的值，通常使用某个id以在主动释放时判断是否自己的锁
     * @return 获取成功=true、失败=false
     */
    boolean setIfAbsent(String key, Object value);

    /**
     * cas锁实现，指定过期时间，单位秒（临时锁，如果不主动释放，到指定时间后自动释放）
     *
     * @param key    要获取锁的名称
     * @param value  锁的值，通常使用某个id以在主动释放时判断是否自己的锁
     * @param expire 过期时间
     * @return 获取成功=true、失败=false
     */
    boolean setIfAbsent(String key, Object value, long expire);

    /**
     * cas锁实现，指定过期日期（临时锁，如果不主动释放，到指定时间后自动释放）
     *
     * @param key   要获取锁的名称
     * @param value 锁的值，通常使用某个id以在主动释放时判断是否自己的锁
     * @param date  过期日期
     * @return 获取成功=true、失败=false
     */
    boolean setIfAbsent(String key, Object value, Date date);

    /**
     * cas锁实现，指定过期时间和单位（临时锁，如果不主动释放，到指定时间后自动释放）
     *
     * @param key     要获取锁的名称
     * @param value   锁的值，通常使用某个id以在主动释放时判断是否自己的锁
     * @param timeout 过期时间
     * @param unit    过期时间单位
     * @return 获取成功=true、失败=false
     */
    boolean setIfAbsent(String key, Object value, long timeout, TimeUnit unit);

    /**
     * 获取指定key的String值
     *
     * @param key 要获取的key
     */
    String getString(String key);

    /**
     * 获取指定key的值
     *
     * @param key 要获取的key
     */
    Object getObject(String key);

    /**
     * 是否包含指定的key
     *
     * @param key 要查询的key
     */
    boolean containsKey(String key);

    /**
     * 修改key的名称
     *
     * @param key    要修改的key
     * @param newKey 修改后的key
     */
    void rename(String key, String newKey);

    /**
     * 删除指定key
     *
     * @param key 要删除的key
     */
    void remove(String key);

    /**
     * 获取指定pattern的所有key
     *
     * @param pattern 要获取的pattern
     */
    Set<String> keys(String pattern);

    /**
     * 设置key的过期时间，单位秒
     *
     * @param key    要设置的key
     * @param expire 过期时间
     */
    boolean expire(String key, long expire);

    /**
     * 设置key的过期时间，指定到date
     *
     * @param key  要设置的key
     * @param date 过期的日期
     */
    boolean expire(String key, Date date);

    /**
     * 设置key的过期时间，指定单位
     *
     * @param key     要设置的key
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 获取key的过期时间，单位秒
     *
     * @param key 要获取的key
     */
    Long getExpire(String key);

    /**
     * 获取key的过期时间，指定单位
     *
     * @param key  要获取的key
     * @param unit 过期时间的单位
     */
    Long getExpire(String key, TimeUnit unit);

    /**
     * 添加String类型set集合元素
     *
     * @param key    集合的key
     * @param values 要添加的String类型元素数组
     */
    boolean sAdd(String key, String... values);

    /**
     * 删除String类型set集合的元素
     *
     * @param key    集合的key
     * @param values 要删除的String类型元素数组
     */
    boolean sRem(String key, String... values);

    /**
     * 删除集合类型的set集合元素
     */
    void sRemData(String key, Collection<String> data);

    /**
     * 获取String类型set集合的内容
     *
     * @param key 集合的key
     */
    Set<String> sMembers(String key);

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
     * 获取String类型map集合的一个字段内容
     */
    String hGetString(String key, String field);

    List<String> hMGetString(String key, String... fields);

    /**
     * 获取String类型map集合的所有字段内容
     */
    Map<String, String> hGetAllString(String key);

    /**
     * 获取Object类型map集合的一个字段内容
     */
    Object hGet(String key, Object field);

    /**
     * 获取Object类型map集合的多个字段内容
     */
    List<Object> hMGet(String key, Object... fields);

    /**
     * 获取Object类型map集合的所有字段内容
     */
    Map<String, Object> hGetAll(String key);

    /**
     * 批量写redis缓存
     *
     * @param map 批量写的key-value集合
     */
    void multiSet(Map<String, Object> map);

    /**
     * 批量写redis缓存
     *
     * @param map     批量写的key-value集合
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    void multiSet(Map<String, Object> map, long timeout, TimeUnit unit);

    /**
     * 批量获取redis内容
     *
     * @param list key列表
     */
    List<Object> multiGet(List<String> list);

    /**
     * spring-data cluster暂时不支持pipeline方式，自己实现
     *
     * @param map 批量写的key-value集合
     */
    void pipelineSet(Map<String, Object> map);

    /**
     * spring-data cluster暂时不支持pipeline方式，自己实现
     *
     * @param map     批量写的key-value集合
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    void pipelineSet(Map<String, Object> map, long timeout, TimeUnit unit);

    /**
     * spring-data cluster暂时不支持pipeline方式，自己实现
     *
     * @param list key列表
     */
    List<Object> pipelineGet(List<String> list);

    /**
     * 管道删除数据(set类型)
     */
    void pipelineSRem(Map<String, Set<String>> value);

    /**
     * 清空指定key对应的全部数据(list结构)
     */
    void trim(String key, long startIndex, long endIndex);

    /**
     * 设置集合数据至redis list结构
     */
    Long leftPush(String redisKey, Collection<String> data);

    /**
     * 校验指定值是否存在
     *
     * @param key   redisKey
     * @param value redisValue
     */
    boolean sIsMember(String key, String value);

    /**
     * 管道添加集合数据(SET)
     *
     * @param syncMap 同步数据
     */
    void pipelineSadd(Map<String, String> syncMap);

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
     * 清空指定key的数据
     *
     * @param redisKey rediskey
     */
    void delKey(String redisKey);

    /**
     * 删除hash指定域
     *
     * @param key   rediskey
     * @param field hash域
     */
    void hdel(String key, String field);

    void pipelineDel(List<String> keys);

}
