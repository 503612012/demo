package com.oven.demo.core.base.service;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.common.util.LogQueueUtils;
import com.oven.demo.core.log.vo.Log;
import com.oven.demo.core.user.vo.User;
import com.oven.demo.framework.cache.CacheService;
import org.joda.time.DateTime;
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
    private CacheService cacheService;

    /**
     * 添加日志
     */
    public void addLog(String title, String content) {
        User user = CommonUtils.getCurrentUser();
        Log log = new Log();
        log.setTitle(title);
        log.setContent(content);
        log.setOperatorTime(DateTime.now().toString(AppConst.TIME_PATTERN));
        if (user != null) {
            log.setOperatorId(user.getId());
            log.setOperatorName(user.getNickName());
            log.setOperatorIp(CommonUtils.getCurrentUserIp());
        } else {
            log.setOperatorId(-1);
            log.setOperatorName("系统");
            log.setOperatorIp("127.0.0.1");
        }
        LogQueueUtils.getInstance().offer(log);
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
