package com.oven.core.crontab.service;

import com.oven.core.base.service.BaseService;
import com.oven.core.crontab.dao.CrontabDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CrontabService extends BaseService {

    @Resource
    private CrontabDao crontabDao;

    /**
     * 根据key获取cron表达式
     */
    public String getCron(String key) {
        return crontabDao.getCron(key);
    }

}
