package com.oven.demo.core.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.DateUtils;
import com.oven.basic.common.util.EncryptUtils;
import com.oven.basic.common.util.IPUtils;
import com.oven.basic.common.util.ResultInfo;
import com.oven.basic.common.vcode.Captcha;
import com.oven.basic.common.vcode.GifCaptcha;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.employee.entity.Employee;
import com.oven.demo.core.employee.service.EmployeeService;
import com.oven.demo.core.log.service.LogService;
import com.oven.demo.core.menu.service.MenuService;
import com.oven.demo.core.system.service.SysDicService;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.service.UserService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.exception.MyException;
import com.oven.demo.framework.limitation.Limit;
import com.oven.demo.framework.limitation.LimitKey;
import com.oven.demo.framework.limitation.LimitType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 系统控制器
 *
 * @author Oven
 */
@Slf4j
@ApiIgnore
@Controller
public class SystemController {

    @Resource
    private LogService logService;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private SysDicService sysDicService;
    @Resource
    private LoggingSystem loggingSystem;
    @Resource
    private EmployeeService employeeService;

    /**
     * 修改日志级别
     */
    @ResponseBody
    @RequestMapping("/changeLogLevel")
    public Object changeLogLevel(String logger, String level) throws MyException {
        try {
            LogLevel logLevel = LogLevel.valueOf(level.toUpperCase());
            loggingSystem.setLogLevel(logger, logLevel);
            log.info("修改日志级别{} to {}，更新完毕", logger, level);
            return ResultInfo.success("修改成功");
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SYSTEM_ERROR.getCode(), "修改日志级别异常！", "修改日志级别异常", e);
        }
    }

    /**
     * 秒杀模拟
     */
    @ResponseBody
    @RequestMapping("/secKill")
    public ResultInfo<Object> secKill() {
        try {
            sysDicService.secKill();
            return ResultInfo.success("秒杀成功！");
        } catch (Exception e) {
            return ResultInfo.fail(400, "秒杀接口异常！");
        }
    }

    /**
     * 获取验证码（Gif版本）
     */
    @RequestMapping("/getGifCode")
    public void getGifCode(HttpServletResponse response, HttpServletRequest request) throws MyException {
        try {
            HttpSession session = request.getSession(true);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            /*
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146, 33, 4);
            captcha.out(response.getOutputStream());
            session.setAttribute(AppConst.CAPTCHA, captcha.text().toLowerCase());
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_ERROR.getCode(), "获取验证码异常！", "获取验证码异常", e);
        }
    }

    /**
     * 欢迎页面
     */
    @RequestMapping("/main.html")
    public String mainPage() {
        return "main";
    }

    /**
     * 默认页面(系统主页面)
     */
    @RequestMapping("/")
    public String index(HttpServletRequest req) {
        req.getSession().setAttribute("key", EncryptUtils.KEY);
        return "index";
    }

    /**
     * 去到登录页面
     */
    @RequestMapping("/login")
    public String login(String errorMsg, Model model) {
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("key", EncryptUtils.KEY);
        return "login";
    }

    /**
     * 登出操作
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest req) {
        try {
            User user = CommonUtils.getCurrentUser();
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked") Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap != null) {
                loginedMap.remove(user.getUserName());
            }
            req.setAttribute("key", EncryptUtils.KEY);
            req.getSession().removeAttribute(AppConst.CURRENT_USER);
            req.getSession().getServletContext().removeAttribute(AppConst.CURRENT_USER);
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            log.error("登录操作出错，请联系网站管理人员。：", e);
        }
        return "login";
    }

    /**
     * 强制退出
     */
    @ResponseBody
    @AspectLog(title = "强制退出")
    @RequestMapping("/forceLogout")
    @RequiresPermissions(PermissionCode.FORCE_LOGOUT)
    public ResultInfo<Object> forceLogout(String userName, HttpServletRequest req) throws MyException {
        try {
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked") Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap != null) {
                JSONObject obj = new JSONObject();
                obj.put(AppConst.SESSION_ID, ResultEnum.FORCE_LOGOUT.getValue());
                obj.put(AppConst.SESSION, null);
                loginedMap.put(userName, obj);
            }
            return ResultInfo.success("退出成功");
        } catch (Exception e) {
            throw MyException.build(ResultEnum.FORCE_LOGOUT_ERROR.getCode(), ResultEnum.FORCE_LOGOUT_ERROR.getValue(), "强制退出异常", e);
        }
    }

    /**
     * 去到错误页面
     */
    @RequestMapping("/noauth")
    public String noauth() {
        return "noauth";
    }

    /**
     * 去到错误页面
     */
    @RequestMapping("/err")
    public String err() {
        return "err";
    }

    /**
     * 登录操作
     *
     * @param userName 用户名
     * @param pwd      密码
     */
    @ResponseBody
    @RequestMapping("/doLogin")
    public ResultInfo<Object> doLogin(String userName, String pwd, String inputCode, boolean rememberMe, HttpServletRequest req) throws MyException {
        try {
            // 校验验证码
            String code = (String) req.getSession().getAttribute(AppConst.CAPTCHA);
            if (StringUtils.isEmpty(inputCode)) {
                return ResultInfo.fail(ResultEnum.CAPTCHA_IS_NONE.getCode(), ResultEnum.CAPTCHA_IS_NONE.getValue());
            } else {
                if (!inputCode.equalsIgnoreCase(code)) {
                    return ResultInfo.fail(ResultEnum.CAPTCHA_ERROR.getCode(), ResultEnum.CAPTCHA_ERROR.getValue());
                }
            }

            Subject subject = SecurityUtils.getSubject();
            pwd = EncryptUtils.aesDecrypt(pwd, EncryptUtils.KEY);
            UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd, rememberMe);
            subject.login(token);

            User userInDb = userService.getByUserName(userName);
            if (userInDb.getErrNum() >= 5 && (userInDb.getId() != 1 && userInDb.getId() != 2)) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.OVER_WRONG_NUM.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return ResultInfo.fail(ResultEnum.OVER_WRONG_NUM.getCode(), ResultEnum.OVER_WRONG_NUM.getValue());
            }
            // 登录成功后放入application，防止同一个账户多人登录
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked") Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap == null) {
                loginedMap = new HashMap<>();
                application.setAttribute(AppConst.LOGINEDUSERS, loginedMap);
            }
            JSONObject obj = new JSONObject();
            obj.put(AppConst.SESSION_ID, req.getSession().getId());
            obj.put(AppConst.SESSION, req.getSession());
            loginedMap.put(userInDb.getUserName(), obj);

            // 登录成功后放入session中
            req.getSession().setAttribute(AppConst.CURRENT_USER, userInDb);
            logService.addLog("登录系统！", "成功！", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
            userService.updateLastLoginTime(DateUtils.getCurrentTime(), userInDb.getId());

            // 获取该用户的所有权限编码，放入session中
            Object userMenus = req.getSession().getAttribute(AppConst.USER_MENU);
            if (userMenus == null) {
                userMenus = menuService.getAllMenuCodeByUserId(userInDb.getId());
                req.getSession().setAttribute(AppConst.USER_MENU, userMenus);
            }
            if (StringUtils.isEmpty(userInDb.getConfig())) {
                req.getSession().setAttribute("userTheme", "light");
                req.getSession().setAttribute("menuPosition", "left");
            } else {
                String userTheme = JSONObject.parseObject(userInDb.getConfig()).getString("userTheme");
                req.getSession().setAttribute("userTheme", StringUtils.isEmpty(userTheme) ? "light" : userTheme);
                String menuPosition = JSONObject.parseObject(userInDb.getConfig()).getString("menuPosition");
                req.getSession().setAttribute("menuPosition", StringUtils.isEmpty(menuPosition) ? "left" : menuPosition);
            }

            return ResultInfo.success("登录成功！");
        } catch (Exception e) {
            User userInDb = userService.getByUserName(userName);
            if (e instanceof UnknownAccountException) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.NO_THIS_USER.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return ResultInfo.fail(ResultEnum.NO_THIS_USER.getCode(), ResultEnum.NO_THIS_USER.getValue());
            } else if (e instanceof IncorrectCredentialsException) {
                if (userInDb.getErrNum() >= 5) {
                    logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.OVER_WRONG_NUM.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                    return ResultInfo.fail(ResultEnum.OVER_WRONG_NUM.getCode(), ResultEnum.OVER_WRONG_NUM.getValue());
                }
                userService.logPasswordWrong(userInDb.getId());
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.PASSWORD_WRONG.getValue() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return ResultInfo.fail(ResultEnum.PASSWORD_WRONG.getCode(), ResultEnum.PASSWORD_WRONG.getValue());
            } else if (e instanceof LockedAccountException) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.USER_DISABLE.getValue() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return ResultInfo.fail(ResultEnum.USER_DISABLE.getCode(), ResultEnum.USER_DISABLE.getValue());
            } else {
                throw MyException.build(ResultEnum.UNKNOW_ERROR.getCode(), "登录操作出错，请联系网站管理人员。", "登录操作异常", e);
            }
        }
    }

    /**
     * 获取当前登录用户的菜单
     */
    @ResponseBody
    @RequestMapping("/getMenus")
    public ResultInfo<Object> getMenus(HttpServletRequest req) throws MyException {
        try {
            List<Map<String, Object>> menus = menuService.getMenuTreeByUserId(CommonUtils.getCurrentUser().getId());
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(CommonUtils.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return ResultInfo.success(menus);
        } catch (Exception e) {
            throw MyException.build(ResultEnum.UNKNOW_ERROR.getCode(), "获取当前登录用户出错，请联系网站管理人员。", "获取当前登录用户的菜单异常", e);
        }
    }

    /**
     * 获取首页数据
     */
    @ResponseBody
    @RequestMapping("/getMainPageData")
    public ResultInfo<Object> getMainPageData() throws MyException {
        try {
            JSONObject obj = new JSONObject();
            Integer totalEmployee = employeeService.getTotalNum(new Employee());
            obj.put("totalPay", "0.00");
            obj.put("totalEmployee", totalEmployee);
            obj.put("totalWorksite", "0");
            obj.put("totalWorkhour", "0.00");
            return ResultInfo.success(obj);
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取首页数据异常", e);
        }
    }

    /**
     * 获取薪资排行前五
     */
    @ResponseBody
    @RequestMapping("/getSalaryTopFive")
    public Object getSalaryTopFive() throws MyException {
        try {
            Map<String, List<String>> result = new HashMap<>();
            List<Map<String, Object>> list = new ArrayList<>();
            List<String> employeeNames = new ArrayList<>();
            List<String> data = new ArrayList<>();
            if (!CollectionUtils.isEmpty(list)) {
                for (Map<String, Object> item : list) {
                    employeeNames.add(String.valueOf(item.get("employeeName")));
                    data.add(String.valueOf(item.get("totalMoney")));
                }
            }
            result.put("employeeNames", employeeNames);
            result.put("data", data);
            return result;
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取薪资排行前五异常", e);
        }
    }

    /**
     * 获取首页占比信息
     */
    @ResponseBody
    @RequestMapping("/getProportionData")
    public ResultInfo<Object> getProportionData() throws MyException {
        try {
            JSONObject obj = new JSONObject();
            obj.put("salaryProportion", "0.00");
            obj.put("workhourProportion", "0.00");
            return ResultInfo.success(obj);
        } catch (Exception e) {
            throw MyException.build(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取首页占比信息异常", e);
        }
    }

    /**
     * 测试接口限流
     */
    @ResponseBody
    @RequestMapping("/limit")
    @Limit(key = "limit", period = LimitKey.LIMIT_TIME, count = 1, errMsg = LimitKey.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public String limit() {
        return UUID.randomUUID().toString();
    }

}
