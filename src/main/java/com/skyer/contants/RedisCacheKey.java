package com.skyer.contants;

/**
 * Reids缓存使用的key定义类
 *
 * @author SKYER
 */
public class RedisCacheKey {

    // 用户实体类缓存key定义
    public static final String USER_PREFIX = "user_";
    public static final String USER_GET_BY_ID = "user_getById_";
    public static final String USER_GET_BY_PAGE = "user_getByPage_";
    public static final String USER_GET_BY_USERNAME = "user_getByUserName_";
    public static final String USER_GET_TOTAL_NUM = "user_getTotalNum";

    // 日志实体类缓存key定义
    public static final String LOG_PREFIX = "log_";
    public static final String LOG_GET_BY_ID = "log_getById_";
    public static final String LOG_GET_BY_PAGE = "log_getByPage_";
    public static final String LOG_GET_TOTAL_NUM = "log_getTotalNum";

    // 菜单实体类缓存key定义
    public static final String MENU_PREFIX = "menu_";
    public static final String MENU_GET_BY_ID = "menu_getById_";
    public static final String MENU_GET_BY_PID = "menu_getByPid_";
    public static final String MENU_GET_MENU_TREE_BY_USERID = "menu_getMenuTreeByUserId_";

    // 用户角色关系实体类缓存key定义
    public static final String USERROLE_PREFIX = "userRole_";
    public static final String USERROLE_GET_BY_USERID = "userRole_getByUserId_";

    // 角色菜单关系实体类缓存key定义
    public static final String ROLEMENU_PREFIX = "roleMenu_";
    public static final String ROLEMENU_GET_BY_ROLEID = "roleMenu_getByRoleId_";

    // 用户菜单编码
    public static final String USER_MENU_CODES = "userMenuCodes_";

}
