package com.oven.demo.core.crontab.service;

import com.oven.demo.core.crontab.dao.CrontabDao;
import org.springframework.stereotype.Service;

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

    /**
     * 根据key获取cron表达式
     */
    public String getCron(String key) {
        return crontabDao.getCron(key);
    }

}
