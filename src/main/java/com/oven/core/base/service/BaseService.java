package com.oven.core.base.service;

import com.oven.common.util.CommonUtils;
import com.oven.core.log.service.LogService;
import com.oven.core.user.vo.User;
import com.oven.framework.cache.CacheService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
     * 添加日志
     */
    public void addLog(String title, String content) {
        User user = CommonUtils.getCurrentUser();
        if (user != null) {
            logService.addLog(title, content, user.getId(), user.getNickName(), CommonUtils.getCurrentUserIp());
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
