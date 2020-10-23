package com.oven.core.role.controller;

import com.alibaba.fastjson.JSONArray;
import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.LayuiPager;
import com.oven.core.base.controller.BaseController;
import com.oven.core.menu.service.MenuService;
import com.oven.core.role.service.RoleService;
import com.oven.core.role.vo.Role;
import com.oven.core.user.service.UserService;
import com.oven.core.user.vo.User;
import com.oven.core.userRole.service.UserRoleService;
import com.oven.core.userRole.vo.UserRole;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 角色控制层
 *
 * @author Oven
 */
@Slf4j
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

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
     * 通过ID获取角色
     *
     * @param id 角色ID
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(roleService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过ID获取角色异常", e);
        }
    }

    /**
     * 分页获取角色
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    public Object getByPage(Integer page, Integer limit, Role role) throws MyException {
        try {
            LayuiPager<Role> result = new LayuiPager<>();
            List<Role> list = roleService.getByPage(page, limit, role);
            for (Role item : list) {
                User createUser = userService.getById(item.getCreateId());
                item.setCreateName(createUser == null ? "" : createUser.getNickName());
                User lastModifyUser = userService.getById(item.getLastModifyId());
                item.setLastModifyName(lastModifyUser == null ? "" : lastModifyUser.getNickName());
            }
            Integer totalNum = roleService.getTotalNum(role);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取角色异常", e);
        }
    }

    /**
     * 添加角色
     */
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.ROLE_INSERT)
    @Limit(key = AppConst.ROLE_INSERT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object add(Role role) throws MyException {
        try {
            roleService.add(role);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加角色异常", e);
        }
    }

    /**
     * 修改角色
     */
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.ROLE_UPDATE)
    @Limit(key = AppConst.ROLE_UPDATE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object update(Role role) throws MyException {
        try {
            roleService.update(role);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改角色异常", e);
        }
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.ROLE_DELETE)
    @Limit(key = AppConst.ROLE_DELETE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            List<UserRole> userRoles = userRoleService.getByRoleId(id);
            if (userRoles != null && userRoles.size() > 0) {
                return super.fail(400, "该角色被其他用户引用，禁止删除！");
            }
            roleService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除角色异常", e);
        }
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 状态编码
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.ROLE_SETSTATUS)
    @Limit(key = AppConst.ROLE_UPDATE_STATUS_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object updateStatus(Integer roleId, Integer status) throws MyException {
        try {
            Role role = roleService.getById(roleId);
            role.setStatus(status);
            roleService.update(role);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改角色状态异常", e);
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
     * 根据角色ID获取权限树
     *
     * @param roleId 角色ID
     */
    @ResponseBody
    @RequestMapping("/getRoleMenuTree")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    public JSONArray getRoleMenuTree(Integer roleId) throws MyException {
        try {
            return roleService.getMenuTreeByRoleId(roleId);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "根据角色ID获取权限树异常", e);
        }
    }

    /**
     * 设置角色权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    @ResponseBody
    @RequestMapping("/setRoleMenu")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    @Limit(key = AppConst.ROLE_SET_ROLE_MENU_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object setRoleMenu(Integer roleId, String menuIds, HttpServletRequest req) throws MyException {
        try {
            roleService.setRoleMenu(roleId, menuIds);
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(super.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return super.success(ResultEnum.UPDATE_ERROR.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "设置角色权限异常", e);
        }
    }

}
