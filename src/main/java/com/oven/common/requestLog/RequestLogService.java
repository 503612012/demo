package com.oven.common.requestLog;

import com.oven.core.base.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 请求日志服务层
 *
 * @author Oven
 */
@Service
public class RequestLogService extends BaseService {

    @Resource
    private RequestLogDao requestLogDao;

    /**
     * 批量保存
     */
    public int[] batchSave(List<RequestLog> list, String tableName) {
        return requestLogDao.batchSave(list, tableName);
    }

    /**
     * 判断表是否存在
     */
    public boolean isExist(String tableName) {
        return requestLogDao.isExist(tableName);
    }

    /**
     * 创建表
     */
    public void createTable(String tableName) {
        requestLogDao.createTable(tableName);
    }

}
