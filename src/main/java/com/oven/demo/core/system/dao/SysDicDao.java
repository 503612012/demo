package com.oven.demo.core.system.dao;

import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.system.entity.SysDicEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 系统级字典dao层
 *
 * @author Oven
 */
@Repository
public class SysDicDao extends BaseDao<SysDicEntity> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public String getByKey(String key) {
        String sql = "select _value from t_sys_dic where _key = ?";
        return this.jdbcTemplate.queryForObject(sql, String.class, key);
    }

    public void reduceNum() {
        String sql = "update t_sys_dic set _value = _value - 1 where _key = 'secKill'";
        this.jdbcTemplate.update(sql);
    }

}
