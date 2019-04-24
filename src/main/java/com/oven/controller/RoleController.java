package com.oven.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.service.MenuService;
import com.oven.service.RoleService;
import com.oven.service.UserRoleService;
import com.oven.service.UserService;
import com.oven.vo.Role;
import com.oven.vo.User;
import com.oven.vo.UserRole;
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
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    @ResponseBody
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(roleService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 分页获取角色
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.ROLE_MANAGER)
    @ResponseBody
    public Object getByPage(Integer page, Integer limit, Role role) throws MyException {
        try {
            JSONObject result = new JSONObject();
            List<Role> list = roleService.getByPage(page, limit, role);
            for (Role item : list) {
                User createUser = userService.getById(item.getCreateId());
                item.setCreateName(createUser == null ? "" : createUser.getNickName());
                User lastModifyUser = userService.getById(item.getLastModifyId());
                item.setLastModifyName(lastModifyUser == null ? "" : lastModifyUser.getNickName());
            }
            Integer totalNum = roleService.getTotalNum(role);
            result.put("code", 0);
            result.put("msg", "");
            result.put("data", list);
            result.put("count", totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 去到添加角色页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.ROLE_INSERT)
    public String add() {
        return "role/add";
    }

    /**
     * 添加角色
     */
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.ROLE_INSERT)
    @ResponseBody
    public Object doAdd(Role role) throws MyException {
        try {
            roleService.add(role);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), "添加角色出错，请联系网站管理人员。", e);
        }
    }

    /**
     * 去到角色更新页面
     *
     * @param id 角色ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.ROLE_UPDATE)
    public String update(Integer id, Model model) throws MyException {
        try {
            Role role = roleService.getById(id);
            model.addAttribute("role", role);
            return "/role/update";
        } catch (Exception e) {
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 修改角色
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.ROLE_UPDATE)
    @ResponseBody
    public Object doUpdate(Role role) throws MyException {
        try {
            roleService.update(role);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "修改角色出错，请联系网站管理人员。", e);
        }
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.ROLE_DELETE)
    @ResponseBody
    public Object delete(Integer id) throws MyException {
        try {
            List<UserRole> userRoles = userRoleService.getByRoleId(id);
            if (userRoles != null && userRoles.size() > 0) {
                return super.fail(400, "该角色被其他用户引用，禁止删除！");
            }
            roleService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), "删除角色出错，请联系网站管理人员。", e);
        }
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 状态编码
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.ROLE_SETSTATUS)
    @ResponseBody
    public Object updateStatus(Integer roleId, Integer status) throws MyException {
        try {
            Role role = roleService.getById(roleId);
            role.setStatus(status);
            roleService.update(role);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "修改角色状态出错，请联系网站管理人员。", e);
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
    @RequestMapping("/getRoleMenuTree")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    @ResponseBody
    public JSONArray getRoleMenuTree(Integer roleId) throws MyException {
        try {
            return roleService.getMenuTreeByRoleId(roleId);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), AppConst.SYSTEM_ERROR, e);
        }
    }

    /**
     * 设置角色权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    @RequestMapping("/setRoleMenu")
    @RequiresPermissions(PermissionCode.ROLE_SETMENU)
    @ResponseBody
    public Object setRoleMenu(Integer roleId, String menuIds, HttpServletRequest req) throws MyException {
        try {
            roleService.setRoleMenu(roleId, menuIds);
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(super.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return super.success(ResultEnum.UPDATE_ERROR.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), "设置角色权限出错，请联系网站管理人员。", e);
        }
    }

}
