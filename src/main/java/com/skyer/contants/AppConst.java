package com.skyer.contants;

/**
 * 系统常量定义类
 *
 * @author SKYER
 */
public class AppConst {

    public static final String ERROR_LOG_PREFIX = "---------------------------"; // ERROR日志前缀
    public static final String DEBUG_LOG_PREFIX = "==========================="; // DEBUG日志前缀
    public static final String INFO_LOG_PREFIX = "***************************"; // INFO日志前缀
    public static final Integer PAGE_SIZE = 10; // 表格每页显示数量
    public static final Long CACHE_MINUTE = 600 * 1000L; // 缓存时效时间，单位：毫秒

    public static final String CURRENT_USER = "current_user"; // 当前登录用户
    public static final String LOGINEDUSERS = "loginedUsers"; // 已经登录到系统用户，是一个map
    public static final String MD5_SALT = "5217"; // MD5盐值

}
