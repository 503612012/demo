package com.oven.demo.common.requestLog;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.framework.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 请求日志dao层
 *
 * @author Oven
 */
@Slf4j
@Repository
public class RequestLogDao {

    @Resource
    private CacheService cacheService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量保存
     */
    public int[] batchSave(List<RequestLog> list, String tableName) {
        String sql = "insert into " + tableName + " (request_time, request_url, request_method, request_ip, request_param, user_id) values (?, ?, ?, ?, ?, ?)";
        return this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                RequestLog item = list.get(i);
                ps.setString(1, item.getRequestTime());
                ps.setString(2, item.getRequestUrl());
                ps.setString(3, item.getRequestMethod());
                ps.setString(4, item.getRequestIp());
                ps.setString(5, item.getRequestParam());
                ps.setInt(6, item.getUserId());
                if (i % 1000 == 0) {
                    // 执行prepareStatement对象中所有的sql语句
                    ps.executeBatch();
                }
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /**
     * 判断表是否存在
     */
    public boolean isExist(String tableName) {
        // 先读缓存，看是否已经创建表
        Object result = cacheService.get(AppConst.APP_NAME + tableName);
        if (result != null) {
            return true;
        }

        // 如果不存在，需要在数据库中再查询是否存在
        String sql = "select 1 from " + tableName;
        try {
            this.jdbcTemplate.execute(sql);
            cacheService.set(AppConst.APP_NAME + tableName, new Object());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建表
     */
    public void createTable(String tableName) {
        // 先读缓存，看是否已经创建表
        Object result = cacheService.get(AppConst.APP_NAME + tableName);
        if (result != null) {
            return;
        }

        StringBuilder sql = new StringBuilder("create table ").append(tableName)
                .append(" as select * from ")
                .append(AppConst.REQUEST_LOG_TEMPLATE_TABLENAME)
                .append(" where 1=2 ");
        try {
            this.jdbcTemplate.execute(sql.toString());
            // 创建成功时，更新缓存
            cacheService.set(AppConst.APP_NAME + tableName, new Object());
        } catch (Exception e) {
            log.error("创建表时出现异常：\n", e);
        }
    }

}
