package com.oven.demo.core.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.DateUtils;
import com.oven.basic.common.util.IPUtils;
import com.oven.basic.common.util.Result;
import com.oven.basic.common.util.RsaUtils;
import com.oven.basic.common.vcode.Captcha;
import com.oven.basic.common.vcode.GifCaptcha;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.PermissionCode;
import com.oven.demo.common.enumerate.ResultCode;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.employee.entity.Employee;
import com.oven.demo.core.employee.service.EmployeeService;
import com.oven.demo.core.log.service.LogService;
import com.oven.demo.core.menu.service.MenuService;
import com.oven.demo.core.system.service.SysDicService;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.service.UserService;
import com.oven.demo.framework.annotation.AspectLog;
import com.oven.demo.framework.config.RsaProperties;
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
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.SYSTEM_ERROR, "修改日志级别异常", e);
        }
    }

    /**
     * 秒杀模拟
     */
    @ResponseBody
    @RequestMapping("/secKill")
    public Result<Object> secKill() {
        try {
            sysDicService.secKill();
            return Result.success("秒杀成功！");
        } catch (Exception e) {
            return Result.fail(ResultCode.SECKILL_ERROR);
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
            throw MyException.build(ResultCode.GET_CAPTCHA_ERROR, "获取验证码异常", e);
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
        req.getSession().setAttribute("key", RsaProperties.publicKey);
        return "index";
    }

    /**
     * 去到登录页面
     */
    @RequestMapping("/login")
    public String login(String errorMsg, Model model) {
        model.addAttribute("errorMsg", errorMsg);
        model.addAttribute("key", RsaProperties.publicKey);
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
            req.setAttribute("key", RsaProperties.publicKey);
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
    public Result<Object> forceLogout(String userName, HttpServletRequest req) throws MyException {
        try {
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked") Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap != null) {
                JSONObject obj = new JSONObject();
                obj.put(AppConst.SESSION_ID, ResultCode.FORCE_LOGOUT.message());
                obj.put(AppConst.SESSION, null);
                loginedMap.put(userName, obj);
            }
            return Result.success();
        } catch (Exception e) {
            throw MyException.build(ResultCode.FORCE_LOGOUT_ERROR, "强制退出异常", e);
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
    public Result<Object> doLogin(String userName, String pwd, String inputCode, boolean rememberMe, HttpServletRequest req) throws MyException {
        try {
            // 校验验证码
            String code = (String) req.getSession().getAttribute(AppConst.CAPTCHA);
            if (StringUtils.isEmpty(inputCode)) {
                return Result.fail(ResultCode.CAPTCHA_IS_NONE);
            } else {
                if (!inputCode.equalsIgnoreCase(code)) {
                    return Result.fail(ResultCode.CAPTCHA_ERROR);
                }
            }

            Subject subject = SecurityUtils.getSubject();
            pwd = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, pwd);
            UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd, rememberMe);
            subject.login(token);

            User userInDb = userService.getByUserName(userName);
            if (userInDb.getErrNum() >= 5 && (userInDb.getId() != 1 && userInDb.getId() != 2)) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultCode.OVER_WRONG_NUM.message() + "]", 0, userName, IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return Result.fail(ResultCode.OVER_WRONG_NUM);
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

            return Result.success();
        } catch (Exception e) {
            User userInDb = userService.getByUserName(userName);
            if (e instanceof UnknownAccountException) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultCode.NO_THIS_USER.message() + "]", 0, "", IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return Result.fail(ResultCode.NO_THIS_USER);
            } else if (e instanceof IncorrectCredentialsException) {
                if (userInDb.getErrNum() >= 5) {
                    logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultCode.OVER_WRONG_NUM.message() + "]", 0, "", IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                    return Result.fail(ResultCode.OVER_WRONG_NUM);
                }
                userService.logPasswordWrong(userInDb.getId());
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultCode.PASSWORD_WRONG.message() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return Result.fail(ResultCode.PASSWORD_WRONG);
            } else if (e instanceof LockedAccountException) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultCode.USER_DISABLE.message() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req), req.getRequestURI(), req.getMethod());
                return Result.fail(ResultCode.USER_DISABLE);
            } else {
                throw MyException.build(ResultCode.LOGIN_ERROR, "登录操作异常", e);
            }
        }
    }

    /**
     * 获取当前登录用户的菜单
     */
    @ResponseBody
    @RequestMapping("/getMenus")
    public Result<Object> getMenus(HttpServletRequest req) throws MyException {
        try {
            List<Map<String, Object>> menus = menuService.getMenuTreeByUserId(CommonUtils.getCurrentUser().getId());
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(CommonUtils.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return Result.success(menus);
        } catch (Exception e) {
            throw MyException.build(ResultCode.GET_CURRENT_USER_ERROR, "获取当前登录用户的菜单异常", e);
        }
    }

    /**
     * 获取首页数据
     */
    @ResponseBody
    @RequestMapping("/getMainPageData")
    public Result<Object> getMainPageData() throws MyException {
        try {
            JSONObject obj = new JSONObject();
            Integer totalEmployee = employeeService.getTotalNum(new Employee());
            obj.put("totalPay", "0.00");
            obj.put("totalEmployee", totalEmployee);
            obj.put("totalWorksite", "0");
            obj.put("totalWorkhour", "0.00");
            return Result.success(obj);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取首页数据异常", e);
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
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取薪资排行前五异常", e);
        }
    }

    /**
     * 获取首页占比信息
     */
    @ResponseBody
    @RequestMapping("/getProportionData")
    public Result<Object> getProportionData() throws MyException {
        try {
            JSONObject obj = new JSONObject();
            obj.put("salaryProportion", "0.00");
            obj.put("workhourProportion", "0.00");
            return Result.success(obj);
        } catch (Exception e) {
            throw MyException.build(ResultCode.SEARCH_ERROR, "获取首页占比信息异常", e);
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
