package com.oven.framework.limitation;

public interface LimitKey {

    int LIMIT_TIME = 2;

    String LIMIT_KEY_PREFIX = "demo_limit_key_prefix_";

    String SYSTEM_LIMIT = "操作太快，请稍后重试！";

    String INSERT_LIMIT = "添加操作太快，请稍后重试！";

    String UPDATE_LIMIT = "修改操作太快，请稍后重试！";

    String DELETE_LIMIT = "删除操作太快，请稍后重试！";

    String EMPLOYEE_INSERT_LIMIT_KEY = "employee.insert.limit.key";
    String EMPLOYEE_UPDATE_LIMIT_KEY = "employee.update.limit.key";
    String EMPLOYEE_DELETE_LIMIT_KEY = "employee.delete.limit.key";
    String EMPLOYEE_UPDATE_STATUS_LIMIT_KEY = "employee.update.status.limit.key";

    String MENU_UPDATE_LIMIT_KEY = "menu.update.limit.key";
    String MENU_UPDATE_STATUS_LIMIT_KEY = "menu.update.status.limit.key";

    String PAY_DOPAY_LIMIT_KEY = "pay.doPay.limit.key";

    String USER_INSERT_LIMIT_KEY = "user.insert.limit.key";
    String USER_UPDATE_LIMIT_KEY = "user.update.limit.key";
    String USER_DELETE_LIMIT_KEY = "user.delete.limit.key";
    String USER_UPDATE_STATUS_LIMIT_KEY = "user.update.status.limit.key";
    String USER_SET_USER_ROLE_LIMIT_KEY = "user.set.user.role.limit.key";
    String USER_UPLOAD_AVATAR_LIMIT_KEY = "user.upload.avatar.limit.key";

    String ROLE_INSERT_LIMIT_KEY = "role.insert.limit.key";
    String ROLE_UPDATE_LIMIT_KEY = "role.update.limit.key";
    String ROLE_DELETE_LIMIT_KEY = "role.delete.limit.key";
    String ROLE_UPDATE_STATUS_LIMIT_KEY = "role.update.status.limit.key";
    String ROLE_SET_ROLE_MENU_LIMIT_KEY = "role.set.role.menu.limit.key";

    String WORKHOUR_INSERT_LIMIT_KEY = "workhour.insert.limit.key";
    String WORKHOUR_DELETE_LIMIT_KEY = "workhour.delete.limit.key";

    String WORKSITE_INSERT_LIMIT_KEY = "worksite.insert.limit.key";
    String WORKSITE_UPDATE_LIMIT_KEY = "worksite.update.limit.key";
    String WORKSITE_DELETE_LIMIT_KEY = "worksite.delete.limit.key";
    String WORKSITE_UPDATE_STATUS_LIMIT_KEY = "worksite.update.status.limit.key";

    String ADVANCESALARY_INSERT_LIMIT_KEY = "com.oven.core.advanceSalary.insert.limit.key";
    String ADVANCESALARY_DELETE_LIMIT_KEY = "com.oven.core.advanceSalary.delete.limit.key";

    String FINANCE_INSERT_LIMIT_KEY = "finance.insert.limit.key";
    String FINANCE_DELETE_LIMIT_KEY = "finance.delete.limit.key";

    String FUND_INSERT_LIMIT_KEY = "fund.insert.limit.key";
    String FUND_UPDATE_LIMIT_KEY = "fund.update.limit.key";
    String FUND_DELETE_LIMIT_KEY = "fund.delete.limit.key";
    String FUND_INPUT_PROFIT_LIMIT_KEY = "fund.input.profit.limit.key";
    String FUND_UPDATE_STATUS_LIMIT_KEY = "fund.update.status.limit.key";
    String FUND_INPUT_UPDATE_PROFIT_LIMIT_KEY = "fund.input.update.profit.limit.key";
    String FUND_INPUT_DELETE_PROFIT_LIMIT_KEY = "fund.input.delete.profit.limit.key";

}
