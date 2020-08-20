package com.oven.core.employee.service;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.RedisCacheKey;
import com.oven.core.base.service.BaseService;
import com.oven.core.employee.dao.EmployeeDao;
import com.oven.core.employee.vo.Employee;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
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
    @Transactional(rollbackFor = Exception.class)
    public void add(Employee employee) {
        employee.setCreateId(super.getCurrentUser().getId());
        employee.setCreateTime(new DateTime().toString(AppConst.TIME_PATTERN));
        employee.setLastModifyId(super.getCurrentUser().getId());
        employee.setLastModifyTime(new DateTime().toString(AppConst.TIME_PATTERN));
        employeeDao.add(employee);
        // 移除缓存
        super.batchRemove(RedisCacheKey.EMPLOYEE_PREFIX);
        // 记录日志
        super.addLog("添加员工", employee.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Employee employee) {
        Employee employeeInDb = this.getById(employee.getId());
        String employeeName = employeeInDb.getName();
        StringBuilder content = new StringBuilder();
        if (!employeeInDb.getName().equals(employee.getName())) {
            content.append("姓名由[").append(employeeInDb.getName()).append("]改为[").append(employee.getName()).append("]，");
            employeeInDb.setName(employee.getName());
        }
        if (!employeeInDb.getAge().equals(employee.getAge())) {
            content.append("年龄由[").append(employeeInDb.getAge()).append("]改为[").append(employee.getAge()).append("]，");
            employeeInDb.setAge(employee.getAge());
        }
        if (!employeeInDb.getGender().equals(employee.getGender())) {
            content.append("性别由[").append(employeeInDb.getGender() == 1 ? "男" : "女").append("]改为[").append(employee.getGender() == 1 ? "男" : "女").append("]，");
            employeeInDb.setGender(employee.getGender());
        }
        if (!employeeInDb.getContact().equals(employee.getContact())) {
            content.append("联系方式由[").append(employeeInDb.getContact()).append("]改为[").append(employee.getContact()).append("]，");
            employeeInDb.setContact(employee.getContact());
        }
        if (!employeeInDb.getHourSalary().equals(employee.getHourSalary())) {
            content.append("时薪由[").append(employeeInDb.getHourSalary()).append("]改为[").append(employee.getHourSalary()).append("]，");
            employeeInDb.setHourSalary(employee.getHourSalary());
        }
        if (employee.getStatus() == null) {
            employee.setStatus(0);
        }
        if (!employeeInDb.getStatus().equals(employee.getStatus())) {
            content.append("状态由[").append(employeeInDb.getStatus() == 0 ? "正常" : "锁定").append("]改为[").append(employee.getStatus() == 0 ? "正常" : "锁定").append("]，");
            employeeInDb.setStatus(employee.getStatus());
        }
        if (!employeeInDb.getAddress().equals(employee.getAddress())) {
            content.append("住址由[").append(employeeInDb.getAddress()).append("]改为[").append(employee.getAddress()).append("]");
            employeeInDb.setAddress(employee.getAddress());
        }
        String str = content.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            employeeInDb.setLastModifyTime(new DateTime().toString(AppConst.TIME_PATTERN));
            employeeInDb.setLastModifyId(super.getCurrentUser().getId());
            employeeDao.update(employeeInDb);
            // 移除缓存
            super.batchRemove(RedisCacheKey.EMPLOYEE_PREFIX);
            // 记录日志
            super.addLog("修改员工", "[" + employeeName + "]" + str, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
    }

    /**
     * 通过主键查询
     */
    public Employee getById(Integer id) {
        Employee employee = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_BY_ID, id)); // 先读取缓存
        if (employee == null) { // double check
            synchronized (this) {
                employee = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (employee == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    employee = employeeDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_BY_ID, id), employee);
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
        List<Employee> list = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_BY_PAGE, pageNum, pageSize, employee.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_BY_PAGE, pageNum, pageSize, employee.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = employeeDao.getByPage(pageNum, pageSize, employee);
                    super.set(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_BY_PAGE, pageNum, pageSize, employee.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取员工总数量
     */
    public Integer getTotalNum(Employee employee) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_TOTAL_NUM, employee.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_TOTAL_NUM, employee.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = employeeDao.getTotalNum(employee);
                    super.set(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_TOTAL_NUM, employee.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 删除员工
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id) {
        Employee employee = this.getById(id);
        boolean flag = employeeDao.delete(id) > 0;
        if (flag) {
            // 移除缓存
            super.batchRemove(RedisCacheKey.EMPLOYEE_PREFIX);
            // 记录日志
            super.addLog("删除员工", employee.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
        return flag;
    }

    /**
     * 获取所有员工
     */
    public Object getAll() {
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
    public Double getHourSalaryByEmployeeId(String employeeId) {
        Double hourSalary = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID, employeeId)); // 先读取缓存
        if (hourSalary == null) { // double check
            synchronized (this) {
                hourSalary = super.get(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID, employeeId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (hourSalary == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    hourSalary = employeeDao.getHourSalaryByEmployeeId(employeeId);
                    super.set(MessageFormat.format(RedisCacheKey.EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID, employeeId), hourSalary);
                }
            }
        }
        return hourSalary;
    }

}
