package com.oven.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.constant.AppConst;
import com.oven.constant.PermissionCode;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import com.oven.service.UserService;
import com.oven.vo.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/user")
public class UserController extends com.oven.controller.BaseController {

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
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public Object getById(Integer id) throws MyException {
        try {
            return super.success(userService.getById(id));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 分页获取用户
     *
     * @param page  页码
     * @param limit 每页显示数量
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public Object getByPage(Integer page, Integer limit, User user) throws MyException {
        try {
            JSONObject result = new JSONObject();
            List<User> list = userService.getByPage(page, limit, user);
            for (User item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());
            }
            Integer totalNum = userService.getTotalNum(user);
            result.put("msg", "");
            result.put("code", 0);
            result.put("count", totalNum);
            result.put("data", list);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
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
    @ResponseBody
    @RequestMapping("/doAdd")
    @RequiresPermissions(PermissionCode.USER_INSERT)
    @Limit(key = AppConst.USER_INSERT_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.CUSTOMER)
    public Object doAdd(User user) throws MyException {
        try {
            User userInDb = userService.getByUserName(user.getUserName());
            if (userInDb != null) {
                return super.success(true);
            }
            userService.add(user);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), e);
        }
    }

    /**
     * 判断用户名是否存在
     */
    @ResponseBody
    @RequestMapping("isExist")
    @RequiresPermissions(PermissionCode.USER_INSERT)
    public Object isExist(String userName) throws MyException {
        try {
            User user = userService.getByUserName(userName);
            if (user == null) {
                return super.success(false);
            } else {
                return super.success(true);
            }
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 去到用户更新页面
     *
     * @param id 用户ID
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    public String update(Integer id, Model model) throws MyException {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            return "/user/update";
        } catch (Exception e) {
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 修改用户
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    @Limit(key = AppConst.USER_UPDATE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object doUpdate(User user) throws MyException {
        try {
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions(PermissionCode.USER_DELETE)
    @Limit(key = AppConst.USER_DELETE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object delete(Integer id) throws MyException {
        try {
            userService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), e);
        }
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态编码
     */
    @ResponseBody
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.USER_SETSTATUS)
    @Limit(key = AppConst.USER_UPDATE_STATUS_LIMIT_KEY, period = 5, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.CUSTOMER)
    public Object updateStatus(Integer userId, Integer status) throws MyException {
        try {
            User user = userService.getById(userId);
            user.setStatus(status);
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

    /**
     * 通过用户ID获取角色列表
     *
     * @param id 用户ID
     */
    @ResponseBody
    @RequestMapping("/getRoleByUserId")
    @RequiresPermissions(PermissionCode.USER_SETROLE)
    public Object getRoleByUserId(Integer id) throws MyException {
        try {
            List<JSONObject> list = userService.getRoleByUserId(id);
            return super.success(list);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

    /**
     * 设置用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    @ResponseBody
    @RequestMapping("/setUserRole")
    @RequiresPermissions(PermissionCode.USER_SETROLE)
    @Limit(key = AppConst.USER_SET_USER_ROLE_LIMIT_KEY, period = 10, count = 1, errMsg = AppConst.SYSTEM_LIMIT, limitType = LimitType.CUSTOMER)
    public Object setUserRole(Integer userId, String roleIds) throws MyException {
        try {
            userService.setUserRole(userId, roleIds);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), e);
        }
    }

    /**
     * 获取所有用户
     */
    @ResponseBody
    @RequestMapping("/getAll")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public Object getAll() throws MyException {
        try {
            return super.success(userService.getAll());
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), e);
        }
    }

}
