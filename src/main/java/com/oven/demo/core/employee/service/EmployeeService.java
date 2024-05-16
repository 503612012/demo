package com.oven.demo.core.employee.service;

import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.employee.dao.EmployeeDao;
import com.oven.demo.core.employee.entity.Employee;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 员工服务层
 *
 * @author Oven
 */
@Service
public class EmployeeService {

    @Resource
    private EmployeeDao employeeDao;

    /**
     * 添加员工
     */
    public void save(Employee employee) throws Exception {
        employee.setStatus(0);
        employee.setCreateId(CommonUtils.getCurrentUser().getId());
        employee.setCreateTime(DateUtils.getCurrentTime());
        employee.setLastModifyId(CommonUtils.getCurrentUser().getId());
        employee.setLastModifyTime(DateUtils.getCurrentTime());
        employeeDao.save(employee);
    }

    /**
     * 更新
     */
    public void update(Employee employee) throws Exception {
        employee.setLastModifyTime(DateUtils.getCurrentTime());
        employee.setLastModifyId(CommonUtils.getCurrentUser().getId());
        employeeDao.update(employee);
    }

    /**
     * 通过主键查询
     */
    public Employee getById(Integer id) {
        return employeeDao.getById(id);
    }

    /**
     * 分页查询员工
     */
    public List<Employee> getByPage(Employee employee) {
        return employeeDao.getByPage(employee);
    }

    /**
     * 获取员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        return employeeDao.getTotalNum(employee);
    }

    /**
     * 删除员工
     */
    public boolean delete(Integer id) throws Exception {
        return employeeDao.delete(id) > 0;
    }

    /**
     * 获取所有员工
     */
    public List<Employee> getAll() {
        return employeeDao.getAll();
    }

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(Integer employeeId) {
        return employeeDao.getHourSalaryByEmployeeId(employeeId);
    }

}
