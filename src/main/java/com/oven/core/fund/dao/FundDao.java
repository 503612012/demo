package com.oven.core.fund.dao;

import com.oven.common.constant.AppConst;
import com.oven.common.util.VoPropertyRowMapper;
import com.oven.core.fund.vo.Fund;
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
 * 基金dao层
 *
 * @author Oven
 */
@Repository
public class FundDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(Fund fund) {
        String sql = "insert into t_fund (`dbid`," +
                "                         `fund_name`," +
                "                         `create_id`," +
                "                         `create_time`," +
                "                         `status`)" +
                "                   values (null, ?, ?, ?, 0)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, fund.getFundName());
            ps.setInt(2, fund.getCreateId());
            ps.setString(3, fund.getCreateTime());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 更新
     */
    public int update(Fund fund) {
        String sql = "update t_fund set `fund_name` = ?," +
                "                       `status` = ?" +
                "                 where `dbid` = ?";
        return this.jdbcTemplate.update(sql, fund.getFundName(), fund.getStatus(), fund.getId());
    }

    /**
     * 通过主键查询
     */
    public Fund getById(Integer id) {
        String sql = "select * from t_fund where dbid = ?";
        List<Fund> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Fund.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页查询基金
     */
    public List<Fund> getByPage(Integer pageNum, Integer pageSize, String fundName) {
        StringBuilder sb = new StringBuilder("select f.*, u.nick_name as create_name from t_fund f left join t_user u on u.dbid = f.create_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, fundName);
        sb.append(" order by status, _order");
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(Fund.class));
    }

    /**
     * 统计基金总数量
     */
    public Integer getTotalNum(String fundName) {
        StringBuilder sb = new StringBuilder("select count(*) from t_fund f");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, fundName);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 获取所有基金
     */
    public List<Fund> getAll() {
        String sql = "select * from t_fund where `status` = 0 order by _order";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Fund.class));
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_fund where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, String fundName) {
        if (!StringUtils.isEmpty(fundName)) {
            sb.append(" and f.`fund_name` like ?");
            params.add("%" + fundName.replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
    }

    /**
     * 修改基金排序
     */
    public void updateOrder(Integer fundId, Integer order) {
        String sql = "update t_fund set _order = ? where dbid = ?";
        this.jdbcTemplate.update(sql, order, fundId);
    }

}
