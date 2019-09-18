package com.oven.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.vo.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
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
                "                             `day_salary`," +
                "                             `month_salary`," +
                "                             `create_time`," +
                "                             `create_id`," +
                "                             `last_modify_time`," +
                "                             `last_modify_id`," +
                "                             `status`)" +
                "                       values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getAge());
            ps.setInt(3, employee.getGender());
            ps.setString(4, employee.getAddress());
            ps.setString(5, employee.getContact());
            ps.setDouble(6, employee.getDaySalary());
            ps.setDouble(7, employee.getMonthSalary());
            ps.setString(8, employee.getCreateTime());
            ps.setInt(9, employee.getCreateId());
            ps.setString(10, employee.getLastModifyTime());
            ps.setInt(11, employee.getLastModifyId());
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
                "                           `day_salary` = ?," +
                "                           `month_salary` = ?," +
                "                           `create_time` = ?," +
                "                           `create_id` = ?," +
                "                           `last_modify_time` = ?," +
                "                           `last_modify_id` = ?," +
                "                           `status` = ?" +
                "                     where `dbid` = ?";
        return this.jdbcTemplate.update(sql, employee.getName(), employee.getAge(), employee.getGender(), employee.getAddress(),
                employee.getContact(), employee.getDaySalary(), employee.getMonthSalary(), employee.getCreateTime(),
                employee.getCreateId(), employee.getLastModifyTime(), employee.getLastModifyId(), employee.getStatus(), employee.getId());
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
        addCondition(sb, employee);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Employee.class), (pageNum - 1) * pageSize, pageSize);
    }

    /**
     * 统计员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        StringBuilder sb = new StringBuilder("select count(*) from t_employee e");
        addCondition(sb, employee);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
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
    private void addCondition(StringBuilder sb, Employee employee) {
        if (!StringUtils.isEmpty(employee.getName())) {
            sb.append(" and e.`name` like '%").append(employee.getName()).append("%'");
        }
        if (!StringUtils.isEmpty(employee.getContact())) {
            sb.append(" and e.contact like '%").append(employee.getContact()).append("%'");
        }
        if (employee.getGender() != null) {
            sb.append(" and e.contact = ").append(employee.getGender());
        }
    }

}
