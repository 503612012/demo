package com.oven.demo.core.worksite.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.worksite.vo.Worksite;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 工地dao层
 *
 * @author Oven
 */
@Repository
public class WorksiteDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 分页查询工地
     */
    public List<Worksite> getByPage(Integer pageNum, Integer pageSize, Worksite worksite) {
        StringBuilder sb = new StringBuilder("select * from t_worksite ws");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, worksite);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(Worksite.class));
    }

    /**
     * 统计工地总数量
     */
    public Integer getTotalNum(Worksite worksite) {
        StringBuilder sb = new StringBuilder("select count(*) from t_worksite ws");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, worksite);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 添加
     */
    public int add(Worksite worksite) {
        String sql = "insert into t_worksite (`dbid`," +
                "                             `name`," +
                "                             `status`," +
                "                             `desc`," +
                "                             `create_id`," +
                "                             `create_time`," +
                "                             `last_modify_id`," +
                "                             `last_modify_time`)" +
                "                      values (null, ?, 0, ?, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, worksite.getName());
            ps.setString(2, worksite.getDesc());
            ps.setInt(3, worksite.getCreateId());
            ps.setString(4, worksite.getCreateTime());
            ps.setInt(5, worksite.getLastModifyId());
            ps.setString(6, worksite.getLastModifyTime());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 更新
     */
    public int update(Worksite worksite) {
        String sql = "update t_worksite set `name` = ?," +
                "                           `desc` = ?," +
                "                           `status` = ?," +
                "                           `create_id` = ?," +
                "                           `create_time` = ?," +
                "                           `last_modify_id` = ?," +
                "                           `last_modify_time` = ?" +
                "                     where `dbid` = ?";
        return this.jdbcTemplate.update(sql, worksite.getName(), worksite.getDesc(), worksite.getStatus(), worksite.getCreateId(),
                worksite.getCreateTime(), worksite.getLastModifyId(), worksite.getLastModifyTime(), worksite.getId());
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_worksite where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 通过主键查询
     */
    public Worksite getById(Integer id) {
        String sql = "select * from t_worksite where dbid = ?";
        List<Worksite> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Worksite.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 查询所有工地
     */
    public List<Worksite> getAll() {
        String sql = "select * from t_worksite where `status` = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Worksite.class));
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, Worksite worksite) {
        if (!StringUtils.isEmpty(worksite.getName())) {
            sb.append(" and ws.`name` like ?");
            params.add("%" + worksite.getName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
    }

}
