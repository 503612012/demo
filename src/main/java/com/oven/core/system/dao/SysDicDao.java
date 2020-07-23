package com.oven.core.system.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.core.system.vo.SysDicVo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统级字典dao层
 *
 * @author Oven
 */
@Repository
public class SysDicDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询所有
     */
    public List<SysDicVo> findAll() {
        String sql = "select * from t_sys_dic";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(SysDicVo.class));
    }

    public String getByKey(String key) {
        String sql = "select _value from t_sys_dic where _key = ?";
        return this.jdbcTemplate.queryForObject(sql, String.class, key);
    }

    public void reduceNum() {
        String sql = "update t_sys_dic set _value = _value - 1 where _key = 'secKill'";
        this.jdbcTemplate.update(sql);
    }

}
