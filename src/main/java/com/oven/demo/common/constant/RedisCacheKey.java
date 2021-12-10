package com.oven.demo.common.constant;

/**
 * Reids缓存使用的key定义类
 *
 * @author Oven
 */
public interface RedisCacheKey {

    String PREFIX = AppConst.APP_NAME;

    // 用户实体类缓存key定义
    String USER_PREFIX = PREFIX + "user_";
    String USER_GET_ALL = USER_PREFIX + "getAll";
    String USER_GET_BY_ID = USER_PREFIX + "getById_{0}";
    String USER_GET_TOTAL_NUM = USER_PREFIX + "getTotalNum_{0}";
    String USER_GET_BY_PAGE = USER_PREFIX + "getByPage_{0}_{1}_{2}";
    String USER_GET_BY_USERNAME = USER_PREFIX + "getByUserName_{0}";

    // 员工实体类缓存key定义
    String EMPLOYEE_PREFIX = PREFIX + "employee_";
    String EMPLOYEE_GET_ALL = EMPLOYEE_PREFIX + "getAll";
    String EMPLOYEE_GET_BY_ID = EMPLOYEE_PREFIX + "getById_{0}";
    String EMPLOYEE_GET_TOTAL_NUM = EMPLOYEE_PREFIX + "getTotalNum_{0}";
    String EMPLOYEE_GET_BY_PAGE = EMPLOYEE_PREFIX + "getByPage_{0}_{1}_{2}";
    String EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID = EMPLOYEE_PREFIX + "getHourSalaryByEmployeeId_{0}";

    // 工地实体类缓存key定义
    String WORKSITE_PREFIX = PREFIX + "worksite_";
    String WORKSITE_GET_ALL = WORKSITE_PREFIX + "getAll";
    String WORKSITE_GET_BY_ID = WORKSITE_PREFIX + "getById_{0}";
    String WORKSITE_GET_TOTAL_NUM = WORKSITE_PREFIX + "getTotalNum_{0}";
    String WORKSITE_GET_BY_PAGE = WORKSITE_PREFIX + "getByPage_{0}_{1}_{2}";

    // 工时实体类缓存key定义
    String WORKHOUR_PREFIX = PREFIX + "workhour_";
    String WORKHOUR_GET_BY_ID = WORKHOUR_PREFIX + "getById_{0}";
    String WORKHOUR_GET_TOTAL_NUM = WORKHOUR_PREFIX + "getTotalNum_{0}";
    String WORKHOUR_GET_BY_PAGE = WORKHOUR_PREFIX + "getByPage_{0}_{1}_{2}";
    String WORKHOUR_GET_WORKSITEID = WORKHOUR_PREFIX + "getByWorksiteId_{0}";
    String WORKHOUR_GET_TOTAL_WORKHOUR = WORKHOUR_PREFIX + "getTotalWorkhour";
    String WORKHOUR_GET_WORKHOUR_PROPORTION = WORKHOUR_PREFIX + "getWorkhourProportion";
    String WORKHOUR_GET_UN_PAY_BY_EMPLOYEEID = WORKHOUR_PREFIX + "getUnPayByEmployeeId_{0}";
    String WORKHOUR_GET_SALARY_COMPARE_PROPORTION = WORKHOUR_PREFIX + "getSalaryCompareProportion_{0}_{1}_{2}";
    String WORKHOUR_GET_SALARY_BY_DATE_AND_DATETYPE = WORKHOUR_PREFIX + "getSalaryByDateAndDateType_{0}_{1}_{2}";
    String WORKHOUR_GET_WORKHOUR_BY_DATE_AND_DATETYPE = WORKHOUR_PREFIX + "getWorkhourByDateAndDateType_{0}_{1}_{2}";
    String WORKHOUR_GET_WORKHOUR_BY_EMPLOYEEID_AND_WORKSITEID = WORKHOUR_PREFIX + "getWorkhourByEmployeeIdAndWorksiteId_{0}_{1}";
    String WORKHOUR_GET_BY_EMPLOYEEID_AND_WORKDATE_AND_WORKSITEID = WORKHOUR_PREFIX + "getByEmployeeIdAndWorkDateAndWorksiteId_{0}_{1}_{2}";

    // 角色实体类缓存key定义
    String ROLE_PREFIX = PREFIX + "role_";
    String ROLE_GET_ALL = ROLE_PREFIX + "getAll";
    String ROLE_GET_BY_ID = ROLE_PREFIX + "getById_{0}";
    String ROLE_GET_TOTAL_NUM = ROLE_PREFIX + "getTotalNum_{0}";
    String ROLE_GET_BY_PAGE = ROLE_PREFIX + "getByPage_{0}_{1}_{2}";

    // 预支薪资实体类缓存key定义
    String ADVANCESALARY_PREFIX = PREFIX + "advanceSalary_";
    String ADVANCESALARY_GET_BY_ID = ADVANCESALARY_PREFIX + "getById_{0}";
    String ADVANCESALARY_GET_TOTAL_NUM = ADVANCESALARY_PREFIX + "getTotalNum_{0}";
    String ADVANCESALARY_GET_BY_PAGE = ADVANCESALARY_PREFIX + "getByPage_{0}_{1}_{2}";
    String ADVANCESALARY_GET_BY_EMPLOYEEID_AND_HASREPAY = ADVANCESALARY_PREFIX + "getByEmployeeId_{0}_{1}";
    String ADVANCESALARY_GET_TOTAL_ADVANCE_SALARY_BY_EMPLOYEEID = ADVANCESALARY_PREFIX + "getTotalAdvanceSalaryByEmployeeId_{0}";
    String ADVANCESALARY_GET_ADVANCE_SALARY_BY_DATE_DATETYPE = ADVANCESALARY_PREFIX + "getAdvanceSalaryByDateAndDateType_{0}_{1}";
    String ADVANCESALARY_GET_ADVANCE_SALARY_COMPARE_PROPORTION = ADVANCESALARY_PREFIX + "getAdvanceSalaryCompareProportion_{0}_{1}";

    // 财务登记实体类缓存key定义
    String FINANCE_PREFIX = PREFIX + "finance_";
    String FINANCE_GET_BY_ID = FINANCE_PREFIX + "getById_{0}";
    String FINANCE_GET_TOTAL_NUM = FINANCE_PREFIX + "getTotalNum_{0}";
    String FINANCE_GET_BY_PAGE = FINANCE_PREFIX + "getByPage_{0}_{1}_{2}";
    String FINANCE_GET_BY_WORKSITEID = FINANCE_PREFIX + "getByWorksiteId_{0}";

    // 日志实体类缓存key定义
    String LOG_PREFIX = PREFIX + "log_";
    String LOG_GET_BY_ID = LOG_PREFIX + "getById_{0}";
    String LOG_GET_TOTAL_NUM = LOG_PREFIX + "getTotalNum_{0}";
    String LOG_GET_BY_PAGE = LOG_PREFIX + "getByPage_{0}_{1}_{2}";

    // 菜单实体类缓存key定义
    String MENU_PREFIX = PREFIX + "menu_";
    String MENU_GET_BY_ID = MENU_PREFIX + "getById_{0}";
    String MENU_GET_BY_PID = MENU_PREFIX + "getByPid_{0}";
    String MENU_GET_MENU_TREE_DATLE_DATA = MENU_PREFIX + "getMenuTreeTableData";
    String MENU_GET_MENU_TREE_BY_USERID = MENU_PREFIX + "getMenuTreeByUserId_{0}";
    String MENU_GET_BY_PID_AND_HASPERMISSION = MENU_PREFIX + "getByPidAndHasPermission_{0}_{1}";

    // 发薪记录实体类缓存key定义
    String PAYRECORD_PREFIX = PREFIX + "payRecord_";
    String PAYRECORD_GET_TOTAL_PAY = PAYRECORD_PREFIX + "getTotaPay";
    String PAYRECORD_GET_TOTAL_NUM = PAYRECORD_PREFIX + "getTotalNum_{0}";
    String PAYRECORD_GET_BY_PAGE = PAYRECORD_PREFIX + "getByPage_{0}_{1}_{2}";
    String PAYRECORD_GET_SALARY_TOP_FIVE = PAYRECORD_PREFIX + "getSalaryTopFive";
    String PAYRECORD_GET_SALARY_PROPORTION = PAYRECORD_PREFIX + "getSalaryProportion";

    // 用户角色关系实体类缓存key定义
    String USERROLE_PREFIX = PREFIX + "userRole_";
    String USERROLE_GET_BY_USERID = USERROLE_PREFIX + "getByUserId_{0}";
    String USERROLE_GET_ROLE_BY_USERID = USERROLE_PREFIX + "getRoleByUserId_{0}";
    String USERROLE_GET_BY_USERID_AND_ROLEID = USERROLE_PREFIX + "getByUserIdAndRoleId_{0}_{1}";

    // 角色菜单关系实体类缓存key定义
    String ROLEMENU_PREFIX = PREFIX + "roleMenu_";
    String ROLEMENU_GET_BY_ROLEID = ROLEMENU_PREFIX + "getByRoleId_{0}";
    String ROLEMENU_GET_BY_ROLEID_AND_MENUID = ROLEMENU_PREFIX + "getByRoleIdAndMenuId_{0}_{1}";

    // 用户菜单编码
    String USER_MENU_CODES = PREFIX + "userMenuCodes_{0}";
    String USER_MENU_CODES_PREFIX = USER_MENU_CODES + "userMenuCodes_";

    // 基金实体类缓存key定义
    String FUND_PREFIX = PREFIX + "fund_";
    String FUND_GET_ALL = FUND_PREFIX + "getAll";
    String FUND_GET_BY_ID = FUND_PREFIX + "getById_{0}";
    String FUND_GET_TOTAL_NUM = FUND_PREFIX + "getTotalNum_{0}";
    String FUND_GET_BY_PAGE = FUND_PREFIX + "getByPage_{0}_{1}_{2}";

    // 基金账单实体类缓存key定义
    String FUNDBILL_PREFIX = PREFIX + "fundBill_";
    String FUNDBILL_GET_TOTAL = FUNDBILL_PREFIX + "getTotal";
    String FUNDBILL_GET_BY_ID = FUNDBILL_PREFIX + "getById_{0}";
    String FUNDBILL_GET_TOTAL_NUM = FUNDBILL_PREFIX + "getTotalNum_{0}_{1}";
    String FUNDBILL_GET_BY_PAGE = FUNDBILL_PREFIX + "getByPage_{0}_{1}_{2}_{3}";
    String FUNDBILL_GET_TOTAL_BY_DATE = FUNDBILL_PREFIX + "getTotalByDate_{0}_{1}";
    String FUNDBILL_GET_CHARTS_DATA = FUNDBILL_PREFIX + "getChartsData_{0}_{1}_{2}";
    String FUNDBILL_GET_CURRENT_DAY_TOTAL_BY_DATE = FUNDBILL_PREFIX + "getCurrentDayTotalByDate_{0}_{1}";

    // 微信基金实体类缓存key定义
    String WECHAT_FUND_PREFIX = PREFIX + "wechatFund_";
    String WECHAT_FUND_GET_CHARTS_DATA = WECHAT_FUND_PREFIX + "getChartsData_{0}";
    String WECHAT_FUND_GET_TOTAL_CHARTS_DATA = WECHAT_FUND_PREFIX + "getTotalChartsData_{0}";

}
