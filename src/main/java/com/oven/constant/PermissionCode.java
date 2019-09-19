package com.oven.constant;

/**
 * 菜单权限Code定义类
 *
 * @author Oven
 */
public class PermissionCode {

    public static final String USER_MANAGER       = "A1_01";      // 用户管理
    public static final String USER_INSERT        = "A1_01_01";   // 添加用户
    public static final String USER_UPDATE        = "A1_01_02";   // 修改用户
    public static final String USER_DELETE        = "A1_01_03";   // 删除用户
    public static final String USER_SETSTATUS     = "A1_01_04";   // 修改用户状态
    public static final String USER_SETROLE       = "A1_01_05";   // 设置用户角色

    public static final String MENU_MANAGER       = "A1_02";      // 菜单管理
    public static final String MENU_UPDATE        = "A1_02_01";   // 修改菜单名称
    public static final String MENU_SETSTATUS     = "A1_02_02";   // 修改菜单状态

    public static final String ROLE_MANAGER       = "A1_03";      // 角色管理
    public static final String ROLE_INSERT        = "A1_03_01";   // 添加角色
    public static final String ROLE_UPDATE        = "A1_03_02";   // 修改角色
    public static final String ROLE_DELETE        = "A1_03_03";   // 删除角色
    public static final String ROLE_SETSTATUS     = "A1_03_04";   // 修改角色状态
    public static final String ROLE_SETMENU       = "A1_03_05";   // 设置角色菜单

    public static final String LOG_MANAGER        = "A1_04";      // 日志管理

    public static final String EMPLOYEE_MANAGER   = "B1_01";      // 员工管理
    public static final String EMPLOYEE_INSERT    = "B1_01_01";   // 添加员工
    public static final String EMPLOYEE_UPDATE    = "B1_01_02";   // 修改员工
    public static final String EMPLOYEE_DELETE    = "B1_01_03";   // 删除员工
    public static final String EMPLOYEE_SETSTATUS = "B1_01_04";   // 修改员工状态
    public static final String EMPLOYEE_SHOWMONEY = "B1_01_05";   // 显示金额

    public static final String ECHARTS_MANAGER    = "D1_01";      // Echarts功能

    public static final String WORKHOUR_MANAGER   = "B1_02";      // 工时管理
    public static final String WORKHOUR_INSERT    = "B1_02_01";   // 工时录入
    public static final String WORKHOUR_DELETE    = "B1_02_02";   // 工时录入

    public static final String WORKSITE_MANAGER   = "B1_03";      // 工地管理
    public static final String WORKSITE_INSERT    = "B1_03_01";   // 添加工地
    public static final String WORKSITE_UPDATE    = "B1_03_02";   // 修改工地
    public static final String WORKSITE_DELETE    = "B1_03_03";   // 删除工地
    public static final String WORKSITE_SETSTATUS = "B1_03_04";   // 修改工地状态

    public static final String SALARY_SET         = "C1";         // 工资统计
    public static final String SALARY_COUNT       = "C1_01";      // 工资统计
    public static final String SALARY_SEND        = "C1_01_01";   // 工资发放
    public static final String SALARY_LOG         = "C1_02";      // 发薪记录

}
