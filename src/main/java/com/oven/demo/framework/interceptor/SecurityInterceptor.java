package com.oven.demo.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.ResultInfo;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.core.menu.service.MenuService;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.service.UserService;
import org.apache.commons.codec.CharEncoding;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 安全验证
 *
 * @author Oven
 */
@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private MenuService menuService;
    @Resource
    private UserService userService;

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String GET_GIF_CODE = "/getGifCode";
    private static final String DO_LOGIN = "/doLogin";
    private static final String VERSION = "/version";
    private static final String ERROR = "/error";
    private static final String LOGIN = "/login";
    private static final String ERR = "/err";
    private static final String INDEX = "/";

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, @SuppressWarnings("NullableProblems") Object handler) throws Exception {
        resp.setContentType("text/plain;charset=UTF-8");
        String servletPath = req.getServletPath();
        // 放行的请求
        if (isExcludedUrls(servletPath, resp)) {
            return true;
        }

        // 获取当前登录用户
        User user = (User) req.getSession().getAttribute(AppConst.CURRENT_USER);

        // 判断记住我功能
        if (user == null) {
            user = this.rememberMe(req);
        }

        // 没有登录状态下访问系统主页面，都跳转到登录页，不提示任何信息
        if (servletPath.startsWith(INDEX)) {
            if (user == null) {
                resp.sendRedirect(getDomain(req) + LOGIN);
                return false;
            }
        }

        // 未登录或会话超时
        if (user == null) {
            return responseRequest(ResultEnum.SESSION_TIMEOUT.getCode(), ResultEnum.SESSION_TIMEOUT.getValue(), ResultEnum.SESSION_TIMEOUT.getValue(), req, resp);
        }

        // 检查是否被其他人挤出去
        ServletContext application = req.getServletContext();
        @SuppressWarnings("unchecked")
        Map<String, JSONObject> loginedMap = (Map<String, JSONObject>) application.getAttribute(AppConst.LOGINEDUSERS);
        if (loginedMap == null) { // 可能是掉线了
            return responseRequest(ResultEnum.LOSE_LOGIN.getCode(), ResultEnum.LOSE_LOGIN.getValue(), ResultEnum.LOSE_LOGIN.getValue(), req, resp);
        }
        JSONObject obj = loginedMap.get(user.getUserName());
        String loginedUserSessionId = obj.getString(AppConst.SESSION_ID);
        if (!StringUtils.isEmpty(loginedUserSessionId) && ResultEnum.FORCE_LOGOUT.getValue().equals(loginedUserSessionId)) {
            return responseRequest(ResultEnum.FORCE_LOGOUT.getCode(), ResultEnum.FORCE_LOGOUT.getValue(), ResultEnum.FORCE_LOGOUT.getValue(), req, resp);
        }
        String mySessionId = req.getSession().getId();

        if (!mySessionId.equals(loginedUserSessionId)) {
            return responseRequest(ResultEnum.OTHER_LOGINED.getCode(), ResultEnum.OTHER_LOGINED.getValue(), ResultEnum.OTHER_LOGINED.getValue(), req, resp);
        }
        return true;
    }

    /**
     * 响应请求
     */
    public boolean responseRequest(Integer code, String data, String param, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String requestType = req.getHeader("X-Requested-With");
        if (XML_HTTP_REQUEST.equals(requestType)) { // ajax请求
            resp.getWriter().write(JSONObject.toJSONString(ResultInfo.build(code, data)));
            return false;
        }
        param = URLEncoder.encode(param, CharEncoding.UTF_8);
        resp.sendRedirect(getDomain(req) + "/login?errorMsg=" + param);
        return false;
    }

    /**
     * 记住我
     */
    private User rememberMe(HttpServletRequest req) {
        User user = null;
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            user = (User) subject.getPrincipal();
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
            loginedMap.put(user.getUserName(), obj);

            req.getSession().setAttribute(AppConst.CURRENT_USER, user);
            if (StringUtils.isEmpty(user.getConfig())) {
                user = userService.getById(user.getId());
            }
            if (StringUtils.isEmpty(user.getConfig())) {
                req.getSession().setAttribute("userTheme", "light");
                req.getSession().setAttribute("menuPosition", "left");
            } else {
                String userTheme = JSONObject.parseObject(user.getConfig()).getString("userTheme");
                req.getSession().setAttribute("userTheme", StringUtils.isEmpty(userTheme) ? "light" : userTheme);
                String menuPosition = JSONObject.parseObject(user.getConfig()).getString("menuPosition");
                req.getSession().setAttribute("menuPosition", StringUtils.isEmpty(menuPosition) ? "left" : menuPosition);
            }
        }

        // 获取该用户的所有权限编码，放入session中
        Object userMenus = req.getSession().getAttribute(AppConst.USER_MENU);
        if (userMenus == null && user != null) {
            userMenus = menuService.getAllMenuCodeByUserId(user.getId());
            req.getSession().setAttribute(AppConst.USER_MENU, userMenus);
        }
        return user;
    }

    /**
     * 放行的url
     */
    private boolean isExcludedUrls(String servletPath, HttpServletResponse resp) throws IOException {
        if (servletPath.startsWith(LOGIN) ||
                VERSION.equals(servletPath) ||
                servletPath.startsWith(DO_LOGIN) ||
                ERR.equals(servletPath) ||
                servletPath.startsWith(GET_GIF_CODE)) {
            return true;
        }
        if (servletPath.startsWith(ERROR)) {
            resp.sendRedirect(ERR);
            return true;
        }
        return false;
    }

    /**
     * 获得域名
     */
    private String getDomain(HttpServletRequest request) {
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    }

}
