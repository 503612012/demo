package com.skyer.service;

import com.skyer.contants.AppConst;
import com.skyer.util.IPUtils;
import com.skyer.vo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 基类服务
 *
 * @author SKYER
 */
@Service
public class BaseService {

    @Resource
    private LogService logService;

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
    public void addLog(String title, String content, Integer operatorId, String operatorIp) {
        logService.addLog(title, content, operatorId, operatorIp);
    }

}
