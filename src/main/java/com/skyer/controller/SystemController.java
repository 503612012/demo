package com.skyer.controller;

import com.skyer.contants.AppConst;
import com.skyer.enumerate.ResultEnum;
import com.skyer.service.LogService;
import com.skyer.service.MenuService;
import com.skyer.service.UserService;
import com.skyer.util.IPUtils;
import com.skyer.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统控制器
 *
 * @author SKYER
 */
@Controller
public class SystemController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SystemController.class);

    @Resource
    private LogService logService;
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;

    /**
     * 默认页面(系统主页面)
     */
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 去到登录页面
     */
    @RequestMapping("/login")
    public String login(String errorMsg, Model model) {
        model.addAttribute("errorMsg", errorMsg);
        return "login";
    }

    /**
     * 登录操作
     *
     * @param userName 用户名
     * @param pwd      密码
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public Object doLogin(String userName, String pwd, HttpServletRequest req) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, pwd);
            subject.login(token);

            User userInDb = userService.getByUserName(userName);
            // 登录成功后放入application，防止同一个账户多人登录
            ServletContext application = req.getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, String> loginedMap = (Map<String, String>) application.getAttribute(AppConst.LOGINEDUSERS);
            if (loginedMap == null) {
                loginedMap = new HashMap<>();
                application.setAttribute(AppConst.LOGINEDUSERS, loginedMap);
            }
            loginedMap.put(userInDb.getUserName(), req.getSession().getId());

            // 登录成功后放入session中
            req.getSession().setAttribute(AppConst.CURRENT_USER, userInDb);
            logService.addLog("登录系统！", "成功！", userInDb.getId(), IPUtils.getClientIPAddr(req));
            return super.success("登录成功！");
        } catch (Exception e) {
            User userInDb = userService.getByUserName(userName);
            if (e instanceof UnknownAccountException) {
                logService.addLog("登录系统！", "失败[" + ResultEnum.NO_THIS_USER.getValue() + "]", 0, IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.NO_THIS_USER.getCode(), ResultEnum.NO_THIS_USER.getValue());
            } else if (e instanceof IncorrectCredentialsException) {
                logService.addLog("登录系统！", "失败[" + ResultEnum.PASSWORD_WRONG.getValue() + "]", userInDb.getId(), IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.PASSWORD_WRONG.getCode(), ResultEnum.PASSWORD_WRONG.getValue());
            } else if (e instanceof LockedAccountException) {
                logService.addLog("登录系统！", "失败[" + ResultEnum.USER_DISABLE.getValue() + "]", userInDb.getId(), IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.USER_DISABLE.getCode(), ResultEnum.USER_DISABLE.getValue());
            } else {
                LOG.error(AppConst.ERROR_LOG_PREFIX + "入参[userName: {}, pwd: {}]", userName, pwd);
                LOG.error(AppConst.ERROR_LOG_PREFIX + "登录操作，错误信息：", e);
                e.printStackTrace();
            }
        }
        return super.fail(ResultEnum.UNKNOW_ERROR.getCode(), ResultEnum.UNKNOW_ERROR.getValue());
    }

    /**
     * 登出操作
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest req) {
        try {
            req.getSession().removeAttribute(AppConst.CURRENT_USER);
            req.getSession().getServletContext().removeAttribute(AppConst.CURRENT_USER);
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "登录操作出错，错误信息：", e);
            e.printStackTrace();
        }
        return "login";
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
     * 获取当前登录用户的菜单
     */
    @RequestMapping("/getMenus")
    @ResponseBody
    public Object getMenus(HttpServletRequest req) {
        try {
            List<Map<String, Object>> menus = menuService.getMenuTreeByUserId(super.getCurrentUser().getId());
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(super.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return super.success(menus);
        } catch (Exception e) {
            LOG.error(AppConst.ERROR_LOG_PREFIX + "获取菜单出错，错误信息：" + e);
            e.printStackTrace();
        }
        return super.fail(ResultEnum.UNKNOW_ERROR.getCode(), ResultEnum.UNKNOW_ERROR.getValue());
    }

}
