package com.oven.core.log.service;

import com.oven.constant.AppConst;
import com.oven.constant.RedisCacheKey;
import com.oven.core.base.service.BaseService;
import com.oven.core.log.dao.LogDao;
import com.oven.core.log.vo.Log;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
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
     * @param id 日志ID
     */
    public Log getById(Integer id) {
        Log log = super.get(MessageFormat.format(RedisCacheKey.LOG_GET_BY_ID, id)); // 先读取缓存
        if (log == null) { // double check
            synchronized (this) {
                log = super.get(MessageFormat.format(RedisCacheKey.LOG_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (log == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    log = logDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.LOG_GET_BY_ID, id), log);
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
        List<Log> list = super.get(MessageFormat.format(RedisCacheKey.LOG_GET_BY_PAGE, pageNum, pageSize, log.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.LOG_GET_BY_PAGE, pageNum, pageSize, log.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = logDao.getByPage(pageNum, pageSize, log);
                    super.set(MessageFormat.format(RedisCacheKey.LOG_GET_BY_PAGE, pageNum, pageSize, log.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取日志总数量
     */
    public Integer getTotalNum(Log log) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.LOG_GET_TOTAL_NUM, log.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.LOG_GET_TOTAL_NUM, log.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = logDao.getTotalNum(log);
                    super.set(MessageFormat.format(RedisCacheKey.LOG_GET_TOTAL_NUM, log.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 添加
     */
    public void add(Log log) {
        logDao.add(log);
        // 清理缓存
        super.batchRemove(RedisCacheKey.LOG_PREFIX);
    }

    /**
     * 添加日志
     */
    @Override
    public void addLog(String title, String content, Integer operatorId, String operatorName, String operatorIp) {
        Log log = new Log();
        log.setTitle(title);
        log.setContent(content);
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperatorIp(operatorIp);
        log.setOperatorTime(new DateTime().toString(AppConst.TIME_PATTERN));
        this.add(log);
    }

}
