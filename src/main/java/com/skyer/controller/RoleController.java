package com.skyer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.PermissionCode;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.MenuService;
import com.skyer.service.RoleService;
import com.skyer.service.UserRoleService;
import com.skyer.service.UserService;
import com.skyer.vo.Role;
import com.skyer.vo.UserRole;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 角色控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

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
    public Object getById(Integer id) {
        try {
            return super.success(roleService.getById(id));
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过ID查询角色出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
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
    public Object getByPage(Integer page, Integer limit, Role role) {
        JSONObject result = new JSONObject();
        try {
            List<Role> list = roleService.getByPage(page, limit, role);
            for (Role item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Long totalNum = roleService.getTotalNum(role);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", ResultEnum.SEARCH_ERROR.getCode());
            result.put("msg", ResultEnum.SEARCH_ERROR.getValue());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[page: {}, limit: {}]", page, limit);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询角色出错，错误信息：", e);
            e.printStackTrace();
        }
        return result;
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
    public Object doAdd(Role role) {
        try {
            roleService.add(role);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[role: {}]", role.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "添加角色出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue());
    }

    /**
     * 去到角色更新页面
     *
     * @param id 角色ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.ROLE_UPDATE)
    public String update(Integer id, Model model) {
        try {
            Role role = roleService.getById(id);
            model.addAttribute("role", role);
            return "/role/update";
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "去到角色修改页面出错，错误信息：", e);
            e.printStackTrace();
        }
        return "err";
    }

    /**
     * 修改角色
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.ROLE_UPDATE)
    @ResponseBody
    public Object doUpdate(Role role) {
        try {
            roleService.update(role);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[role: {}]", role.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改角色出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.ROLE_DELETE)
    @ResponseBody
    public Object delete(Integer id) {
        try {
            List<UserRole> userRoles = userRoleService.getByRoleId(id);
            if (userRoles != null && userRoles.size() > 0) {
                return super.fail(400, "该角色被其他用户引用，禁止删除！");
            }
            roleService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "删除角色出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
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
    public Object updateStatus(Integer roleId, Integer status) {
        try {
            Role role = roleService.getById(roleId);
            role.setStatus(status);
            roleService.update(role);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[roleId: {}, status: {}]", roleId, status);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改角色状态出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
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
    public JSONArray getRoleMenuTree(Integer roleId) {
        try {
            return roleService.getMenuTreeByRoleId(roleId);
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[roleId: {}]", roleId);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "去到给角色授权页面出错，错误信息：", e);
            e.printStackTrace();
        }
        return null;
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
    public Object setRoleMenu(Integer roleId, String menuIds, HttpServletRequest req) {
        try {
            roleService.setRoleMenu(roleId, menuIds);
            List<Map<String, Object>> menus = menuService.getMenuTreeByUserId(super.getCurrentUser().getId());
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(super.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return super.success(ResultEnum.UPDATE_ERROR.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[roleId: {}, menuIds: {}]", roleId, menuIds);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "设置角色权限出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

}
