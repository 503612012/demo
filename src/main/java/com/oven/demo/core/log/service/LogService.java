package com.oven.demo.core.log.service;

import cn.hutool.core.util.StrUtil;
import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.service.BaseService;
import com.oven.demo.common.util.LogQueueUtils;
import com.oven.demo.core.log.dao.LogDao;
import com.oven.demo.core.log.entity.Log;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 日志服务层
 *
 * @author Oven
 */
@Service
public class LogService extends BaseService {

    @Resource
    private LogDao logDao;

    /**
     * 通过id获取
     *
     * @param id 日志id
     */
    public Log getById(Integer id) {
        Log log = super.get(StrUtil.format(RedisCacheKey.LOG_GET_BY_ID, id)); // 先读取缓存
        if (log == null) { // double check
            synchronized (this) {
                log = super.get(StrUtil.format(RedisCacheKey.LOG_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (log == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    log = logDao.getById(id);
                    super.set(StrUtil.format(RedisCacheKey.LOG_GET_BY_ID, id), log);
                }
            }
        }
        return log;
    }

    /**
     * 分页获取日志
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     */
    public List<Log> getByPage(Integer pageNum, Integer pageSize, Log log) {
        List<Log> list = super.get(StrUtil.format(RedisCacheKey.LOG_GET_BY_PAGE, pageNum, pageSize, log.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(StrUtil.format(RedisCacheKey.LOG_GET_BY_PAGE, pageNum, pageSize, log.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = logDao.getByPage(pageNum, pageSize, log);
                    super.set(StrUtil.format(RedisCacheKey.LOG_GET_BY_PAGE, pageNum, pageSize, log.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取日志总数量
     */
    public Integer getTotalNum(Log log) {
        Integer totalNum = super.get(StrUtil.format(RedisCacheKey.LOG_GET_TOTAL_NUM, log.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(StrUtil.format(RedisCacheKey.LOG_GET_TOTAL_NUM, log.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = logDao.getTotalNum(log);
                    super.set(StrUtil.format(RedisCacheKey.LOG_GET_TOTAL_NUM, log.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 批量添加
     */
    public void batchSave(List<Log> list) {
        logDao.batchSave(list);
    }

    /**
     * 添加日志
     */
    public void addLog(String title, String content, Integer operatorId, String operatorName, String operatorIp, String requestUri, String requestMethod) {
        Log log = Log.builder()
                .title(title)
                .request(content)
                .response("")
                .requestUri(requestUri)
                .requestMethod(requestMethod)
                .operatorId(operatorId)
                .operatorName(operatorName)
                .operatorTime(DateUtils.getCurrentTime())
                .operatorIp(operatorIp)
                .build();
        LogQueueUtils.getInstance().offer(log);
    }

}
