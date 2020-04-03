package com.oven.core.payRecord.dao;

import com.oven.constant.AppConst;
import com.oven.util.VoPropertyRowMapper;
import com.oven.core.payRecord.vo.PayRecord;
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
 * 薪资发放记录dao层
 *
 * @author Oven
 */
@Repository
public class PayRecordDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(PayRecord payRecord) {
        String sql = "insert into t_pay_record (dbid, payer_id, employee_id, pay_date, total_money, total_hour, workhour_ids, remark, has_modify_money, change_money) values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, payRecord.getPayerId());
            ps.setInt(2, payRecord.getEmployeeId());
            ps.setString(3, payRecord.getPayDate());
            ps.setDouble(4, payRecord.getTotalMoney());
            ps.setInt(5, payRecord.getTotalHour());
            ps.setString(6, payRecord.getWorkhourIds());
            ps.setString(7, payRecord.getRemark());
            ps.setInt(8, payRecord.getHasModifyMoney());
            ps.setDouble(9, payRecord.getChangeMoney());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 分页查询发薪记录
     */
    public List<PayRecord> getByPage(Integer pageNum, Integer pageSize, String employeeName) {
        StringBuilder sb = new StringBuilder("select pr.*, e.name as employee_name, u.nick_name as payer_name from t_pay_record pr");
        sb.append(" left join t_employee e on e.dbid = pr.employee_id");
        sb.append(" left join t_user u on u.dbid = pr.payer_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, employeeName);
        String sql = sb.append(" order by pr.pay_date desc").append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(PayRecord.class));
    }

    /**
     * 统计发薪记录总数量
     */
    public Integer getTotalNum(String employeeName) {
        StringBuilder sb = new StringBuilder("select count(*) from t_pay_record pr");
        sb.append(" left join t_employee e on e.dbid = pr.employee_id");
        sb.append(" left join t_user u on u.dbid = pr.payer_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, employeeName);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, String employeeName) {
        if (!StringUtils.isEmpty(employeeName)) {
            sb.append(" and e.`name` like ?");
            params.add("%" + employeeName.replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
    }

    /**
     * 获取总发薪金额
     */
    public Double getTotalPay() {
        String sql = "select sum(total_money) from t_pay_record";
        return this.jdbcTemplate.queryForObject(sql, Double.class);
    }

    /**
     * 获取薪资排行前五
     */
    public List<Map<String, Object>> getSalaryTopFive() {
        String sql = "select e.name as employeeName, a.totalMoney from (select employee_id, sum(total_money) as totalMoney from t_pay_record group by employee_id) a left join t_employee e on e.dbid = a.employee_id order by totalMoney desc";
        return this.jdbcTemplate.queryForList(sql);
    }

    /**
     * 获取已发薪资占比
     */
    public List<String> getSalaryProportion() {
        String sql = "select sum(total_money) from t_pay_record " +
                "union " +
                "select sum(work_hour * hour_salary) from t_workhour";
        return this.jdbcTemplate.queryForList(sql, String.class);
    }

}
