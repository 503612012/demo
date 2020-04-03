package com.oven.core.fundBill.dao;

import com.oven.constant.AppConst;
import com.oven.util.VoPropertyRowMapper;
import com.oven.core.fundBill.vo.FundBill;
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
import java.util.Map;
import java.util.Objects;

/**
 * 基金账单dao层
 *
 * @author Oven
 */
@Repository
public class FundBillDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(FundBill fundBill) {
        String sql = "insert into t_fund_bill (`dbid`," +
                "                             `fund_id`," +
                "                             `data_date`," +
                "                             `money`)" +
                "                       values (null, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, fundBill.getFundId());
            ps.setString(2, fundBill.getDataDate());
            ps.setDouble(3, fundBill.getMoney());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 更新
     */
    public int update(FundBill fundBill) {
        String sql = "update t_fund_bill set `fund_id` = ?," +
                "                           `data_date` = ?," +
                "                           `money` = ?" +
                "                     where `dbid` = ?";
        return this.jdbcTemplate.update(sql, fundBill.getFundId(), fundBill.getDataDate(), fundBill.getMoney(), fundBill.getId());
    }

    /**
     * 通过主键查询
     */
    public FundBill getById(Integer id) {
        String sql = "select fb.*, f.fund_name from t_fund_bill fb left join t_fund f on f.dbid = fb.fund_id where fb.dbid = ?";
        List<FundBill> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(FundBill.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页查询基金收益
     */
    public List<FundBill> getByPage(Integer pageNum, Integer pageSize, String fundName, String date) {
        StringBuilder sb = new StringBuilder("select fb.*, f.fund_name from t_fund_bill fb left join t_fund f on f.dbid = fb.fund_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, fundName, date);
        sb.append(" order by fb.data_date desc, f._order");
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(FundBill.class));
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_fund_bill where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 统计基金收益总数量
     */
    public Integer getTotalNum(String fundName, String date) {
        StringBuilder sb = new StringBuilder("select count(*) from t_fund_bill fb left join t_fund f on f.dbid = fb.fund_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, fundName, date);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, String fundName, String date) {
        if (!StringUtils.isEmpty(fundName)) {
            sb.append(" and f.`fund_name` like ?");
            params.add("%" + fundName.replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(date)) {
            sb.append(" and fb.`data_date` = ?");
            params.add(date);
        }
    }

    /**
     * 获取基金收益
     *
     * @param dateType 日期类型：1按月、2按天
     */
    public Map<String, Object> getChartsData(String date, Integer dateType, Integer fundId) {
        String sql;
        if (dateType == 1) { // 传入的是月
            sql = "select sum(money) as money from t_fund_bill where substr(data_date, 1, 7) = ? and fund_id = ? and `status` = 0";
        } else if (dateType == 2) { // 传入的是日
            sql = "select sum(money) as money from t_fund_bill where substr(data_date, 1, 10) = ? and fund_id = ? and `status` = 0";
        } else {
            return null;
        }
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql, date, fundId);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取累计收益
     *
     * @param dateType 日期类型：1按月、2按天
     */
    public Double getCurrentDayTotalByDate(String date, Integer dateType) {
        String sql;
        if (dateType == 1) { // 传入的是月
            sql = "select sum(money) as money from t_fund_bill where substr(data_date, 1, 7) = ? and `status` = 0";
        } else if (dateType == 2) { // 传入的是日
            sql = "select sum(money) as money from t_fund_bill where substr(data_date, 1, 10) = ? and `status` = 0";
        } else {
            return null;
        }
        List<Double> list = this.jdbcTemplate.queryForList(sql, Double.class, date);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取持有收益
     *
     * @param dateType 日期类型：1按月、2按天
     */
    public Double getTotalByDate(String date, Integer dateType) {
        String sql;
        if (dateType == 1) { // 传入的是月
            sql = "select sum(money) as money from t_fund_bill where substr(data_date, 1, 7) <= ? and `status` = 0";
        } else if (dateType == 2) { // 传入的是日
            sql = "select sum(money) as money from t_fund_bill where substr(data_date, 1, 10) <= ? and `status` = 0";
        } else {
            return null;
        }
        List<Double> list = this.jdbcTemplate.queryForList(sql, Double.class, date);
        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * 获取全部累计
     */
    public Double getTotal() {
        String sql = "select sum(money) as money from t_fund_bill where status = 0";
        return this.jdbcTemplate.queryForObject(sql, Double.class);
    }

}
