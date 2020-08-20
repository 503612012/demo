package com.oven.core.advanceSalary.dao;

import com.oven.common.constant.AppConst;
import com.oven.common.util.VoPropertyRowMapper;
import com.oven.core.advanceSalary.vo.AdvanceSalary;
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
 * 预支薪资dao层
 *
 * @author Oven
 */
@Repository
public class AdvanceSalaryDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(AdvanceSalary advanceSalary) {
        String sql = "insert into t_advance_salary (dbid, employee_id, has_repay, advance_time, money, create_id, remark) values (null, ?, default, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, advanceSalary.getEmployeeId());
            ps.setString(2, advanceSalary.getAdvanceTime());
            ps.setDouble(3, advanceSalary.getMoney());
            ps.setInt(4, advanceSalary.getCreateId());
            ps.setString(5, advanceSalary.getRemark());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 分页查询预支薪资
     */
    public List<AdvanceSalary> getByPage(Integer pageNum, Integer pageSize, AdvanceSalary advanceSalary) {
        StringBuilder sb = new StringBuilder("select a.*, e.name as employee_name, u.nick_name as create_name from t_advance_salary a " +
                "left join t_employee e on e.dbid = a.employee_id " +
                "left join t_user u on u.dbid = a.create_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, advanceSalary);
        String sql = sb.append(" order by a.advance_time desc limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(AdvanceSalary.class));
    }

    /**
     * 统计预支薪资总数量
     */
    public Integer getTotalNum(AdvanceSalary advanceSalary) {
        StringBuilder sb = new StringBuilder("select count(*) from t_advance_salary a " +
                "left join t_employee e on e.dbid = a.employee_id " +
                "left join t_user u on u.dbid = a.create_id");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, advanceSalary);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 通过主键查询
     */
    public AdvanceSalary getById(Integer id) {
        String sql = "select * from t_advance_salary where dbid = ?";
        List<AdvanceSalary> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(AdvanceSalary.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_advance_salary where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, AdvanceSalary advanceSalary) {
        if (!StringUtils.isEmpty(advanceSalary.getAdvanceTime())) {
            sb.append(" and a.`advance_time` = ?");
            params.add(advanceSalary.getAdvanceTime());
        }
        if (!StringUtils.isEmpty(advanceSalary.getEmployeeName())) {
            sb.append(" and e.name like ?");
            params.add("%" + advanceSalary.getEmployeeName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
    }

    /**
     * 获取同比增长数据
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月，3传入的是年月日
     */
    public Double getAdvanceSalaryByDateAndDateType(String date, Integer dateType) {
        String sql;
        if (dateType == 1) { // 传入的年
            sql = "select sum(money) as money from t_advance_salary where substr(advance_time, 1, 4) = ?";
        } else if (dateType == 2) { // 传入的是年月
            sql = "select sum(money) as money from t_advance_salary where substr(advance_time, 1, 7) = ?";
        } else if (dateType == 3) { // 传入的是年月日
            sql = "select sum(money) as money from t_advance_salary where substr(advance_time, 1, 10) = ?";
        } else {
            return null;
        }
        return this.jdbcTemplate.queryForObject(sql, Double.class, date);
    }

    /**
     * 根据员工ID获取
     */
    public List<AdvanceSalary> getByEmployeeId(Integer employeeId, Integer hasRepay) {
        StringBuilder sql = new StringBuilder("select * from t_advance_salary where employee_id = ?");
        if (hasRepay != null) {
            sql.append(" and has_repay = ").append(hasRepay);
        }
        return this.jdbcTemplate.query(sql.toString(), new VoPropertyRowMapper<>(AdvanceSalary.class), employeeId);
    }

    /**
     * 批量更新预支薪资为已归还
     */
    public void backAdvanceSalaryByEmployeeId(Integer employeeId) {
        String sql = "update t_advance_salary set has_repay = 0 where employee_id = ?";
        this.jdbcTemplate.update(sql, employeeId);
    }

    /**
     * 获取员工未归还薪资总额
     */
    public Double getTotalAdvanceSalaryByEmployeeId(Integer employeeId) {
        String sql = "select sum(money) as money from t_advance_salary where has_repay = 1 and employee_id = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, employeeId);
    }

}
