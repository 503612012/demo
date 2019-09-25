package com.oven.constant;

/**
 * 系统常量定义类
 *
 * @author Oven
 */
public class AppConst {

    public static final String ERROR_LOG_PREFIX = "---------------------------"; // ERROR日志前缀

    public static final String INFO_LOG_PREFIX = "***************************"; // INFO日志前缀

    public static final Long CACHE_MINUTE = 600 * 1000L; // 缓存时效时间，单位：毫秒

    public static final String CURRENT_USER = "current_user"; // 当前登录用户

    public static final String LOGINEDUSERS = "loginedUsers"; // 已经登录到系统用户，是一个map

    public static final String MD5_SALT = "5217"; // MD5盐值

    public static final String USER_MENU = "menu"; // 用户菜单

    public static final String CAPTCHA = "_captcha";

    public static final String LIMIT_KEY_PREFIX = "limit_key_prefix_";

    public static final String SYSTEM_LIMIT = "操作太快，请稍后重试！";

    public static final String INSERT_LIMIT = "添加操作太快，请稍后重试！";

    public static final String UPDATE_LIMIT = "修改操作太快，请稍后重试！";

    public static final String DELETE_LIMIT = "删除操作太快，请稍后重试！";

}
