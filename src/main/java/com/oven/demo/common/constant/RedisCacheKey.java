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
    String USER_GET_BY_ID = USER_PREFIX + "getById_{}";
    String USER_GET_TOTAL_NUM = USER_PREFIX + "getTotalNum_{}";
    String USER_GET_BY_PAGE = USER_PREFIX + "getByPage_{}_{}_{}";
    String USER_GET_BY_USERNAME = USER_PREFIX + "getByUserName_{}";

    // 员工实体类缓存key定义
    String EMPLOYEE_PREFIX = PREFIX + "employee_";
    String EMPLOYEE_GET_ALL = EMPLOYEE_PREFIX + "getAll";
    String EMPLOYEE_GET_BY_ID = EMPLOYEE_PREFIX + "getById_{}";
    String EMPLOYEE_GET_TOTAL_NUM = EMPLOYEE_PREFIX + "getTotalNum_{}";
    String EMPLOYEE_GET_BY_PAGE = EMPLOYEE_PREFIX + "getByPage_{}_{}_{}";
    String EMPLOYEE_GET_HOUR_SALARY_BY_EMPLOYEEID = EMPLOYEE_PREFIX + "getHourSalaryByEmployeeId_{}";

    // 角色实体类缓存key定义
    String ROLE_PREFIX = PREFIX + "role_";
    String ROLE_GET_ALL = ROLE_PREFIX + "getAll";
    String ROLE_GET_BY_ID = ROLE_PREFIX + "getById_{}";
    String ROLE_GET_TOTAL_NUM = ROLE_PREFIX + "getTotalNum_{}";
    String ROLE_GET_BY_PAGE = ROLE_PREFIX + "getByPage_{}_{}_{}";

    // 日志实体类缓存key定义
    String LOG_PREFIX = PREFIX + "log_";
    String LOG_GET_BY_ID = LOG_PREFIX + "getById_{}";
    String LOG_GET_TOTAL_NUM = LOG_PREFIX + "getTotalNum_{}";
    String LOG_GET_BY_PAGE = LOG_PREFIX + "getByPage_{}_{}_{}";

    // 菜单实体类缓存key定义
    String MENU_PREFIX = PREFIX + "menu_";
    String MENU_GET_BY_ID = MENU_PREFIX + "getById_{}";
    String MENU_GET_BY_PID = MENU_PREFIX + "getByPid_{}";
    String MENU_GET_MENU_TREE_DATLE_DATA = MENU_PREFIX + "getMenuTreeTableData";
    String MENU_GET_MENU_TREE_BY_USERID = MENU_PREFIX + "getMenuTreeByUserId_{}";
    String MENU_GET_BY_PID_AND_HASPERMISSION = MENU_PREFIX + "getByPidAndHasPermission_{}_{}";

    // 用户角色关系实体类缓存key定义
    String USERROLE_PREFIX = PREFIX + "userRole_";
    String USERROLE_GET_BY_USERID = USERROLE_PREFIX + "getByUserId_{}";
    String USERROLE_GET_ROLE_BY_USERID = USERROLE_PREFIX + "getRoleByUserId_{}";
    String USERROLE_GET_BY_USERID_AND_ROLEID = USERROLE_PREFIX + "getByUserIdAndRoleId_{}_{}";

    // 角色菜单关系实体类缓存key定义
    String ROLEMENU_PREFIX = PREFIX + "roleMenu_";
    String ROLEMENU_GET_BY_ROLEID = ROLEMENU_PREFIX + "getByRoleId_{}";
    String ROLEMENU_GET_BY_ROLEID_AND_MENUID = ROLEMENU_PREFIX + "getByRoleIdAndMenuId_{}_{}";

    // 用户菜单编码
    String USER_MENU_CODES_PREFIX = PREFIX + "userMenuCodes_";
    String USER_MENU_CODES = USER_MENU_CODES_PREFIX + "userMenuCodes_{}";

    // 数据字典缓存key定义
    String SYS_DIC_FIND_ALL = "SYS_DIC_FIND_ALL";
    String SYS_DIC_FIND_BY_ID = "SYS_DIC_FIND_BY_ID_{}";
    String SYS_DIC_FIND_BY_KEY = "SYS_DIC_FIND_BY_KEY_{}";

    // cron表达式
    String GET_CRON_BY_KEY = PREFIX + "getCronByKey_{}";

}
