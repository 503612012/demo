package com.oven.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.EncryptUtils;
import com.oven.common.util.LayuiPager;
import com.oven.core.base.controller.BaseController;
import com.oven.core.user.service.UserService;
import com.oven.core.user.vo.User;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户控制层
 *
 * @author Oven
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;
    @Resource(name = "sessionManager")
    private DefaultSessionManager sessionManager;

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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过ID获取用户异常", e);
        }
    }

    /**
     * 获取当前登录用户
     */
    @ResponseBody
    @RequestMapping("/getCurrentUserInfo")
    public Object getCurrentUserInfo() throws MyException {
        try {
            return super.success(userService.getByUserName(super.getCurrentUser().getUserName()));
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取当前登录用户异常", e);
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
    public Object getByPage(Integer page, Integer limit, User user, HttpServletRequest req) throws MyException {
        try {
            LayuiPager<User> result = new LayuiPager<>();
            List<User> list = userService.getByPage(page, limit, user);

            ServletContext context = req.getServletContext();
            //noinspection unchecked
            Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) context.getAttribute(AppConst.LOGINEDUSERS);
            for (User item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());

                // 在线状态
                item.setOnline(getOnlineStatus(loginedMap, item));
            }
            Integer totalNum = userService.getTotalNum(user);
            result.setCode(0);
            result.setMsg("");
            result.setData(list);
            result.setCount(totalNum);
            return result;
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_PAGE_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "分页获取用户异常", e);
        }
    }

    /**
     * 判断在线状态
     */
    private boolean getOnlineStatus(Map<String, JSONObject> loginedMap, User user) {
        if (loginedMap != null) {
            if (loginedMap.containsKey(user.getUserName())) {
                JSONObject obj = loginedMap.get(user.getUserName());
                String sessionId = obj.getString(AppConst.SESSION_ID);
                if (!StringUtils.isEmpty(sessionId) && !ResultEnum.FORCE_LOGOUT.getValue().equals(sessionId)) {
                    Collection<Session> activeSessions = sessionManager.getSessionDAO().getActiveSessions();
                    for (Session s : activeSessions) {
                        if (sessionId.equals(s.getId())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
    @Limit(key = AppConst.USER_INSERT_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doAdd(User user) throws MyException {
        try {
            User userInDb = userService.getByUserName(user.getUserName());
            if (userInDb != null) {
                return super.fail(ResultEnum.USER_ALREADY_EXIST.getCode(), ResultEnum.USER_ALREADY_EXIST.getValue());
            }
            userService.add(user);
            return super.success(ResultEnum.INSERT_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.INSERT_ERROR.getCode(), ResultEnum.INSERT_ERROR.getValue(), "添加用户异常", e);
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "判断用户名是否存在异常", e);
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
            throw new MyException(ResultEnum.ERROR_PAGE.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "去到用户更新页面异常", e);
        }
    }

    /**
     * 修改用户
     */
    @ResponseBody
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    @Limit(key = AppConst.USER_UPDATE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object doUpdate(User user) throws MyException {
        try {
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改用户异常", e);
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
    @Limit(key = AppConst.USER_DELETE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object delete(Integer id) throws MyException {
        try {
            userService.delete(id);
            return super.success(ResultEnum.DELETE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.DELETE_ERROR.getCode(), ResultEnum.DELETE_ERROR.getValue(), "删除用户异常", e);
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
    @Limit(key = AppConst.USER_UPDATE_STATUS_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object updateStatus(Integer userId, Integer status) throws MyException {
        try {
            User user = userService.getById(userId);
            user.setStatus(status);
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改用户状态异常", e);
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "通过用户ID获取角色列表异常", e);
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
    @Limit(key = AppConst.USER_SET_USER_ROLE_LIMIT_KEY, period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object setUserRole(Integer userId, String roleIds) throws MyException {
        try {
            userService.setUserRole(userId, roleIds);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "设置用户角色异常", e);
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取所有用户异常", e);
        }
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @RequestMapping("/changePwd")
    public Object changePwd(String oldPwd, String newPwd) throws MyException {
        try {
            String oldPwdDecode = EncryptUtils.aesDecrypt(oldPwd, EncryptUtils.KEY);
            User user = userService.getByUserName(super.getCurrentUser().getUserName());
            Md5Hash md5 = new Md5Hash(oldPwdDecode, AppConst.MD5_SALT, 2);
            // 密码错误
            if (!md5.toString().equals(user.getPassword())) {
                return super.fail(ResultEnum.OLD_PASSWORD_WRONG.getCode(), ResultEnum.OLD_PASSWORD_WRONG.getValue());
            }
            newPwd = EncryptUtils.aesDecrypt(newPwd, EncryptUtils.KEY);
            user.setPassword(newPwd);
            userService.update(user);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "修改密码异常", e);
        }
    }

    /**
     * 重置错误次数
     */
    @ResponseBody
    @RequestMapping("/resetErrNum")
    @RequiresPermissions(PermissionCode.RESET_ERR_NUM)
    public Object resetErrNum(Integer userId) throws MyException {
        try {
            userService.resetErrNum(userId);
            return super.success(ResultEnum.UPDATE_SUCCESS.getValue());
        } catch (Exception e) {
            throw new MyException(ResultEnum.UPDATE_ERROR.getCode(), ResultEnum.UPDATE_ERROR.getValue(), "重置错误次数异常", e);
        }
    }

}
