package com.oven.demo.core.role.controller;

import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.Result;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.menu.service.MenuService;
import com.oven.demo.core.role.entity.Role;
import com.oven.demo.core.role.service.RoleService;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.entity.UserRole;
import com.oven.demo.core.user.service.UserRoleService;
import com.oven.demo.core.user.service.UserService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色控制层
 *
 * @author Oven
 */
@ApiIgnore
@Controller
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 去到角色管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    public String index() {
        return "role/role";
    }

    /**
     * 通过id获取角色
     *
     * @param id 角色id
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    public Result<Object> getById(Integer id) throws MyException {
        try {
            return Result.success(roleService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "通过id获取角色异常", e);
        }
    }

    /**
     * 分页获取角色
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    public Object getByPage(@RequestBody Role role) throws MyException {
        try {
            List<Role> list = roleService.getByPage(role);
            for (Role item : list) {
                User createUser = userService.getById(item.getCreateId());
                item.setCreateName(createUser == null ? "" : createUser.getNickName());
                User lastModifyUser = userService.getById(item.getLastModifyId());
                item.setLastModifyName(lastModifyUser == null ? "" : lastModifyUser.getNickName());
            }
            Integer totalNum = roleService.getTotalNum(role);
            return LayuiPager.build(list, totalNum);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_PAGE_ERROR, "分页获取角色异常", e);
        }
    }

    /**
     * 添加角色
     */
    @ResponseBody
    @RequestMapping("/save")
    @AspectLog(title = "添加角色")
    @RequiresPermissions(PermissionCode.ROLE_INSERT)
    @Limit(key = LimitKey.ROLE_INSERT_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> save(Role role) throws MyException {
        try {
            roleService.save(role);
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.INSERT_ERROR, "添加角色异常", e);
        }
    }

    /**
     * 修改角色
     */
    @ResponseBody
    @RequestMapping("/update")
    @AspectLog(title = "修改角色")
    @RequiresPermissions(PermissionCode.ROLE_UPDATE)
    @Limit(key = LimitKey.ROLE_UPDATE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> update(Role role) throws MyException {
        try {
            if (role.getId() == 1 || role.getId() == 2) {
                return Result.fail(ResultCode.CAN_NOT_UPDATE_ROLE);
            }
            roleService.update(role);
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改角色异常", e);
        }
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    @ResponseBody
    @RequestMapping("/delete")
    @AspectLog(title = "删除角色")
    @RequiresPermissions(PermissionCode.ROLE_DELETE)
    @Limit(key = LimitKey.ROLE_DELETE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> delete(Integer id) throws MyException {
        try {
            if (id == 1 || id == 2) {
                return Result.fail(ResultCode.CAN_NOT_DELETE_ROLE);
            }
            List<UserRole> userRoles = userRoleService.getByRoleId(id);
            if (userRoles != null && !userRoles.isEmpty()) {
                return Result.fail(ResultCode.DELETE_ROLE_ERROR);
            }
            roleService.delete(id);
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.DELETE_ERROR, "删除角色异常", e);
        }
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色id
     * @param status 状态编码
     */
    @ResponseBody
    @AspectLog(title = "修改角色状态")
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.ROLE_SETSTATUS)
    @Limit(key = LimitKey.ROLE_UPDATE_STATUS_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> updateStatus(Integer roleId, Integer status) throws MyException {
        try {
            if (roleId == 1 || roleId == 2) {
                return Result.fail(ResultCode.CAN_NOT_UPDATE_ROLE);
            }
            Role role = roleService.getById(roleId);
            role.setStatus(status);
            roleService.update(role);
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改角色状态异常", e);
        }
    }

    /**
     * 去到给角色授权页面
     */
    @RequestMapping("/roleMenu")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    public String roleMenu(Integer roleId, Model model) {
        Role role = roleService.getById(roleId);
        model.addAttribute("role", role);
        return "role/setMenu";
    }

    /**
     * 根据角色id获取权限树
     *
     * @param roleId 角色id
     */
    @ResponseBody
    @RequestMapping("/getRoleMenuTree")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    public Result<Object> getRoleMenuTree(Integer roleId) throws MyException {
        try {
            return Result.success(roleService.getMenuTreeByRoleId(roleId));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "根据角色id获取权限树异常", e);
        }
    }

    /**
     * 设置角色权限
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    @ResponseBody
    @AspectLog(title = "设置角色权限")
    @RequestMapping("/setRoleMenu")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    @Limit(key = LimitKey.ROLE_SET_ROLE_MENU_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> setRoleMenu(Integer roleId, String menuIds, HttpServletRequest req) throws MyException {
        try {
            if (roleId == 1 || roleId == 2) {
                return Result.fail(ResultCode.CAN_NOT_SET_MENU);
            }
            roleService.setRoleMenu(roleId, menuIds);
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(CommonUtils.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return Result.success(ResultCode.UPDATE_ERROR);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "设置角色权限异常", e);
        }
    }

}
