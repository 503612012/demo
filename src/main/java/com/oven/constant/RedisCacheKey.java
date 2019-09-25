package com.oven.constant;

/**
 * Reids缓存使用的key定义类
 *
 * @author Oven
 */
public class RedisCacheKey {

    // 用户实体类缓存key定义
    public static final String USER_PREFIX = "user_";
    public static final String USER_GET_ALL = "user_getAll";
    public static final String USER_GET_BY_ID = "user_getById_{0}";
    public static final String USER_GET_TOTAL_NUM = "user_getTotalNum_{0}";
    public static final String USER_GET_BY_PAGE = "user_getByPage_{0}_{1}_{2}";
    public static final String USER_GET_BY_USERNAME = "user_getByUserName_{0}";

    // 员工实体类缓存key定义
    public static final String EMPLOYEE_PREFIX = "employee_";
    public static final String EMPLOYEE_GET_ALL = "employee_getAll";
    public static final String EMPLOYEE_GET_BY_ID = "employee_getById_{0}";
    public static final String EMPLOYEE_GET_TOTAL_NUM = "employee_getTotalNum_{0}";
    public static final String EMPLOYEE_GET_BY_PAGE = "employee_getByPage_{0}_{1}_{2}";
    public static final String EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID = "employee_getHourSalaryByEmployeeId_{0}";

    // 工地实体类缓存key定义
    public static final String WORKSITE_PREFIX = "worksite_";
    public static final String WORKSITE_GET_ALL = "worksite_getAll";
    public static final String WORKSITE_GET_BY_ID = "worksite_getById_{0}";
    public static final String WORKSITE_GET_TOTAL_NUM = "worksite_getTotalNum_{0}";
    public static final String WORKSITE_GET_BY_PAGE = "worksite_getByPage_{0}_{1}_{2}";

    // 工时实体类缓存key定义
    public static final String WORKHOUR_PREFIX = "workhour_";
    public static final String WORKHOUR_GET_BY_ID = "workhour_getById_{0}";
    public static final String WORKHOUR_GET_TOTAL_NUM = "workhour_getTotalNum_{0}";
    public static final String WORKHOUR_GET_BY_PAGE = "workhour_getByPage_{0}_{1}_{2}";
    public static final String WORKHOUR_GET_TOTAL_WORKHOUR = "workhour_getTotalWorkhour";
    public static final String WORKHOUR_GET_WORKHOUR_PROPORTION = "workhour_getWorkhourProportion";
    public static final String WORKHOUR_GET_WORKHOUR_BY_EMPLOYEEID = "workhour_getWorkhourByEmployeeId_{0}";
    public static final String WORKHOUR_GET_BY_EMPLOYEEID_AND_WORKDATE_AND_WORKSITEID = "workhour_getByEmployeeIdAndWorkDateAndWorksiteId_{0}_{1}_{2}";

    // 角色实体类缓存key定义
    public static final String ROLE_PREFIX = "role_";
    public static final String ROLE_GET_ALL = "role_getAll";
    public static final String ROLE_GET_BY_ID = "role_getById_{0}";
    public static final String ROLE_GET_TOTAL_NUM = "role_getTotalNum_{0}";
    public static final String ROLE_GET_BY_PAGE = "role_getByPage_{0}_{1}_{2}";

    // 日志实体类缓存key定义
    public static final String LOG_PREFIX = "log_";
    public static final String LOG_GET_BY_ID = "log_getById_{0}";
    public static final String LOG_GET_TOTAL_NUM = "log_getTotalNum_{0}";
    public static final String LOG_GET_BY_PAGE = "log_getByPage_{0}_{1}_{2}";

    // 菜单实体类缓存key定义
    public static final String MENU_PREFIX = "menu_";
    public static final String MENU_GET_BY_ID = "menu_getById_{0}";
    public static final String MENU_GET_BY_PID = "menu_getByPid_{0}";
    public static final String MENU_GET_MENU_TREE_DATLE_DATA = "menu_getMenuTreeTableData";
    public static final String MENU_GET_MENU_TREE_BY_USERID = "menu_getMenuTreeByUserId_{0}";
    public static final String MENU_GET_BY_PID_AND_HASPERMISSION = "menu_getByPidAndHasPermission_{0}_{1}";

    // 发薪记录实体类缓存key定义
    public static final String PAYRECORD_PREFIX = "payRecord_";
    public static final String PAYRECORD_GET_TOTAL_PAY = "payRecord_getTotaPay";
    public static final String PAYRECORD_GET_TOTAL_NUM = "payRecord_getTotalNum_{0}";
    public static final String PAYRECORD_GET_BY_PAGE = "payRecord_getByPage_{0}_{1}_{2}";
    public static final String PAYRECORD_GET_SALARY_TOP_FIVE = "payRecord_getSalaryTopFive";
    public static final String PAYRECORD_GET_SALARY_PROPORTION = "payRecord_getSalaryProportion";

    // 用户角色关系实体类缓存key定义
    public static final String USERROLE_PREFIX = "userRole_";
    public static final String USERROLE_GET_BY_USERID = "userRole_getByUserId_{0}";
    public static final String USERROLE_GET_ROLE_BY_USERID = "userRole_getRoleByUserId_{0}";
    public static final String USERROLE_GET_BY_USERID_AND_ROLEID = "userRole_getByUserIdAndRoleId_{0}_{1}";

    // 角色菜单关系实体类缓存key定义
    public static final String ROLEMENU_PREFIX = "roleMenu_";
    public static final String ROLEMENU_GET_BY_ROLEID = "roleMenu_getByRoleId_{0}";
    public static final String ROLEMENU_GET_BY_ROLEID_AND_MENUID = "roleMenu_getByRoleIdAndMenuId_{0}_{1}";

    // 用户菜单编码
    public static final String USER_MENU_CODES = "userMenuCodes_{0}";
    public static final String USER_MENU_CODES_PREFIX = "userMenuCodes_";

}
