package com.oven.demo.core.log.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.demo.core.log.entity.Log;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 日志dao层
 *
 * @author Oven
 */
@Repository
public class LogDao extends BaseDao<Log> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 批量添加
     */
    public void batchSave(List<Log> list) {
        String sql = "insert into t_log values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            @SuppressWarnings("NullableProblems")
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Log item = list.get(i);
                ps.setString(1, item.getTitle());
                ps.setString(2, item.getRequest());
                ps.setString(3, item.getResponse());
                ps.setString(4, item.getRequestUri());
                ps.setString(5, item.getRequestMethod());
                ps.setInt(6, item.getOperatorId());
                ps.setString(7, item.getOperatorName());
                ps.setString(8, item.getOperatorTime());
                ps.setString(9, item.getOperatorIp());
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

}
