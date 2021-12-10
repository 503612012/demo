package com.oven.demo.common.util;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.core.user.vo.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 常用工具类
 *
 * @author Oven
 */
public class CommonUtils {

    /**
     * 获取当前登录人信息
     */
    public static User getCurrentUser() {
        Object user = SecurityUtils.getSubject().getSession().getAttribute(AppConst.CURRENT_USER);
        return (User) user;
    }

    /**
     * 获取当前登录人IP地址
     */
    public static String getCurrentUserIp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return IPUtils.getClientIPAddr(servletRequestAttributes.getRequest());
    }

}
