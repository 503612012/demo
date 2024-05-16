package com.oven.demo.core.employee.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.demo.core.employee.entity.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 员工dao层
 *
 * @author Oven
 */
@Repository
public class EmployeeDao extends BaseDao<Employee> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(Integer employeeId) {
        String sql = "select hour_salary from t_employee where dbid = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, employeeId);
    }

}
