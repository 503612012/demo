package com.oven.demo.core.log.service;

import com.oven.basic.common.util.DateUtils;
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
public class LogService {

    @Resource
    private LogDao logDao;

    /**
     * 通过id获取
     *
     * @param id 日志id
     */
    public Log getById(Integer id) {
        return logDao.getById(id);
    }

    /**
     * 分页获取日志
     */
    public List<Log> getByPage(Log log) {
        return logDao.getByPage(log);
    }

    /**
     * 获取日志总数量
     */
    public Integer getTotalNum(Log log) {
        return logDao.getTotalNum(log);
    }

    /**
     * 批量添加
     */
    public void batchSave(List<Log> list) throws Exception {
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
