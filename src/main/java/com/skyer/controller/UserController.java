package com.skyer.controller;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.PermissionCode;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.UserService;
import com.skyer.vo.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制层
 *
 * @author SKYER
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 去到用户管理页面
     */
    @RequestMapping("/index")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public String index() {
        return "user/user";
    }

    /**
     * 通过ID获取用户
     *
     * @param id 用户ID
     */
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    @ResponseBody
    public Object getById(Integer id) {
        try {
            return super.success(userService.getById(id));
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过ID查询用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
    }

    /**
     * 分页获取用户
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    @ResponseBody
    public Object getByPage(Integer page, Integer limit, User user) {
        JSONObject result = new JSONObject();
        try {
            List<User> list = userService.getByPage(page, limit, user);
            for (User item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Long totalNum = userService.getTotalNum(user);
            result.put("code", 0);
            result.put("msg", "");
            result.put("count", totalNum);
            result.put("data", list);
        } catch (Exception e) {
            result.put("code", ResultEnum.SEARCH_ERROR.getCode());
            result.put("msg", ResultEnum.SEARCH_ERROR.getValue());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[page: {}, limit: {}]", page, limit);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "分页查询用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 去到添加用户页面
     */
    @RequestMapping("/add")
    @RequiresPermissions(PermissionCode.USER_INSERT)
    public String add() {
        return "user/add";
    }

    /**
     * 添加用户
     */
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.USER_INSERT)
    @ResponseBody
    public Object doAdd(User user) {
        try {
            userService.add(user);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[user: {}]", user.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "添加用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue());
    }

    /**
     * 去到用户更新页面
     *
     * @param id 用户ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    public String update(Integer id, Model model) {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            return "/user/update";
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "去到用户修改页面出错，错误信息：", e);
            e.printStackTrace();
        }
        return "err";
    }

    /**
     * 修改用户
     */
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    @ResponseBody
    public Object doUpdate(User user) {
        try {
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[user: {}]", user.toString());
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.USER_DELETE)
    @ResponseBody
    public Object delete(Integer id) {
        try {
            userService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "删除用户出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue());
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态编码
     */
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.USER_SETSTATUS)
    @ResponseBody
    public Object updateStatus(Integer userId, Integer status) {
        try {
            User user = userService.getById(userId);
            user.setStatus(status);
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[userId: {}, status: {}]", userId, status);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "修改用户状态出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

    /**
     * 通过用户ID获取角色列表
     *
     * @param id 用户ID
     */
    @RequestMapping("/getRoleByUserId")
    @RequiresPermissions(PermissionCode.USER_SETROLE)
    @ResponseBody
    public Object getRoleByUserId(Integer id) {
        try {
            List<JSONObject> list = userService.getRoleByUserId(id);
            return super.success(list);
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[id: {}]", id);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "通过用户ID获取角色列表出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue());
    }

    /**
     * 设置用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    @RequestMapping("/setUserRole")
    @RequiresPermissions(PermissionCode.USER_SETROLE)
    @ResponseBody
    public Object setUserRole(Integer userId, String roleIds) {
        try {
            userService.setUserRole(userId, roleIds);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[userId: {}, roleIds: {}]", userId, roleIds);
            LOG.error(AppConst.ERROR_LOG_PREFIX + "设置用户角色出错，错误信息：", e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue());
    }

}
