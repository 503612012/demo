package com.oven.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.vo.SysDicVo;
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

}
