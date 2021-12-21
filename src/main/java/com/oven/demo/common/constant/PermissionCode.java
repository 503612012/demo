package com.oven.demo.common.constant;

/**
 * 菜单权限Code定义类
 *
 * @author Oven
 */
public interface PermissionCode {

    String USER_MANAGER                       = "A1_01";      // 用户管理
    String USER_INSERT                        = "A1_01_01";   // 添加用户
    String USER_UPDATE                        = "A1_01_02";   // 修改用户
    String USER_DELETE                        = "A1_01_03";   // 删除用户
    String USER_SETSTATUS                     = "A1_01_04";   // 修改用户状态
    String USER_SETROLE                       = "A1_01_05";   // 设置用户角色
    String FORCE_LOGOUT                       = "A1_01_06";   // 强制退出
    String RESET_ERR_NUM                      = "A1_01_07";   // 重置错误次数
    String UPLOAD_AVATAR                      = "A1_01_08";   // 修改头像

    String MENU_MANAGER                       = "A1_02";      // 菜单管理
    String MENU_UPDATE                        = "A1_02_01";   // 修改菜单名称
    String MENU_SETSTATUS                     = "A1_02_02";   // 修改菜单状态

    String ROLE_MANAGER                       = "A1_03";      // 角色管理
    String ROLE_INSERT                        = "A1_03_01";   // 添加角色
    String ROLE_UPDATE                        = "A1_03_02";   // 修改角色
    String ROLE_DELETE                        = "A1_03_03";   // 删除角色
    String ROLE_SETSTATUS                     = "A1_03_04";   // 修改角色状态
    String ROLE_SETMENU                       = "A1_03_05";   // 设置角色菜单

    String LOG_MANAGER                        = "A1_04";      // 日志管理
    String MONITOR_MANAGER                    = "A1_05";      // 服务监控

    String EMPLOYEE_MANAGER                   = "B1_01";      // 员工管理
    String EMPLOYEE_INSERT                    = "B1_01_01";   // 添加员工
    String EMPLOYEE_UPDATE                    = "B1_01_02";   // 修改员工
    String EMPLOYEE_DELETE                    = "B1_01_03";   // 删除员工
    String EMPLOYEE_SETSTATUS                 = "B1_01_04";   // 修改员工状态
    String EMPLOYEE_SHOWMONEY                 = "B1_01_05";   // 显示金额

}
