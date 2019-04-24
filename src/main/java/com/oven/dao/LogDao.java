package com.oven.dao;

import com.oven.vo.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        List<Log> list = this.jdbcTemplate.query(sql, (rs, rowNum) -> getLog(rs), id);
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页获取日志
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Log> getByPage(Integer pageNum, Integer pageSize, Log log) {
        StringBuilder sb = new StringBuilder("select * from t_log");
        addCondition(sb, log);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> getLog(rs), (pageNum - 1) * pageSize, pageSize);
    }

    /**
     * 获取日志总数量
     */
    public Integer getTotalNum(Log log) {
        StringBuilder sb = new StringBuilder("select count(*) from t_log");
        addCondition(sb, log);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 添加
     */
    public int add(Log log) {
        String sql = "insert into t_log values (null, ?, ?, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, log.getTitle());
            ps.setString(2, log.getContent());
            ps.setInt(3, log.getOperatorId());
            ps.setString(4, log.getOperatorName());
            ps.setString(5, log.getOperatorTime());
            ps.setString(6, log.getOperatorIp());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, Log log) {
        if (!StringUtils.isEmpty(log.getTitle())) {
            sb.append(" and title like '%").append(log.getTitle()).append("%'");
        }
        if (!StringUtils.isEmpty(log.getContent())) {
            sb.append(" and content like '%").append(log.getContent()).append("%'");
        }
        if (log.getOperatorId() != null) {
            sb.append(" and operator_id = ").append(log.getOperatorId());
        }
    }

    /**
     * 关系映射
     */
    private Log getLog(ResultSet rs) throws SQLException {
        Log log = new Log();
        log.setId(rs.getInt("dbid"));
        log.setTitle(rs.getString("title"));
        log.setContent(rs.getString("content"));
        log.setOperatorId(rs.getInt("operator_id"));
        log.setOperatorName(rs.getString("operator_name"));
        log.setOperatorTime(rs.getString("operator_time"));
        log.setOperatorIp(rs.getString("operator_ip"));
        return log;
    }

}
