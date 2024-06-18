package com.oven.demo.core.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.LayuiPager;
import com.oven.basic.common.util.Result;
import com.oven.basic.common.util.RsaUtils;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.service.UserService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.config.RsaProperties;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户控制层
 *
 * @author Oven
 */
@ApiIgnore
@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${avatar.path}")
    private String avatarPath;

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
     * 通过id获取用户
     *
     * @param id 用户id
     */
    @ResponseBody
    @RequestMapping("/getById")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public Result<Object> getById(Integer id) throws MyException {
        try {
            return Result.success(userService.getById(id));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "通过id获取用户异常", e);
        }
    }

    /**
     * 获取当前登录用户
     */
    @ResponseBody
    @RequestMapping("/getCurrentUserInfo")
    public Result<Object> getCurrentUserInfo() throws MyException {
        try {
            return Result.success(userService.getByUserName(CommonUtils.getCurrentUser().getUserName()));
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取当前登录用户异常", e);
        }
    }

    /**
     * 分页获取用户
     */
    @ResponseBody
    @RequestMapping("/getByPage")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public Object getByPage(@RequestBody User user, HttpServletRequest req) throws MyException {
        try {
            List<User> list = userService.getByPage(user);
            ServletContext context = req.getServletContext();
            // noinspection unchecked
            Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) context.getAttribute(AppConst.LOGINEDUSERS);
            for (User item : list) {
                item.setCreateName(userService.getById(item.getCreateId()).getNickName());
                item.setLastModifyName(userService.getById(item.getLastModifyId()).getNickName());

                // 在线状态
                item.setOnline(getOnlineStatus(loginedMap, item));
            }
            Integer totalNum = userService.getTotalNum(user);
            return LayuiPager.build(list, totalNum);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_PAGE_ERROR, "分页获取用户异常", e);
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
                if (!StringUtils.isEmpty(sessionId) && !ResultCode.FORCE_LOGOUT.message().equals(sessionId)) {
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
    public String add(Model model) {
        model.addAttribute("key", RsaProperties.publicKey);
        return "user/add";
    }

    /**
     * 添加用户
     */
    @ResponseBody
    @RequestMapping("/doAdd")
    @AspectLog(title = "添加用户")
    @RequiresPermissions(PermissionCode.USER_INSERT)
    @Limit(key = LimitKey.USER_INSERT_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> doAdd(User user) throws MyException {
        try {
            User userInDb = userService.getByUserName(user.getUserName());
            if (userInDb != null) {
                return Result.fail(ResultCode.USER_ALREADY_EXIST);
            }
            user.setPassword(RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, user.getPassword()));
            userService.save(user);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.INSERT_ERROR, "添加用户异常", e);
        }
    }

    /**
     * 判断用户名是否存在
     */
    @ResponseBody
    @RequestMapping("isExist")
    @RequiresPermissions(PermissionCode.USER_INSERT)
    public Result<Object> isExist(String userName) throws MyException {
        try {
            User user = userService.getByUserName(userName);
            if (user == null) {
                return Result.success(false);
            } else {
                return Result.success(true);
            }
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "判断用户名是否存在异常", e);
        }
    }

    /**
     * 去到用户更新页面
     *
     * @param id 用户id
     */
    @RequestMapping("/update")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    public String update(Integer id, Model model) throws MyException {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            return "/user/update";
        } catch (Exception e) {
            throw MyException.build(ResultCode.ERROR_PAGE, "去到用户更新页面异常", e);
        }
    }

    /**
     * 修改用户
     */
    @ResponseBody
    @AspectLog(title = "修改用户")
    @RequestMapping("/doUpdate")
    @RequiresPermissions(PermissionCode.USER_UPDATE)
    @Limit(key = LimitKey.USER_UPDATE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> doUpdate(User user) throws MyException {
        try {
            if (user.getId() == 1 || user.getId() == 2) {
                return Result.fail(ResultCode.CAN_NOT_UPDATE_USER);
            }
            userService.update(user);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改用户异常", e);
        }
    }

    /**
     * 删除用户
     *
     * @param id 用户id
     */
    @ResponseBody
    @RequestMapping("/delete")
    @AspectLog(title = "删除用户")
    @RequiresPermissions(PermissionCode.USER_DELETE)
    @Limit(key = LimitKey.USER_DELETE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.DELETE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> delete(Integer id) throws MyException {
        try {
            if (id == 1 || id == 2) {
                return Result.fail(ResultCode.CAN_NOT_DELETE_USER);
            }
            userService.delete(id);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.DELETE_ERROR, "删除用户异常", e);
        }
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户id
     * @param status 状态编码
     */
    @ResponseBody
    @AspectLog(title = "修改用户状态")
    @RequestMapping("/updateStatus")
    @RequiresPermissions(PermissionCode.USER_SETSTATUS)
    @Limit(key = LimitKey.USER_UPDATE_STATUS_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.UPDATE_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> updateStatus(Integer userId, Integer status) throws MyException {
        try {
            if (userId == 1 || userId == 2) {
                return Result.fail(ResultCode.CAN_NOT_UPDATE_USER);
            }
            User user = userService.getById(userId);
            user.setStatus(status);
            userService.update(user);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改用户状态异常", e);
        }
    }

    /**
     * 通过用户id获取角色列表
     *
     * @param id 用户id
     */
    @ResponseBody
    @RequestMapping("/getRoleByUserId")
    @RequiresPermissions(PermissionCode.USER_SETROLE)
    public Result<Object> getRoleByUserId(Integer id) throws MyException {
        try {
            List<JSONObject> list = userService.getRoleByUserId(id);
            return Result.success(list);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "通过用户id获取角色列表异常", e);
        }
    }

    /**
     * 设置用户角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    @ResponseBody
    @AspectLog(title = "设置用户角色")
    @RequestMapping("/setUserRole")
    @RequiresPermissions(PermissionCode.USER_SETROLE)
    @Limit(key = LimitKey.USER_SET_USER_ROLE_LIMIT_KEY, period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> setUserRole(Integer userId, String roleIds) throws MyException {
        try {
            if (userId == 1 || userId == 2) {
                return Result.fail(ResultCode.CAN_NOT_SET_ROLE);
            }
            userService.setUserRole(userId, roleIds);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "设置用户角色异常", e);
        }
    }

    /**
     * 获取所有用户
     */
    @ResponseBody
    @RequestMapping("/getAll")
    @RequiresPermissions(PermissionCode.USER_MANAGER)
    public Result<Object> getAll() throws MyException {
        try {
            return Result.success(userService.getAll());
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取所有用户异常", e);
        }
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @AspectLog(title = "修改密码")
    @RequestMapping("/changePwd")
    public Result<Object> changePwd(String oldPwd, String newPwd) throws MyException {
        try {
            String oldPwdDecode = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, oldPwd);
            User user = userService.getByUserName(CommonUtils.getCurrentUser().getUserName());
            if (user.getId() == 1 || user.getId() == 2) {
                return Result.fail(ResultCode.CAN_NOT_SET_PWD);
            }
            Md5Hash md5 = new Md5Hash(oldPwdDecode, AppConst.MD5_SALT, 2);
            // 密码错误
            if (!md5.toString().equals(user.getPassword())) {
                return Result.fail(ResultCode.OLD_PASSWORD_WRONG);
            }
            newPwd = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, newPwd);
            user.setPassword(newPwd);
            userService.update(user);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改密码异常", e);
        }
    }

    /**
     * 上传头像
     */
    @ResponseBody
    @AspectLog(title = "上传头像")
    @RequestMapping("/uploadAvatar")
    @RequiresPermissions(PermissionCode.UPLOAD_AVATAR)
    @Limit(key = LimitKey.USER_UPLOAD_AVATAR_LIMIT_KEY, period = 5, count = 1, errMsg = LimitKey.INSERT_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Result<Object> uploadAvatar(MultipartFile file, HttpServletRequest req) throws MyException {
        try {
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                return Result.fail(ResultCode.UPDATE_ERROR.code(), "文件名称为空，请重新上传！");
            }
            String fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            File path = new File(avatarPath);
            if (!path.exists()) {
                // noinspection ResultOfMethodCallIgnored
                path.mkdirs();
            }
            File savedFile = new File(avatarPath, fileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), savedFile);
            userService.updateAvatar(CommonUtils.getCurrentUser().getId(), "/avatar/" + fileName);
            User userInSession = (User) req.getSession().getAttribute(AppConst.CURRENT_USER);
            userInSession.setAvatar("/avatar/" + fileName);
            req.getSession().setAttribute(AppConst.CURRENT_USER, userInSession);
            return Result.success("保存成功！");
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPLOAD_ERROR, "上传头像异常", e);
        }
    }

    /**
     * 重置错误次数
     */
    @ResponseBody
    @AspectLog(title = "重置错误次数")
    @RequestMapping("/resetErrNum")
    @RequiresPermissions(PermissionCode.RESET_ERR_NUM)
    public Result<Object> resetErrNum(Integer userId) throws MyException {
        try {
            userService.resetErrNum(userId);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "重置错误次数异常", e);
        }
    }

    /**
     * 修改主题
     */
    @ResponseBody
    @AspectLog(title = "修改主题")
    @RequestMapping("/userTheme")
    @RequiresPermissions(PermissionCode.USER_THEME)
    public Result<Object> userTheme(String userTheme, HttpServletRequest req) throws MyException {
        try {
            userService.updateConfig("userTheme", userTheme);
            req.getSession().setAttribute("userTheme", userTheme);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改主题异常", e);
        }
    }

    /**
     * 修改菜单位置
     */
    @ResponseBody
    @AspectLog(title = "修改菜单位置")
    @RequestMapping("/menuPosition")
    @RequiresPermissions(PermissionCode.MENU_POSITION)
    public Result<Object> menuPosition(String menuPosition, HttpServletRequest req) throws MyException {
        try {
            userService.updateConfig("menuPosition", menuPosition);
            req.getSession().setAttribute("menuPosition", menuPosition);
            return Result.success(ResultCode.SUCCESS);
        } catch (Exception e) {
            throw MyException.build(ResultCode.UPDATE_ERROR, "修改菜单位置异常", e);
        }
    }

}
