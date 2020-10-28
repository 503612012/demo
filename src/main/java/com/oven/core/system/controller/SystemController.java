package com.oven.core.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.common.constant.AppConst;
import com.oven.common.constant.PermissionCode;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.EncryptUtils;
import com.oven.common.util.IPUtils;
import com.oven.common.vcode.Captcha;
import com.oven.common.vcode.GifCaptcha;
import com.oven.core.base.controller.BaseController;
import com.oven.core.employee.service.EmployeeService;
import com.oven.core.employee.vo.Employee;
import com.oven.core.log.service.LogService;
import com.oven.core.menu.service.MenuService;
import com.oven.core.payRecord.service.PayRecordService;
import com.oven.core.system.service.SysDicService;
import com.oven.core.user.service.UserService;
import com.oven.core.user.vo.User;
import com.oven.core.workhour.service.WorkhourService;
import com.oven.core.worksite.service.WorksiteService;
import com.oven.core.worksite.vo.Worksite;
import com.oven.framework.exception.MyException;
import com.oven.framework.limitation.Limit;
import com.oven.framework.limitation.LimitType;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
public class SystemController extends BaseController {

    @Resource
    private LogService logService;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;
    @Resource
    private SysDicService sysDicService;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private WorksiteService worksiteService;
    @Resource
    private WorkhourService workhourService;
    @Resource
    private PayRecordService payRecordService;

    /**
     * 秒杀模拟
     */
    @ResponseBody
    @RequestMapping("/secKill")
    public Object secKill() {
        try {
            sysDicService.secKill();
            return super.success("秒杀成功！");
        } catch (Exception e) {
            return super.fail(400, "秒杀接口异常！");
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), "获取验证码异常！", "获取验证码异常", e);
        }
    }

    /**
     * 欢迎页面
     */
    @RequestMapping("/main.html")
    public String main() {
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
            User user = super.getCurrentUser();
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap != null) {
                loginedMap.remove(user.getUserName());
            }
            req.setAttribute("key", EncryptUtils.KEY);
            req.getSession().removeAttribute(AppConst.CURRENT_USER);
            req.getSession().getServletContext().removeAttribute(AppConst.CURRENT_USER);
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            log.error(AppConst.ERROR_LOG_PREFIX + "登录操作出错，请联系网站管理人员。：", e);
        }
        return "login";
    }

    /**
     * 强制退出
     */
    @ResponseBody
    @RequestMapping("/forceLogout")
    @RequiresPermissions(PermissionCode.FORCE_LOGOUT)
    public Object forceLogout(String userName, HttpServletRequest req) throws MyException {
        try {
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap != null) {
                JSONObject obj = new JSONObject();
                obj.put(AppConst.SESSION_ID, ResultEnum.FORCE_LOGOUT.getValue());
                obj.put(AppConst.SESSION, null);
                loginedMap.put(userName, obj);
            }
            return super.success("退出成功");
        } catch (Exception e) {
            throw new MyException(ResultEnum.FORCE_LOGOUT_ERROR.getCode(), ResultEnum.FORCE_LOGOUT_ERROR.getValue(), "强制退出异常", e);
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
    public Object doLogin(String userName, String pwd, String inputCode, boolean rememberMe, HttpServletRequest req) throws MyException {
        try {
            // 校验验证码
            String code = (String) req.getSession().getAttribute(AppConst.CAPTCHA);
            if (StringUtils.isEmpty(inputCode)) {
                return super.fail(ResultEnum.CAPTCHA_IS_NONE.getCode(), ResultEnum.CAPTCHA_IS_NONE.getValue());
            } else {
                if (!inputCode.toLowerCase().equals(code.toLowerCase())) {
                    return super.fail(ResultEnum.CAPTCHA_ERROR.getCode(), ResultEnum.CAPTCHA_ERROR.getValue());
                }
            }

            Subject subject = SecurityUtils.getSubject();
            pwd = EncryptUtils.aesDecrypt(pwd, EncryptUtils.KEY);
            UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd, rememberMe);
            subject.login(token);

            User userInDb = userService.getByUserName(userName);
            if (userInDb.getErrNum() >= 5) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.OVEN_WRONG_NUM.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.OVEN_WRONG_NUM.getCode(), ResultEnum.OVEN_WRONG_NUM.getValue());
            }
            // 登录成功后放入application，防止同一个账户多人登录
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
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
            logService.addLog("登录系统！", "成功！", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req));
            userService.updateLastLoginTime(new DateTime().toString(AppConst.TIME_PATTERN), userInDb.getId());
            return super.success("登录成功！");
        } catch (Exception e) {
            User userInDb = userService.getByUserName(userName);
            if (e instanceof UnknownAccountException) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.NO_THIS_USER.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.NO_THIS_USER.getCode(), ResultEnum.NO_THIS_USER.getValue());
            } else if (e instanceof IncorrectCredentialsException) {
                if (userInDb.getErrNum() >= 5) {
                    logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.OVEN_WRONG_NUM.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req));
                    return super.fail(ResultEnum.OVEN_WRONG_NUM.getCode(), ResultEnum.OVEN_WRONG_NUM.getValue());
                }
                userService.logPasswordWrong(userInDb.getId());
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.PASSWORD_WRONG.getValue() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.PASSWORD_WRONG.getCode(), ResultEnum.PASSWORD_WRONG.getValue());
            } else if (e instanceof LockedAccountException) {
                logService.addLog("登录系统！", "失败[用户名：" + userName + "，失败原因：" + ResultEnum.USER_DISABLE.getValue() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.USER_DISABLE.getCode(), ResultEnum.USER_DISABLE.getValue());
            } else {
                throw new MyException(ResultEnum.UNKNOW_ERROR.getCode(), "登录操作出错，请联系网站管理人员。", "登录操作异常", e);
            }
        }
    }

    /**
     * 获取当前登录用户的菜单
     */
    @ResponseBody
    @RequestMapping("/getMenus")
    public Object getMenus(HttpServletRequest req) throws MyException {
        try {
            List<Map<String, Object>> menus = menuService.getMenuTreeByUserId(super.getCurrentUser().getId());
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(super.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return super.success(menus);
        } catch (Exception e) {
            throw new MyException(ResultEnum.UNKNOW_ERROR.getCode(), "获取当前登录用户出错，请联系网站管理人员。", "获取当前登录用户的菜单异常", e);
        }
    }

    /**
     * 获取首页数据
     */
    @ResponseBody
    @RequestMapping("/getMainPageData")
    public Object getMainPageData() throws MyException {
        try {
            JSONObject obj = new JSONObject();
            Double totalPay = payRecordService.getTotalPay();
            Integer totalEmployee = employeeService.getTotalNum(new Employee());
            Integer totalWorksite = worksiteService.getTotalNum(new Worksite());
            Double totalWorkhour = workhourService.getTotalWorkhour();
            obj.put("totalPay", totalPay);
            obj.put("totalEmployee", totalEmployee);
            obj.put("totalWorksite", totalWorksite);
            obj.put("totalWorkhour", totalWorkhour);
            return super.success(obj);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取首页数据异常", e);
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
            List<Map<String, Object>> list = payRecordService.getSalaryTopFive();
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
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取薪资排行前五异常", e);
        }
    }

    /**
     * 获取首页占比信息
     */
    @ResponseBody
    @RequestMapping("/getProportionData")
    public Object getProportionData() throws MyException {
        try {
            JSONObject obj = new JSONObject();
            Double salaryProportion = payRecordService.getSalaryProportion();
            Double workhourProportion = workhourService.getWorkhourProportion();
            obj.put("salaryProportion", salaryProportion);
            obj.put("workhourProportion", workhourProportion);
            return super.success(obj);
        } catch (Exception e) {
            throw new MyException(ResultEnum.SEARCH_ERROR.getCode(), ResultEnum.SEARCH_ERROR.getValue(), "获取首页占比信息异常", e);
        }
    }

    /**
     * 测试接口限流
     */
    @ResponseBody
    @RequestMapping("/limit")
    @Limit(key = "limit", period = AppConst.LIMIT_TIME, count = 1, errMsg = AppConst.SYSTEM_LIMIT, limitType = LimitType.IP_AND_METHOD)
    public Object limit() {
        return UUID.randomUUID().toString();
    }

}
