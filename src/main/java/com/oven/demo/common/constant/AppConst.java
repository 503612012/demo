package com.oven.demo.common.constant;

/**
 * 系统常量定义类
 *
 * @author Oven
 */
public interface AppConst {

    String APP_NAME = "demo_";

    String CURRENT_USER = "current_user"; // 当前登录用户

    String LOGINEDUSERS = "loginedUsers"; // 已经登录到系统用户，是一个map
    String SESSION_ID = "sessionId";
    String SESSION = "session";

    String MD5_SALT = "5217"; // MD5盐值

    String USER_MENU = "menu"; // 用户菜单

    String REQUEST_LOG_TEMPLATE_TABLENAME = "t_request_log_template"; // 请求日志模板表名

    String SHIRO_COOKIE_KEY = APP_NAME + "shiro_cookie_key";
    String SHIRO_CACHE_KEY_PROFIX = APP_NAME;

    String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    String CAPTCHA = "_captcha";
    String PERCENTAGE_MARK = "\\\\%";

}
