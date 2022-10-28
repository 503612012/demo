package com.oven.demo.common.redis.impl;

import com.oven.demo.common.redis.IRedisDao;
import com.oven.demo.common.redis.util.JedisClusterPipeline;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConverters;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis缓存操作实现类
 *
 * @author Oven
 */
@Component
public class RedisDaoImpl implements IRedisDao {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setString(final String key, final String value) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[] rawValue = stringSerializer.serialize(value);

        this.redisTemplate.execute(connection -> {
            assert rawValue != null;
            assert rawKey != null;
            connection.set(rawKey, rawValue);
            return null;
        }, true);
    }

    /**
     * @param timeout 过期时间，小于0时不设置过期时间
     * @param unit    过期时间单位
     */
    @Override
    public void setString(final String key, final String value, final long timeout, final TimeUnit unit) {
        final TimeUnit timeUnit = (null == unit ? TimeUnit.SECONDS : unit);
        if (timeout <= 0L) {
            this.setString(key, value);
            return;
        }

        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[] rawValue = stringSerializer.serialize(value);
        this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            assert rawValue != null;
            connection.setEx(rawKey, TimeoutUtils.toSeconds(timeout, timeUnit), rawValue);
            return null;
        }, true);
    }

    /**
     * @param expire 过期时间，小于0时不设置过期时间，单位秒
     */
    @Override
    public void setString(final String key, final String value, final long expire) {
        this.setString(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * @param date 过期日期，为null时不设置过期时间
     */
    @Override
    public void setString(final String key, final String value, final Date date) {
        final long timeout = (null == date) ? -1L : (date.getTime() - System.currentTimeMillis());
        if (timeout > 0L) {
            this.setString(key, value, timeout, TimeUnit.MILLISECONDS);
        } else {
            this.setString(key, value);
        }
    }

    @Override
    public void setObject(final String key, final Object value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param timeout 过期时间，小于0时不设置过期时间
     * @param unit    过期时间单位
     */
    @Override
    public void setObject(final String key, final Object value, final long timeout, final TimeUnit unit) {
        final TimeUnit timeUnit = (null == unit ? TimeUnit.SECONDS : unit);
        if (timeout > 0L) {
            this.redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        } else {
            this.setObject(key, value);
        }
    }

    /**
     * @param expire 过期时间，单位秒
     */
    @Override
    public void setObject(final String key, final Object value, final long expire) {
        this.setObject(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * @param date 过期日期，为null时表示不设置过期时间
     */
    @Override
    public void setObject(final String key, final Object value, final Date date) {
        final long timeout = (null == date) ? -1L : (date.getTime() - System.currentTimeMillis());
        if (timeout > 0L) {
            this.setObject(key, value, timeout, TimeUnit.MILLISECONDS);
        } else {
            this.setObject(key, value);
        }
    }

    @Override
    public boolean setIfAbsent(final String key, final Object value) {
        return Objects.requireNonNull(this.redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    @Override
    public boolean setIfAbsent(final String key, final Object value, final long timeout, final TimeUnit unit) {
        final TimeUnit timeUnit = (null == unit ? TimeUnit.SECONDS : unit);
        boolean lock = this.setIfAbsent(key, value);
        if (lock && timeout > 0L) {
            try {
                this.expire(key, timeout, timeUnit);
            } catch (Exception e) {
                // 设置过期时间失败时必须删除已设置的key
                this.remove(key);
                return false;
            }
        }
        return lock;
    }

    /**
     * @param expire 过期时间，单位秒
     */
    @Override
    public boolean setIfAbsent(final String key, final Object value, final long expire) {
        return this.setIfAbsent(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean setIfAbsent(final String key, final Object value, final Date date) {
        final long timeout = (null == date) ? -1L : (date.getTime() - System.currentTimeMillis());
        if (timeout > 0L) {
            return this.setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
        } else {
            return this.setIfAbsent(key, value);
        }
    }

    @Override
    public String getString(final String key) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        return this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return stringSerializer.deserialize(connection.get(rawKey));
        }, true);
    }

    @Override
    public Object getObject(final String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean containsKey(final String key) {
        return Objects.requireNonNull(this.redisTemplate.hasKey(key));
    }

    @Override
    public void rename(final String key, final String newKey) {
        this.redisTemplate.boundValueOps(key).rename(newKey);
    }

    @Override
    public void remove(final String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public Set<String> keys(final String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    /**
     * @param expire 过期时间，单位秒
     */
    @Override
    public boolean expire(final String key, final long expire) {
        return this.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    @Override
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        final TimeUnit timeUnit = (null == unit ? TimeUnit.SECONDS : unit);
        return timeout > 0L && Objects.requireNonNull(this.redisTemplate.expire(key, timeout, timeUnit));
    }

    /**
     * @param date 过期日期
     */
    @Override
    public boolean expire(final String key, final Date date) {
        return null != date && Objects.requireNonNull(this.redisTemplate.expireAt(key, date));
    }

    @Override
    public Long getExpire(final String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * @param unit 过期时间单位，为null时，默认返回秒
     */
    @Override
    public Long getExpire(final String key, final TimeUnit unit) {
        final TimeUnit timeUnit = (null == unit ? TimeUnit.SECONDS : unit);
        return this.redisTemplate.getExpire(key, timeUnit);
    }

    @Override
    public boolean sAdd(final String key, final String... values) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[][] rawValues = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            rawValues[i] = stringSerializer.serialize(values[i]);
        }

        final Long result = this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.sAdd(rawKey, rawValues);
        }, true);

        // 返回1表示添加成功，返回0表示要添加的values已经存在
        return result != null && (result > 0);
    }

    @Override
    public boolean sRem(final String key, final String... values) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[][] rawValues = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            rawValues[i] = stringSerializer.serialize(values[i]);
        }

        final Long result = this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.sRem(rawKey, rawValues);
        }, true);

        // 返回1表示删除成功，返回0表示要删除的values不存在
        return result != null && (result > 0);
    }

    @Override
    public void sRemData(String key, Collection<String> data) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[][] rawValues = new byte[data.size()][];
        int i = 0;
        for (String value : data) {
            rawValues[i++] = stringSerializer.serialize(value);
        }
        this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.sRem(rawKey, rawValues);
        }, true);
    }

    @Override
    public Set<String> sMembers(final String key) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);

        final Set<byte[]> rawValues = this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.sMembers(rawKey);
        }, true);

        final Set<String> result = new HashSet<>();
        if (rawValues != null) {
            for (byte[] value : rawValues) {
                result.add(stringSerializer.deserialize(value));
            }
        }
        return result;
    }

    @Override
    public void pipelineSet(final Map<String, Object> map) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();

        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        // 单点模式支持pipeline
        if (redisConnection.isPipelined()) {
            this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    byte[] rawKey = stringSerializer.serialize(entry.getKey());
                    byte[] rawValue = valueSerializer.serialize(entry.getValue());
                    assert rawValue != null;
                    assert rawKey != null;
                    connection.set(rawKey, rawValue);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    byte[] rawKey = stringSerializer.serialize(entry.getKey());
                    byte[] rawValue = valueSerializer.serialize(entry.getValue());
                    pipeline.set(rawKey, rawValue);
                }
                // 同步到redis集群
                pipeline.sync();
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    /**
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    @Override
    public void pipelineSet(final Map<String, Object> map, final long timeout, final TimeUnit unit) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();

        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        final long expire = TimeoutUtils.toSeconds(timeout, unit);
        // 单点模式支持pipeline
        if (redisConnection.isPipelined()) {
            this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    byte[] rawKey = stringSerializer.serialize(entry.getKey());
                    byte[] rawValue = valueSerializer.serialize(entry.getValue());
                    assert rawValue != null;
                    assert rawKey != null;
                    connection.setEx(rawKey, expire, rawValue);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    byte[] rawKey = stringSerializer.serialize(entry.getKey());
                    byte[] rawValue = valueSerializer.serialize(entry.getValue());
                    pipeline.setex(rawKey, (int) expire, rawValue);
                }
                // 同步到redis集群
                pipeline.sync();
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    public List<Object> pipelineGet(final List<String> list) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final RedisSerializer<?> valueSerializer = this.redisTemplate.getValueSerializer();

        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        // 单点模式支持pipeline
        if (redisConnection.isPipelined()) {
            return this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : list) {
                    byte[] rawKey = stringSerializer.serialize(key);
                    assert rawKey != null;
                    connection.get(rawKey);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (String key : list) {
                    byte[] rawKey = stringSerializer.serialize(key);
                    pipeline.get(rawKey);
                }
                // 获取redis批量返回数据
                final List<Object> values = pipeline.syncAndReturnAll();

                // 解析返回值
                final List<Object> result = new ArrayList<>();
                for (Object rawValue : values) {
                    result.add(valueSerializer.deserialize((byte[]) rawValue));
                }
                return result;
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    public void pipelineSRem(final Map<String, Set<String>> value) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) this.redisTemplate.getValueSerializer();

        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        // 单点模式支持pipeline
        if (redisConnection.isPipelined()) {
            this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : value.keySet()) {
                    Set<String> cleanValues = value.get(key);
                    byte[] rawKey = stringSerializer.serialize(key);
                    byte[][] rawValues = new byte[cleanValues.size()][];
                    int i = 0;
                    for (String value1 : cleanValues) {
                        rawValues[i++] = valueSerializer.serialize(value1);
                    }
                    assert rawKey != null;
                    connection.sRem(rawKey, rawValues);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (String key : value.keySet()) {
                    Set<String> cleanValues = value.get(key);
                    byte[] rawKey = stringSerializer.serialize(key);
                    byte[][] rawValues = new byte[cleanValues.size()][];
                    int i = 0;
                    for (String cleanValue : cleanValues) {
                        rawValues[i++] = valueSerializer.serialize(cleanValue);
                    }
                    pipeline.srem(rawKey, rawValues);
                }
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    public void trim(String key, final long startIndex, final long endIndex) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            connection.lTrim(rawKey, startIndex, endIndex);
            return null;
        }, true);
    }

    @Override
    public Long leftPush(String redisKey, Collection<String> values) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();
        final byte[] rawKey = stringSerializer.serialize(redisKey);
        final byte[][] rawValues = new byte[values.size()][];
        int i = 0;
        for (Object value : values) {
            rawValues[i++] = valueSerializer.serialize(value);
        }
        return redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.lPush(rawKey, rawValues);
        }, true);
    }

    @Override
    public boolean sIsMember(String key, String value) {
        return Objects.requireNonNull(this.redisTemplate.opsForSet().isMember(key, value));
    }

    @Override
    public void pipelineSadd(final Map<String, String> values) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) this.redisTemplate.getValueSerializer();

        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        // 单点模式支持pipeline
        if (redisConnection.isPipelined()) {
            this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : values.keySet()) {
                    String value = values.get(key);
                    byte[] rawKey = stringSerializer.serialize(key);
                    byte[] rawValues = valueSerializer.serialize(value);
                    assert rawKey != null;
                    connection.sAdd(rawKey, rawValues);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (String key : values.keySet()) {
                    String value = values.get(key);
                    byte[] rawKey = stringSerializer.serialize(key);
                    byte[] rawValue = valueSerializer.serialize(value);
                    pipeline.sadd(rawKey, rawValue);
                }
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void pipelineBatchSAdd(final Map<String, Set<Object>> syncData) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) this.redisTemplate.getValueSerializer();
        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        if (redisConnection.isPipelined()) { // 单点模式支持pipeline
            this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : syncData.keySet()) {
                    Set<Object> setValue = syncData.get(key);
                    final byte[][] rawValues = new byte[setValue.size()][];
                    int i = 0;
                    for (Object setValues : setValue) {
                        rawValues[i++] = valueSerializer.serialize(setValues.toString());
                    }
                    byte[] rawKey = stringSerializer.serialize(key);
                    assert rawKey != null;
                    connection.sAdd(rawKey, rawValues);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (String key : syncData.keySet()) {
                    Set<Object> setValue = syncData.get(key);
                    final byte[][] rawValues = new byte[setValue.size()][];
                    int i = 0;
                    for (Object setValues : setValue) {
                        rawValues[i++] = valueSerializer.serialize(setValues.toString());
                    }
                    byte[] rawKey = stringSerializer.serialize(key);
                    pipeline.sadd(rawKey, rawValues);
                }
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Set<Object>> pipelineBatchSGet(final List<String> syncData) {
        final Map<String, Set<Object>> result = new HashMap<>();
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final RedisSerializer<?> valueSerializer = this.redisTemplate.getValueSerializer();
        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        if (redisConnection.isPipelined()) { // 单点模式支持pipeline
            return this.redisTemplate.execute(connection -> {
                for (String key : syncData) {
                    byte[] rawKey = stringSerializer.serialize(key);
                    assert rawKey != null;
                    Set<byte[]> bytesValues = connection.sMembers(rawKey);
                    Set<Object> setValues = new HashSet<>();
                    assert bytesValues != null;
                    for (byte[] tempByte : bytesValues) {
                        setValues.add(valueSerializer.deserialize(tempByte));
                    }
                    result.put(key, setValues);
                }
                return result;
            }, true);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (String syncDatum : syncData) {
                    byte[] rawKey = stringSerializer.serialize(syncDatum);
                    pipeline.smembers(rawKey);
                }
                final List<Object> values = pipeline.syncAndReturnAll();
                for (int i = 0; i < values.size(); i++) {
                    Object rawValue = values.get(i);
                    Set<byte[]> setByte = (Set<byte[]>) rawValue;
                    Set<Object> tempSet = new HashSet<>();
                    for (byte[] tempByte : setByte) {
                        tempSet.add(valueSerializer.deserialize(tempByte));
                    }
                    result.put(syncData.get(i), tempSet);
                }
                return result;
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    public void delKey(String redisKey) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(redisKey);
        this.redisTemplate.execute(connection -> {
            connection.del(rawKey);
            return null;
        }, true);
    }

    @Override
    public void hdel(String key, String field) {
        this.redisTemplate.opsForHash().delete(key, field);
    }

    @Override
    public void pipelineDel(final List<String> keys) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<String> valueSerializer = (RedisSerializer<String>) this.redisTemplate.getValueSerializer();

        final RedisConnection redisConnection = Objects.requireNonNull(this.redisTemplate.getConnectionFactory()).getConnection();
        final Object nativeConnection = redisConnection.getNativeConnection();
        // 单点模式支持pipeline
        if (redisConnection.isPipelined()) {
            this.redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                for (String key : keys) {
                    byte[] rawKey = stringSerializer.serialize(key);
                    connection.del(rawKey);
                }
                return null;
            }, valueSerializer);
            // 集群模式不支持pipeline，只能自己实现
        } else if (nativeConnection instanceof JedisCluster) {
            try (JedisClusterPipeline pipeline = JedisClusterPipeline.pipelined((JedisCluster) nativeConnection)) {
                // 刷新元数据
                pipeline.refreshCluster();
                for (String key : keys) {
                    pipeline.del(key);
                }
            } catch (Exception e) {
                throw JedisConverters.toDataAccessException(e);
            } finally {
                RedisConnectionUtils.releaseConnection(redisConnection, this.redisTemplate.getConnectionFactory());
            }
        } else {
            throw new UnsupportedOperationException("Pipeline is not supported for currently mode.");
        }
    }

    @Override
    public void multiSet(final Map<String, Object> map) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();

        this.redisTemplate.execute(connection -> {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                byte[] rawKey = stringSerializer.serialize(entry.getKey());
                byte[] rawValue = valueSerializer.serialize(entry.getValue());
                assert rawKey != null;
                assert rawValue != null;
                connection.set(rawKey, rawValue);
            }
            return null;
        }, true);
    }

    @Override
    public void multiSet(final Map<String, Object> map, final long timeout, final TimeUnit unit) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();
        final long expire = TimeoutUtils.toSeconds(timeout, unit);

        this.redisTemplate.execute(connection -> {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                byte[] rawKey = stringSerializer.serialize(entry.getKey());
                byte[] rawValue = valueSerializer.serialize(entry.getValue());
                assert rawValue != null;
                assert rawKey != null;
                connection.setEx(rawKey, expire, rawValue);
            }
            return null;
        }, true);
    }

    @Override
    public List<Object> multiGet(final List<String> list) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) this.redisTemplate.getValueSerializer();

        return this.redisTemplate.execute(connection -> {
            List<Object> result = new ArrayList<>();
            for (String key : list) {
                byte[] rawKey = stringSerializer.serialize(key);
                assert rawKey != null;
                byte[] rawValue = connection.get(rawKey);
                result.add(valueSerializer.deserialize(rawValue));
            }
            return result;
        }, true);
    }

    @Override
    public boolean hSetString(final String key, final String field, final String value) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[] rawField = stringSerializer.serialize(field);
        final byte[] rawValue = stringSerializer.serialize(value);

        return Objects.requireNonNull(this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            assert rawField != null;
            assert rawValue != null;
            return connection.hSet(rawKey, rawField, rawValue);
        }, true));
    }

    @Override
    public void hMSetString(String key, Map<String, String> map) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final Map<byte[], byte[]> hashes = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            hashes.put(stringSerializer.serialize(entry.getKey()), stringSerializer.serialize(entry.getValue()));
        }

        this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            connection.hMSet(rawKey, hashes);
            return null;
        }, true);
    }

    @Override
    public boolean hSet(String key, Object field, Object value) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> hashKeySerializer = (RedisSerializer<Object>) this.redisTemplate.getHashKeySerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> hashValueSerializer = (RedisSerializer<Object>) this.redisTemplate.getHashValueSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[] rawField = hashKeySerializer.serialize(field);
        final byte[] rawValue = hashValueSerializer.serialize(value);

        return Objects.requireNonNull(this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            assert rawField != null;
            assert rawValue != null;
            return connection.hSet(rawKey, rawField, rawValue);
        }, true));
    }

    @Override
    public void hMSet(String key, Map<String, Object> map) {
        this.redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public String hGetString(String key, String field) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[] rawField = stringSerializer.serialize(field);

        return this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            assert rawField != null;
            return stringSerializer.deserialize(connection.hGet(rawKey, rawField));
        }, true);
    }

    @Override
    public List<String> hMGetString(String key, String... fields) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);
        final byte[][] rawFields = new byte[fields.length][];

        List<byte[]> list = this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.hMGet(rawKey, rawFields);
        }, true);

        if (list == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (byte[] value : list) {
            result.add(stringSerializer.deserialize(value));
        }
        return result;
    }

    @Override
    public Map<String, String> hGetAllString(String key) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);

        Map<byte[], byte[]> map = this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.hGetAll(rawKey);
        }, true);

        if (map == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>(16);
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            result.put(stringSerializer.deserialize(entry.getKey()), stringSerializer.deserialize(entry.getValue()));
        }
        return result;
    }

    @Override
    public Object hGet(String key, Object field) {
        return this.redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public List<Object> hMGet(String key, Object... fields) {
        return this.redisTemplate.opsForHash().multiGet(key, Arrays.asList(fields));
    }

    @Override
    public Map<String, Object> hGetAll(String key) {
        final RedisSerializer<String> stringSerializer = this.redisTemplate.getStringSerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<String> hashKeySerializer = (RedisSerializer<String>) this.redisTemplate.getHashKeySerializer();
        @SuppressWarnings("unchecked") final RedisSerializer<Object> hashValueSerializer = (RedisSerializer<Object>) this.redisTemplate.getHashValueSerializer();
        final byte[] rawKey = stringSerializer.serialize(key);

        Map<byte[], byte[]> map = this.redisTemplate.execute(connection -> {
            assert rawKey != null;
            return connection.hGetAll(rawKey);
        }, true);

        if (map == null) {
            return null;
        }
        Map<String, Object> result = new HashMap<>(16);
        for (Map.Entry<byte[], byte[]> entry : map.entrySet()) {
            result.put(hashKeySerializer.deserialize(entry.getKey()), hashValueSerializer.deserialize(entry.getValue()));
        }
        return result;
    }

}
