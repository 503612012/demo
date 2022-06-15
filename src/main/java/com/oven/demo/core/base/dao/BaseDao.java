package com.oven.demo.core.base.dao;

import com.oven.demo.common.util.VoPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class BaseDao<T> {

    public List<T> getByPage(StringBuilder sb, List<Object> params, Class<T> clazz, Integer pageNum, Integer pageSize, JdbcTemplate jdbcTemplate) {
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(clazz));
    }

    public List<T> getByPage(StringBuilder sb, List<Object> params, Class<T> clazz, Integer pageNum, Integer pageSize, JdbcTemplate jdbcTemplate, String orderby) {
        sb.append(" order by ").append(orderby);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(clazz));
    }

    public Integer getTotalNum(StringBuilder sb, List<Object> params, JdbcTemplate jdbcTemplate) {
        String sql = sb.toString().replaceFirst("and", "where");
        return jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

}
