package com.oven.demo.core.employee.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.employee.vo.Employee;
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
 * 员工dao层
 *
 * @author Oven
 */
@Repository
public class EmployeeDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(Employee employee) {
        String sql = "insert into t_employee (`dbid`," +
                "                             `name`," +
                "                             `age`," +
                "                             `gender`," +
                "                             `address`," +
                "                             `contact`," +
                "                             `hour_salary`," +
                "                             `create_time`," +
                "                             `create_id`," +
                "                             `last_modify_time`," +
                "                             `last_modify_id`," +
                "                             `status`)" +
                "                      values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getAge());
            ps.setInt(3, employee.getGender());
            ps.setString(4, employee.getAddress());
            ps.setString(5, employee.getContact());
            ps.setDouble(6, employee.getHourSalary());
            ps.setString(7, employee.getCreateTime());
            ps.setInt(8, employee.getCreateId());
            ps.setString(9, employee.getLastModifyTime());
            ps.setInt(10, employee.getLastModifyId());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 更新
     */
    public int update(Employee employee) {
        String sql = "update t_employee set `name` = ?," +
                "                           `age` = ?," +
                "                           `gender` = ?," +
                "                           `address` = ?," +
                "                           `contact` = ?," +
                "                           `hour_salary` = ?," +
                "                           `create_time` = ?," +
                "                           `create_id` = ?," +
                "                           `last_modify_time` = ?," +
                "                           `last_modify_id` = ?," +
                "                           `status` = ?" +
                "                     where `dbid` = ?";
        return this.jdbcTemplate.update(sql, employee.getName(), employee.getAge(), employee.getGender(), employee.getAddress(),
                employee.getContact(), employee.getHourSalary(), employee.getCreateTime(), employee.getCreateId(),
                employee.getLastModifyTime(), employee.getLastModifyId(), employee.getStatus(), employee.getId());
    }

    /**
     * 通过主键查询
     */
    public Employee getById(Integer id) {
        String sql = "select * from t_employee where dbid = ?";
        List<Employee> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Employee.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页查询员工
     */
    public List<Employee> getByPage(Integer pageNum, Integer pageSize, Employee employee) {
        StringBuilder sb = new StringBuilder("select * from t_employee e");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, employee);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(Employee.class));
    }

    /**
     * 统计员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        StringBuilder sb = new StringBuilder("select count(*) from t_employee e");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, employee);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 获取所有员工
     */
    public List<Employee> getAll() {
        String sql = "select * from t_employee where `status` = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Employee.class));
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_employee where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, Employee employee) {
        if (!StringUtils.isEmpty(employee.getName())) {
            sb.append(" and e.`name` like ?");
            params.add("%" + employee.getName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(employee.getContact())) {
            sb.append(" and e.contact like ?");
            params.add("%" + employee.getContact().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (employee.getGender() != null) {
            sb.append(" and e.gender = ?");
            params.add(employee.getGender());
        }
    }

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(String employeeId) {
        String sql = "select hour_salary from t_employee where dbid = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, employeeId);
    }

}
