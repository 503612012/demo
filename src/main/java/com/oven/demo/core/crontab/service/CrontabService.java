package com.oven.demo.core.crontab.service;

import cn.hutool.core.util.StrUtil;
import com.oven.demo.common.constant.CacheKeyConst;
import com.oven.demo.common.redis.IRedisService;
import com.oven.demo.core.crontab.dao.CrontabDao;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * 定时任务服务层
 *
 * @author Oven
 */
@Service
public class CrontabService {

    @Resource
    private CrontabDao crontabDao;
    @Resource
    private IRedisService redisService;

    /**
     * 根据key获取cron表达式
     */
    public String getCron(String key) {
        String result = redisService.getString(StrUtil.format(CacheKeyConst.GET_CRONTAB_BY_KEY, key)); // 先读取缓存
        if (StringUtils.isEmpty(result)) { // double check
            synchronized (this) {
                result = redisService.get(StrUtil.format(CacheKeyConst.GET_CRONTAB_BY_KEY, key)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = crontabDao.getCron(key);
                    redisService.setString(StrUtil.format(CacheKeyConst.GET_CRONTAB_BY_KEY, key), result, 86400);
                }
            }
        }
        return result;
    }

}
