package com.oven.core.base.service;

import com.oven.common.constant.AppConst;
import com.oven.common.util.IPUtils;
import com.oven.core.log.service.LogService;
import com.oven.core.user.vo.User;
import com.oven.framework.cache.CacheService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 基类服务
 *
 * @author Oven
 */
@Service
public class BaseService {

    @Resource
    private LogService logService;
    @Resource
    private CacheService cacheService;

    /**
     * 获取当前登录人信息
     */
    @SuppressWarnings("all")
    public User getCurrentUser() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest req = servletRequestAttributes.getRequest();
        if (req == null) {
            return null;
        }
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        Object attribute = session.getAttribute(AppConst.CURRENT_USER);
        if (attribute == null) {
            return null;
        }
        return (User) attribute;
    }

    /**
     * 获取当前登录人IP地址
     */
    @SuppressWarnings("all")
    public String getCurrentUserIp() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest req = servletRequestAttributes.getRequest();
        if (req == null) {
            return null;
        }
        return IPUtils.getClientIPAddr(req);
    }

    /**
     * 添加日志
     */
    public void addLog(String title, String content) {
        User user = this.getCurrentUser();
        if (user != null) {
            logService.addLog(title, content, user.getId(), user.getNickName(), this.getCurrentUserIp());
        } else {
            logService.addLog(title, content, -1, "系统", "127.0.0.1");
        }
    }

    /**
     * 读缓存
     */
    public <T> T get(String key) {
        return cacheService.get(key);
    }

    /**
     * 写缓存
     */
    public <T> void set(String key, T obj) {
        cacheService.set(key, obj);
    }

    /**
     * 批量移除缓存
     */
    public void batchRemove(String... key) {
        cacheService.batchRemove(key);
    }

}
