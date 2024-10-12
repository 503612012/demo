package com.oven.demo.core.crontab.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import org.springframework.stereotype.Repository;

/**
 * 定时任务dao层
 *
 * @author Oven
 */
@Repository
public class CrontabDao extends BaseDao<Object> {

    /**
     * 根据key获取cron表达式
     */
    public String getCron(String key) {
        return getOneString(ConditionAndParams.eq("_key", key), "cron", "t_crontab");
    }

}
