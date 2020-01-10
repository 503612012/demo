package com.oven.dao;

import com.oven.vo.WechatFund;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 微信基金dao层
 *
 * @author Oven
 */
@Repository
public class WechatFundDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(WechatFund wechatFund) {
        String sql = "insert into t_wechat_fund (`dbid`," +
                "                             `data_date`," +
                "                             `money`)" +
                "                       values (null, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, wechatFund.getDataDate());
            ps.setDouble(2, wechatFund.getMoney());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 获取微信基金报表
     */
    public Map<String, Object> getChartsData(String date) {
        String sql = "select sum(cast(money as decimal(27, 2))) as money from t_wechat_fund where substr(data_date, 1, 10) = ?";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, date);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取微信累计基金报表
     */
    public Map<String, Object> getTotalChartsData(String date) {
        String sql = "select sum(cast(money as decimal(27, 2))) as money from t_wechat_fund where substr(data_date, 1, 10) <= ?";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, date);
        return list.size() > 0 ? list.get(0) : null;
    }

}
