package com.oven.demo.core.crontab.service;

import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.service.BaseService;
import com.oven.demo.core.crontab.dao.CrontabDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * 定时任务服务层
 *
 * @author Oven
 */
@Service
public class CrontabService extends BaseService {

    @Resource
    private CrontabDao crontabDao;

    /**
     * 根据key获取cron表达式
     */
    public String getCron(String key) {
        String result = super.get(MessageFormat.format(RedisCacheKey.GET_CRON_BY_KEY, key)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.GET_CRON_BY_KEY, key)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = crontabDao.getCron(key);
                    super.set(MessageFormat.format(RedisCacheKey.GET_CRON_BY_KEY, key), result);
                }
            }
        }
        return result;
    }

}
