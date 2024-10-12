package com.oven.demo.core.employee.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.demo.core.employee.entity.Employee;
import org.springframework.stereotype.Repository;

/**
 * 员工dao层
 *
 * @author Oven
 */
@Repository
public class EmployeeDao extends BaseDao<Employee> {

    /**
     * 获取一个员工的时薪
     */
    public Double getHourSalaryByEmployeeId(Integer employeeId) {
        return super.getOneDouble(ConditionAndParams.eq("dbid", employeeId), "hour_salary");
    }

}
