package com.oven.demo.common.constant;

/**
 * Reids缓存使用的key定义类
 *
 * @author Oven
 */
public interface RedisCacheKey {

    String PREFIX = AppConst.APP_NAME;

    // 员工实体类缓存key定义
    String EMPLOYEE_PREFIX = PREFIX + "employee_";
    String EMPLOYEE_GET_ALL = EMPLOYEE_PREFIX + "getAll";
    String EMPLOYEE_GET_BY_ID = EMPLOYEE_PREFIX + "getById_{0}";
    String EMPLOYEE_GET_TOTAL_NUM = EMPLOYEE_PREFIX + "getTotalNum_{0}";
    String EMPLOYEE_GET_BY_PAGE = EMPLOYEE_PREFIX + "getByPage_{0}_{1}_{2}";
    String EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID = EMPLOYEE_PREFIX + "getHourSalaryByEmployeeId_{0}";

}
