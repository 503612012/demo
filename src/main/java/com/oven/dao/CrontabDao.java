package com.oven.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时任务Dao层
 *
 * @author Oven
 */
@Repository
public class CrontabDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据key获取cron表达式
     */
    public String getCron(String key) {
        String sql = "select cron from t_crontab where _key='" + key + "'";
        List<String> list = this.jdbcTemplate.queryForList(sql, String.class);
        return list.size() == 0 ? null : list.get(0);
    }

}
