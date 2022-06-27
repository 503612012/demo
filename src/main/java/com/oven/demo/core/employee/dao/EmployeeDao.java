package com.oven.demo.core.employee.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.employee.vo.Employee;
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
     * 添加
     */
    public int add(Employee employee) throws Exception {
        return super.add(jdbcTemplate, employee);
    }

    /**
     * 更新
     */
    public int update(Employee employee) throws Exception {
        return super.update(jdbcTemplate, employee);
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
        StringBuilder sql = new StringBuilder("select * from t_employee e");
        List<Object> params = addCondition(sql, employee);
        return super.getByPage(sql, params, Employee.class, pageNum, pageSize, jdbcTemplate);
    }

    /**
     * 统计员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        StringBuilder sql = new StringBuilder("select count(*) from t_employee e");
        List<Object> params = addCondition(sql, employee);
        return super.getTotalNum(sql, params, jdbcTemplate);
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
    private List<Object> addCondition(StringBuilder sql, Employee employee) {
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
        return params;
    }

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(String employeeId) {
        String sql = "select hour_salary from t_employee where dbid = ?";
        return this.jdbcTemplate.queryForObject(sql, Double.class, employeeId);
    }

}
