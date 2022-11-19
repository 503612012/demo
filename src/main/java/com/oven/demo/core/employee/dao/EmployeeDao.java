package com.oven.demo.core.employee.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.core.employee.entity.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
     * 分页查询员工
     */
    public List<Employee> getByPage(Integer pageNum, Integer pageSize, Employee employee) {
        return super.getByPage(pageNum, pageSize, addCondition(employee));
    }

    /**
     * 统计员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        return super.getTotalNum(addCondition(employee));
    }

    /**
     * 搜索条件
     */
    private ConditionAndParams addCondition(Employee employee) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(employee.getName())) {
            sql.append(" and e.`name` like ?");
            params.add("%" + employee.getName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(employee.getContact())) {
            sql.append(" and e.contact like ?");
            params.add("%" + employee.getContact().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (employee.getGender() != null) {
            sql.append(" and e.gender = ?");
            params.add(employee.getGender());
        }
        return ConditionAndParams.build(sql.toString(), params.toArray());
    }

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(String employeeId) {
        String sql = "select hour_salary from t_employee where dbid = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, employeeId);
    }

}
