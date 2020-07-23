package com.oven.constant;

/**
 * 系统常量定义类
 *
 * @author Oven
 */
public class AppConst {

    public static final String ERROR_LOG_PREFIX = "---------------------------"; // ERROR日志前缀

    public static final String INFO_LOG_PREFIX = "***************************"; // INFO日志前缀

    public static final String CURRENT_USER = "current_user"; // 当前登录用户

    public static final String LOGINEDUSERS = "loginedUsers"; // 已经登录到系统用户，是一个map

    public static final String MD5_SALT = "5217"; // MD5盐值

    public static final String USER_MENU = "menu"; // 用户菜单

    public static final String REQUEST_LOG_TEMPLATE_TABLENAME = "t_request_log_template"; // 请求日志模板表名

    public static final String SHIRO_COOKIE_KEY = "demo_shiro_cookie_key";
    public static final String SHIRO_CACHE_KEY_PROFIX = "demo_";

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String CAPTCHA = "_captcha";
    public static final String PERCENTAGE_MARK = "\\\\%";

    public static final String LIMIT_KEY_PREFIX = "limit_key_prefix_";

    public static final String SYSTEM_LIMIT = "操作太快，请稍后重试！";

    public static final String INSERT_LIMIT = "添加操作太快，请稍后重试！";

    public static final String UPDATE_LIMIT = "修改操作太快，请稍后重试！";

    public static final String DELETE_LIMIT = "删除操作太快，请稍后重试！";

    public static final int LIMIT_TIME = 5;

    public static final String EMPLOYEE_INSERT_LIMIT_KEY = "employee.insert.limit.key";
    public static final String EMPLOYEE_UPDATE_LIMIT_KEY = "employee.update.limit.key";
    public static final String EMPLOYEE_DELETE_LIMIT_KEY = "employee.delete.limit.key";
    public static final String EMPLOYEE_UPDATE_STATUS_LIMIT_KEY = "employee.update.status.limit.key";

    public static final String MENU_UPDATE_LIMIT_KEY = "menu.update.limit.key";
    public static final String MENU_UPDATE_STATUS_LIMIT_KEY = "menu.update.status.limit.key";

    public static final String PAY_DOPAY_LIMIT_KEY = "pay.doPay.limit.key";

    public static final String USER_INSERT_LIMIT_KEY = "user.insert.limit.key";
    public static final String USER_UPDATE_LIMIT_KEY = "user.update.limit.key";
    public static final String USER_DELETE_LIMIT_KEY = "user.delete.limit.key";
    public static final String USER_UPDATE_STATUS_LIMIT_KEY = "user.update.status.limit.key";
    public static final String USER_SET_USER_ROLE_LIMIT_KEY = "user.set.user.role.limit.key";

    public static final String ROLE_INSERT_LIMIT_KEY = "role.insert.limit.key";
    public static final String ROLE_UPDATE_LIMIT_KEY = "role.update.limit.key";
    public static final String ROLE_DELETE_LIMIT_KEY = "role.delete.limit.key";
    public static final String ROLE_UPDATE_STATUS_LIMIT_KEY = "role.update.status.limit.key";
    public static final String ROLE_SET_ROLE_MENU_LIMIT_KEY = "role.set.role.menu.limit.key";

    public static final String WORKHOUR_INSERT_LIMIT_KEY = "workhour.insert.limit.key";
    public static final String WORKHOUR_DELETE_LIMIT_KEY = "workhour.delete.limit.key";

    public static final String WORKSITE_INSERT_LIMIT_KEY = "worksite.insert.limit.key";
    public static final String WORKSITE_UPDATE_LIMIT_KEY = "worksite.update.limit.key";
    public static final String WORKSITE_DELETE_LIMIT_KEY = "worksite.delete.limit.key";
    public static final String WORKSITE_UPDATE_STATUS_LIMIT_KEY = "worksite.update.status.limit.key";

    public static final String ADVANCESALARY_INSERT_LIMIT_KEY = "com.oven.core.advanceSalary.insert.limit.key";
    public static final String ADVANCESALARY_DELETE_LIMIT_KEY = "com.oven.core.advanceSalary.delete.limit.key";

    public static final String FINANCE_INSERT_LIMIT_KEY = "finance.insert.limit.key";
    public static final String FINANCE_DELETE_LIMIT_KEY = "finance.delete.limit.key";

    public static final String FUND_INSERT_LIMIT_KEY = "fund.insert.limit.key";
    public static final String FUND_UPDATE_LIMIT_KEY = "fund.update.limit.key";
    public static final String FUND_DELETE_LIMIT_KEY = "fund.delete.limit.key";
    public static final String FUND_INPUT_PROFIT_LIMIT_KEY = "fund.input.profit.limit.key";
    public static final String FUND_UPDATE_STATUS_LIMIT_KEY = "fund.update.status.limit.key";
    public static final String FUND_INPUT_UPDATE_PROFIT_LIMIT_KEY = "fund.input.update.profit.limit.key";
    public static final String FUND_INPUT_DELETE_PROFIT_LIMIT_KEY = "fund.input.delete.profit.limit.key";

}
