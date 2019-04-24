package com.oven.service;

import com.oven.cache.CacheService;
import com.oven.constant.AppConst;
import com.oven.util.IPUtils;
import com.oven.vo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 基类服务
 *
 * @author Oven
 */
@Service
public class BaseService {

    @Resource
    private com.oven.service.LogService logService;
    @Resource
    private CacheService cacheService;

    /**
     * 获取当前登录人信息
     */
    @SuppressWarnings("all")
    public User getCurrentUser() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (User) req.getSession().getAttribute(AppConst.CURRENT_USER);
    }

    /**
     * 获取当前登录人IP地址
     */
    @SuppressWarnings("all")
    public String getCurrentUserIp() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return IPUtils.getClientIPAddr(req);
    }

    /**
     * 添加日志
     */
    public void addLog(String title, String content, Integer operatorId, String operatorName, String operatorIp) {
        logService.addLog(title, content, operatorId, operatorName, operatorIp);
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
    public void batchRemove(String key) {
        cacheService.batchRemove(key);
    }

}
