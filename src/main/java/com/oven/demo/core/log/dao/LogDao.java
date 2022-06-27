package com.oven.demo.core.log.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.log.vo.Log;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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
     * 通过id获取
     *
     * @param id 日志ID
     */
    public Log getById(Integer id) {
        String sql = "select * from t_log where dbid = ?";
        List<Log> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Log.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页获取日志
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Log> getByPage(Integer pageNum, Integer pageSize, Log log) {
        StringBuilder sql = new StringBuilder("select * from t_log");
        List<Object> params = addCondition(sql, log);
        return super.getByPage(sql, params, Log.class, pageNum, pageSize, jdbcTemplate, "operator_time desc");
    }

    /**
     * 获取日志总数量
     */
    public Integer getTotalNum(Log log) {
        StringBuilder sql = new StringBuilder("select count(*) from t_log");
        List<Object> params = addCondition(sql, log);
        return super.getTotalNum(sql, params, jdbcTemplate);
    }

    /**
     * 搜索条件
     */
    private List<Object> addCondition(StringBuilder sql, Log log) {
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(log.getTitle())) {
            sql.append(" and title like ?");
            params.add("%" + log.getTitle().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (log.getOperatorId() != null) {
            sql.append(" and operator_id = ?");
            params.add(log.getOperatorId());
        }
        return params;
    }

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
