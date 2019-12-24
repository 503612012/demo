package com.oven.constant;

/**
 * Reids缓存使用的key定义类
 *
 * @author Oven
 */
public class RedisCacheKey {

    private static final String PREFIX = "demo_";

    // 用户实体类缓存key定义
    public static final String USER_PREFIX = PREFIX + "user_";
    public static final String USER_GET_ALL = PREFIX + "user_getAll";
    public static final String USER_GET_BY_ID = PREFIX + "user_getById_{0}";
    public static final String USER_GET_TOTAL_NUM = PREFIX + "user_getTotalNum_{0}";
    public static final String USER_GET_BY_PAGE = PREFIX + "user_getByPage_{0}_{1}_{2}";
    public static final String USER_GET_BY_USERNAME = PREFIX + "user_getByUserName_{0}";

    // 员工实体类缓存key定义
    public static final String EMPLOYEE_PREFIX = PREFIX + "employee_";
    public static final String EMPLOYEE_GET_ALL = PREFIX + "employee_getAll";
    public static final String EMPLOYEE_GET_BY_ID = PREFIX + "employee_getById_{0}";
    public static final String EMPLOYEE_GET_TOTAL_NUM = PREFIX + "employee_getTotalNum_{0}";
    public static final String EMPLOYEE_GET_BY_PAGE = PREFIX + "employee_getByPage_{0}_{1}_{2}";
    public static final String EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID = PREFIX + "employee_getHourSalaryByEmployeeId_{0}";

    // 工地实体类缓存key定义
    public static final String WORKSITE_PREFIX = PREFIX + "worksite_";
    public static final String WORKSITE_GET_ALL = PREFIX + "worksite_getAll";
    public static final String WORKSITE_GET_BY_ID = PREFIX + "worksite_getById_{0}";
    public static final String WORKSITE_GET_TOTAL_NUM = PREFIX + "worksite_getTotalNum_{0}";
    public static final String WORKSITE_GET_BY_PAGE = PREFIX + "worksite_getByPage_{0}_{1}_{2}";

    // 工时实体类缓存key定义
    public static final String WORKHOUR_PREFIX = PREFIX + "workhour_";
    public static final String WORKHOUR_GET_BY_ID = PREFIX + "workhour_getById_{0}";
    public static final String WORKHOUR_GET_TOTAL_NUM = PREFIX + "workhour_getTotalNum_{0}";
    public static final String WORKHOUR_GET_BY_PAGE = PREFIX + "workhour_getByPage_{0}_{1}_{2}";
    public static final String WORKHOUR_GET_WORKSITEID = PREFIX + "workhour_getByWorksiteId_{0}";
    public static final String WORKHOUR_GET_TOTAL_WORKHOUR = PREFIX + "workhour_getTotalWorkhour";
    public static final String WORKHOUR_GET_WORKHOUR_PROPORTION = PREFIX + "workhour_getWorkhourProportion";
    public static final String WORKHOUR_GET_UN_PAY_BY_EMPLOYEEID = PREFIX + "workhour_getUnPayByEmployeeId_{0}";
    public static final String WORKHOUR_GET_SALARY_COMPARE_PROPORTION = PREFIX + "workhour_getSalaryCompareProportion_{0}_{1}_{2}";
    public static final String WORKHOUR_GET_SALARY_BY_DATE_AND_DATETYPE = PREFIX + "workhour_getSalaryByDateAndDateType_{0}_{1}_{2}";
    public static final String WORKHOUR_GET_WORKHOUR_BY_DATE_AND_DATETYPE = PREFIX + "workhour_getWorkhourByDateAndDateType_{0}_{1}_{2}";
    public static final String WORKHOUR_GET_WORKHOUR_BY_EMPLOYEEID_AND_WORKSITEID = PREFIX + "workhour_getWorkhourByEmployeeIdAndWorksiteId_{0}_{1}";
    public static final String WORKHOUR_GET_BY_EMPLOYEEID_AND_WORKDATE_AND_WORKSITEID = PREFIX + "workhour_getByEmployeeIdAndWorkDateAndWorksiteId_{0}_{1}_{2}";

    // 角色实体类缓存key定义
    public static final String ROLE_PREFIX = PREFIX + "role_";
    public static final String ROLE_GET_ALL = PREFIX + "role_getAll";
    public static final String ROLE_GET_BY_ID = PREFIX + "role_getById_{0}";
    public static final String ROLE_GET_TOTAL_NUM = PREFIX + "role_getTotalNum_{0}";
    public static final String ROLE_GET_BY_PAGE = PREFIX + "role_getByPage_{0}_{1}_{2}";

    // 预支薪资实体类缓存key定义
    public static final String ADVANCESALARY_PREFIX = PREFIX + "advanceSalary_";
    public static final String ADVANCESALARY_GET_BY_ID = PREFIX + "advanceSalary_getById_{0}";
    public static final String ADVANCESALARY_GET_TOTAL_NUM = PREFIX + "advanceSalary_getTotalNum_{0}";
    public static final String ADVANCESALARY_GET_BY_PAGE = PREFIX + "advanceSalary_getByPage_{0}_{1}_{2}";
    public static final String ADVANCESALARY_GET_BY_EMPLOYEEID_AND_HASREPAY = PREFIX + "advanceSalary_getByEmployeeId_{0}_{1}";
    public static final String ADVANCESALARY_GET_TOTAL_ADVANCE_SALARY_BY_EMPLOYEEID = PREFIX + "advanceSalary_getTotalAdvanceSalaryByEmployeeId_{0}";
    public static final String ADVANCESALARY_GET_ADVANCE_SALARY_BY_DATE_DATETYPE = PREFIX + "advanceSalary_getAdvanceSalaryByDateAndDateType_{0}_{1}";
    public static final String ADVANCESALARY_GET_ADVANCE_SALARY_COMPARE_PROPORTION = PREFIX + "advanceSalary_getAdvanceSalaryCompareProportion_{0}_{1}";

    // 财务登记实体类缓存key定义
    public static final String FINANCE_PREFIX = PREFIX + "finance_";
    public static final String FINANCE_GET_BY_ID = PREFIX + "finance_getById_{0}";
    public static final String FINANCE_GET_TOTAL_NUM = PREFIX + "finance_getTotalNum_{0}";
    public static final String FINANCE_GET_BY_PAGE = PREFIX + "finance_getByPage_{0}_{1}_{2}";
    public static final String FINANCE_GET_BY_WORKSITEID = PREFIX + "finance_getByWorksiteId_{0}";

    // 日志实体类缓存key定义
    public static final String LOG_PREFIX = PREFIX + "log_";
    public static final String LOG_GET_BY_ID = PREFIX + "log_getById_{0}";
    public static final String LOG_GET_TOTAL_NUM = PREFIX + "log_getTotalNum_{0}";
    public static final String LOG_GET_BY_PAGE = PREFIX + "log_getByPage_{0}_{1}_{2}";

    // 菜单实体类缓存key定义
    public static final String MENU_PREFIX = PREFIX + "menu_";
    public static final String MENU_GET_BY_ID = PREFIX + "menu_getById_{0}";
    public static final String MENU_GET_BY_PID = PREFIX + "menu_getByPid_{0}";
    public static final String MENU_GET_MENU_TREE_DATLE_DATA = PREFIX + "menu_getMenuTreeTableData";
    public static final String MENU_GET_MENU_TREE_BY_USERID = PREFIX + "menu_getMenuTreeByUserId_{0}";
    public static final String MENU_GET_BY_PID_AND_HASPERMISSION = PREFIX + "menu_getByPidAndHasPermission_{0}_{1}";

    // 发薪记录实体类缓存key定义
    public static final String PAYRECORD_PREFIX = PREFIX + "payRecord_";
    public static final String PAYRECORD_GET_TOTAL_PAY = PREFIX + "payRecord_getTotaPay";
    public static final String PAYRECORD_GET_TOTAL_NUM = PREFIX + "payRecord_getTotalNum_{0}";
    public static final String PAYRECORD_GET_BY_PAGE = PREFIX + "payRecord_getByPage_{0}_{1}_{2}";
    public static final String PAYRECORD_GET_SALARY_TOP_FIVE = PREFIX + "payRecord_getSalaryTopFive";
    public static final String PAYRECORD_GET_SALARY_PROPORTION = PREFIX + "payRecord_getSalaryProportion";

    // 用户角色关系实体类缓存key定义
    public static final String USERROLE_PREFIX = PREFIX + "userRole_";
    public static final String USERROLE_GET_BY_USERID = PREFIX + "userRole_getByUserId_{0}";
    public static final String USERROLE_GET_ROLE_BY_USERID = PREFIX + "userRole_getRoleByUserId_{0}";
    public static final String USERROLE_GET_BY_USERID_AND_ROLEID = PREFIX + "userRole_getByUserIdAndRoleId_{0}_{1}";

    // 角色菜单关系实体类缓存key定义
    public static final String ROLEMENU_PREFIX = PREFIX + "roleMenu_";
    public static final String ROLEMENU_GET_BY_ROLEID = PREFIX + "roleMenu_getByRoleId_{0}";
    public static final String ROLEMENU_GET_BY_ROLEID_AND_MENUID = PREFIX + "roleMenu_getByRoleIdAndMenuId_{0}_{1}";

    // 用户菜单编码
    public static final String USER_MENU_CODES = PREFIX + "userMenuCodes_{0}";
    public static final String USER_MENU_CODES_PREFIX = PREFIX + "userMenuCodes_";

    // 基金实体类缓存key定义
    public static final String FUND_PREFIX = PREFIX + "fund_";
    public static final String FUND_GET_ALL = PREFIX + "fund_getAll";
    public static final String FUND_GET_BY_ID = PREFIX + "fund_getById_{0}";
    public static final String FUND_GET_TOTAL_NUM = PREFIX + "fund_getTotalNum_{0}";
    public static final String FUND_GET_BY_PAGE = PREFIX + "fund_getByPage_{0}_{1}_{2}";

    // 基金账单实体类缓存key定义
    public static final String FUNDBILL_PREFIX = PREFIX + "fundBill_";
    public static final String FUNDBILL_GET_TOTAL = PREFIX + "fundBill_getTotal";
    public static final String FUNDBILL_GET_BY_ID = PREFIX + "fundBill_getById_{0}";
    public static final String FUNDBILL_GET_TOTAL_NUM = PREFIX + "fundBill_getTotalNum_{0}_{1}";
    public static final String FUNDBILL_GET_BY_PAGE = PREFIX + "fundBill_getByPage_{0}_{1}_{2}_{3}";
    public static final String FUNDBILL_GET_TOTAL_BY_DATE = PREFIX + "fundBill_getTotalByDate_{0}_{1}";
    public static final String FUNDBILL_GET_CHARTS_DATA = PREFIX + "fundBill_getChartsData_{0}_{1}_{2}";
    public static final String FUNDBILL_GET_CURRENT_DAY_TOTAL_BY_DATE = PREFIX + "fundBill_getCurrentDayTotalByDate_{0}_{1}";

}
