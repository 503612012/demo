package com.oven.demo.common.constant;

/**
 * 菜单权限Code定义类
 *
 * @author Oven
 */
public interface PermissionCode {

    String USER_MANAGER                       = "sys:user";                     // 用户管理
    String USER_INSERT                        = "sys:user:add";                 // 添加用户
    String USER_UPDATE                        = "sys:user:update";              // 修改用户
    String USER_DELETE                        = "sys:user:del";                 // 删除用户
    String USER_SETSTATUS                     = "sys:user:status";              // 修改用户状态
    String USER_SETROLE                       = "sys:user:setrole";             // 设置用户角色
    String FORCE_LOGOUT                       = "sys:user:forcelogout";         // 强制退出
    String RESET_ERR_NUM                      = "sys:user:reset";               // 重置错误次数
    String UPLOAD_AVATAR                      = "sys:user:avatar";              // 修改头像
    String USER_THEME                         = "sys:user:theme";               // 修改主题
    String MENU_POSITION                      = "sys:user:menuposition";        // 菜单位置

    String MENU_MANAGER                       = "sys:menu";                     // 菜单管理
    String MENU_UPDATE                        = "sys:menu:update";              // 修改菜单名称
    String MENU_SETSTATUS                     = "sys:menu:status";              // 修改菜单状态

    String ROLE_MANAGER                       = "sys:role";                     // 角色管理
    String ROLE_INSERT                        = "sys:role:add";                 // 添加角色
    String ROLE_UPDATE                        = "sys:role:update";              // 修改角色
    String ROLE_DELETE                        = "sys:role:del";                 // 删除角色
    String ROLE_SETSTATUS                     = "sys:role:status";              // 修改角色状态
    String ROLE_SETMENU                       = "sys:role:setmenu";             // 设置角色菜单

    String SYSDIC_MANAGER                     = "sys:dic";                      // 数据字典管理
    String SYSDIC_INSERT                      = "sys:dic:add";                  // 添加数据字典
    String SYSDIC_UPDATE                      = "sys:dic:update";               // 修改数据字典
    String SYSDIC_DELETE                      = "sys:dic:del";                  // 删除数据字典
    String SYSDIC_RELOAD                      = "sys:dic:reload";               // 重载数据字典
    String SYSDIC_STATUS                      = "sys:dic:status";               // 修改数据字典状态

    String LOG_MANAGER                        = "sys:log";                      // 日志查看
    String MONITOR_MANAGER                    = "sys:monitor";                  // 服务监控

    String EMPLOYEE_MANAGER                   = "employee:employee";            // 员工管理
    String EMPLOYEE_INSERT                    = "employee:employee:add";        // 添加员工
    String EMPLOYEE_UPDATE                    = "employee:employee:update";     // 修改员工
    String EMPLOYEE_DELETE                    = "employee:employee:del";        // 删除员工
    String EMPLOYEE_SETSTATUS                 = "employee:employee:status";     // 修改员工状态
    String EMPLOYEE_SHOW_MONEY                = "employee:employee:showmeoney"; // 显示金额

}
