package com.skyer.contants;

/**
 * Reids缓存使用的key定义类
 *
 * @author SKYER
 */
public class RedisCacheKey {

    public static final String GET_USER_BY_ID = "getUserById_";
    public static final String GET_USER_BY_PAGE = "getUserByPage_";
    public static final String GET_USER_BY_USERNAME = "getUserByUserName_";
    public static final String GET_USER_TOTAL_NUM = "getUserTotalNum";

    public static final String GET_LOG_BY_ID = "getLogById_";
    public static final String GET_LOG_BY_PAGE = "getLogByPage_";
    public static final String GET_LOG_TOTAL_NUM = "getLogTotalNum";

    public static final String GET_MENU_BY_ID = "getMenuById_";
    public static final String GET_MENU_BY_PID = "getMenuByPid_";
    public static final String GET_MENU_TREE_BY_USERID = "getMenuTreeByUserId_";

    public static final String GET_USERROLE_BY_USERID = "getUserRoleByUserId_";
    public static final String GET_ROLEMENU_BY_ROLEID = "getRoleMenuByUserId_";

}
