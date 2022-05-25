package com.oven.demo.core.log.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.log.vo.Log;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 日志dao层
 *
 * @author Oven
 */
@Repository
public class LogDao {

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
        StringBuilder sb = new StringBuilder("select * from t_log");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, log);
        sb.append(" order by operator_time desc");
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(Log.class));
    }

    /**
     * 获取日志总数量
     */
    public Integer getTotalNum(Log log) {
        StringBuilder sb = new StringBuilder("select count(*) from t_log");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, log);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, Log log) {
        if (!StringUtils.isEmpty(log.getTitle())) {
            sb.append(" and title like ?");
            params.add("%" + log.getTitle().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(log.getContent())) {
            sb.append(" and content like ?");
            params.add("%" + log.getContent().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (log.getOperatorId() != null) {
            sb.append(" and operator_id = ?");
            params.add(log.getOperatorId());
        }
    }

    /**
     * 批量添加
     */
    public void batchSave(List<Log> list) {
        String sql = "insert into t_log values (null, ?, ?, ?, ?, ?, ?)";
        this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Log item = list.get(i);
                ps.setString(1, item.getTitle());
                ps.setString(2, item.getContent());
                ps.setInt(3, item.getOperatorId());
                ps.setString(4, item.getOperatorName());
                ps.setString(5, item.getOperatorTime());
                ps.setString(6, item.getOperatorIp());
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
