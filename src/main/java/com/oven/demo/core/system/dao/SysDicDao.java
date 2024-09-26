package com.oven.demo.core.system.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
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

    /**
     * 更新
     */
    public int update(SysDicEntity sysdic) {
        String sql = "update t_sys_dic set `_value` = ?, `_desc` = ? where `dbid` = ?";
        return this.jdbcTemplate.update(sql, sysdic.getValue(), sysdic.getDesc(), sysdic.getId());
    }

    /**
     * 修改状态
     */
    public void updateStatus(Integer id, Integer status) {
        this.jdbcTemplate.update("update t_sys_dic set `_status` = ? where dbid = ?", status, id);
    }

    /**
     * 根据key查询
     */
    public SysDicEntity getByKey(String key) {
        return super.getOne(ConditionAndParams.eq("_key", key));
    }

    public void reduceNum() {
        String sql = "update t_sys_dic set _value = _value - 1 where _key = 'secKill'";
        this.jdbcTemplate.update(sql);
    }

}
