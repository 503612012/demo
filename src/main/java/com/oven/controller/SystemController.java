package com.oven.controller;

import com.oven.contants.AppConst;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.MyException;
import com.oven.service.LogService;
import com.oven.service.MenuService;
import com.oven.service.UserService;
import com.oven.util.IPUtils;
import com.oven.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
    public Object doLogin(String userName, String pwd, HttpServletRequest req) throws MyException {
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
            logService.addLog("登录系统！", "成功！", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req));
            return super.success("登录成功！");
        } catch (Exception e) {
            User userInDb = userService.getByUserName(userName);
            if (e instanceof UnknownAccountException) {
                logService.addLog("登录系统！", "失败[" + ResultEnum.NO_THIS_USER.getValue() + "]", 0, "", IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.NO_THIS_USER.getCode(), ResultEnum.NO_THIS_USER.getValue());
            } else if (e instanceof IncorrectCredentialsException) {
                logService.addLog("登录系统！", "失败[" + ResultEnum.PASSWORD_WRONG.getValue() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.PASSWORD_WRONG.getCode(), ResultEnum.PASSWORD_WRONG.getValue());
            } else if (e instanceof LockedAccountException) {
                logService.addLog("登录系统！", "失败[" + ResultEnum.USER_DISABLE.getValue() + "]", userInDb.getId(), userInDb.getNickName(), IPUtils.getClientIPAddr(req));
                return super.fail(ResultEnum.USER_DISABLE.getCode(), ResultEnum.USER_DISABLE.getValue());
            } else {
                throw new MyException(ResultEnum.UNKNOW_ERROR.getCode(), "登录操作出错，请联系网站管理人员。", e);
            }
        }
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
            log.error(AppConst.ERROR_LOG_PREFIX + "登录操作出错，请联系网站管理人员。：", e);
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
    public Object getMenus(HttpServletRequest req) throws MyException {
        try {
            List<Map<String, Object>> menus = menuService.getMenuTreeByUserId(super.getCurrentUser().getId());
            // 获取该用户的所有权限编码，放入session中
            List<String> code = menuService.getAllMenuCodeByUserId(super.getCurrentUser().getId());
            req.getSession().setAttribute(AppConst.USER_MENU, code);
            return super.success(menus);
        } catch (Exception e) {
            throw new MyException(ResultEnum.UNKNOW_ERROR.getCode(), "获取当前登录用户出错，请联系网站管理人员。", e);
        }
    }

}
