package com.oven.demo.core.employee.service;

import cn.hutool.core.util.StrUtil;
import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.service.BaseService;
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
public class EmployeeService extends BaseService {

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
        // 移除缓存
        super.batchRemove(RedisCacheKey.EMPLOYEE_PREFIX);
    }

    /**
     * 更新
     */
    public void update(Employee employee) throws Exception {
        employee.setLastModifyTime(DateUtils.getCurrentTime());
        employee.setLastModifyId(CommonUtils.getCurrentUser().getId());
        employeeDao.update(employee);
        // 移除缓存
        super.batchRemove(RedisCacheKey.EMPLOYEE_PREFIX);
    }

    /**
     * 通过主键查询
     */
    public Employee getById(Integer id) {
        Employee employee = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_BY_ID, id)); // 先读取缓存
        if (employee == null) { // double check
            synchronized (this) {
                employee = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (employee == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    employee = employeeDao.getById(id);
                    super.set(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_BY_ID, id), employee);
                }
            }
        }
        return employee;
    }

    /**
     * 分页查询员工
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Employee> getByPage(Integer pageNum, Integer pageSize, Employee employee) {
        List<Employee> list = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_BY_PAGE, pageNum, pageSize, employee.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_BY_PAGE, pageNum, pageSize, employee.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = employeeDao.getByPage(pageNum, pageSize, employee);
                    super.set(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_BY_PAGE, pageNum, pageSize, employee.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        Integer totalNum = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_TOTAL_NUM, employee.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_TOTAL_NUM, employee.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = employeeDao.getTotalNum(employee);
                    super.set(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_TOTAL_NUM, employee.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 删除员工
     */
    public boolean delete(Integer id) throws Exception {
        boolean flag = employeeDao.delete(id) > 0;
        if (flag) {
            // 移除缓存
            super.batchRemove(RedisCacheKey.EMPLOYEE_PREFIX);
        }
        return flag;
    }

    /**
     * 获取所有员工
     */
    public List<Employee> getAll() {
        List<Employee> list = super.get(RedisCacheKey.EMPLOYEE_GET_ALL); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.EMPLOYEE_GET_ALL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = employeeDao.getAll();
                    super.set(RedisCacheKey.EMPLOYEE_GET_ALL, list);
                }
            }
        }
        return list;
    }

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(Integer employeeId) {
        Double hourSalary = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID, employeeId)); // 先读取缓存
        if (hourSalary == null) { // double check
            synchronized (this) {
                hourSalary = super.get(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID, employeeId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (hourSalary == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    hourSalary = employeeDao.getHourSalaryByEmployeeId(employeeId);
                    super.set(StrUtil.format(RedisCacheKey.EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID, employeeId), hourSalary);
                }
            }
        }
        return hourSalary;
    }

}
