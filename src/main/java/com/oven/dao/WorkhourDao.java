package com.oven.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.vo.Workhour;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

/**
 * WorkhourDao
 *
 * @author Oven
 */
@Repository
public class WorkhourDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 分页查询工时
     */
    public List<Workhour> getByPage(Integer pageNum, Integer pageSize, Workhour workhour) {
        StringBuilder sb = new StringBuilder("select wh.*, e.name as employee_name, ws.name as worksite_name, u.nick_name as create_name from t_workhour wh " +
                "         left join t_employee e on wh.employee_id = e.dbid " +
                "         left join t_worksite ws on ws.dbid = wh.worksite_id " +
                "            left join t_user u on u.dbid = wh.create_id");
        addCondition(sb, workhour);
        String sql = sb.append(" order by wh.work_date desc").append(" limit ?,?").toString().replaceFirst("and", "where");
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), (pageNum - 1) * pageSize, pageSize);
    }

    /**
     * 统计工时总数量
     */
    public Integer getTotalNum(Workhour workhour) {
        StringBuilder sb = new StringBuilder("select count(*) from t_workhour wh " +
                "         left join t_employee e on wh.employee_id = e.dbid " +
                "         left join t_worksite ws on ws.dbid = wh.worksite_id " +
                "            left join t_user u on u.dbid = wh.create_id");
        addCondition(sb, workhour);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, Workhour workhour) {
        if (!StringUtils.isEmpty(workhour.getEmployeeName())) {
            sb.append(" and e.`name` like '%").append(workhour.getEmployeeName()).append("%'");
        }
        if (!StringUtils.isEmpty(workhour.getWorksiteName())) {
            sb.append(" and ws.`name` like '%").append(workhour.getWorksiteName()).append("%'");
        }
        if (!StringUtils.isEmpty(workhour.getWorkDate())) {
            sb.append(" and wh.`work_date` = '").append(workhour.getWorkDate()).append("'");
        }
    }

    /**
     * 通过主键查询
     */
    public Workhour getById(Integer id) {
        String sql = "select * from t_workhour where dbid = ?";
        List<Workhour> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_workhour where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 添加
     */
    public int add(Workhour workhour) {
        String sql = "insert into t_workhour (dbid, employee_id, worksite_id, work_date, work_hour, hour_salary, create_id, has_pay) values (null, ?, ?, ?, ?, ?, ?, 0)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, workhour.getEmployeeId());
            ps.setInt(2, workhour.getWorksiteId());
            ps.setString(3, workhour.getWorkDate());
            ps.setInt(4, workhour.getWorkhour());
            ps.setDouble(5, workhour.getHourSalary());
            ps.setInt(6, workhour.getCreateId());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 判断该员工该日期是否有录入过
     */
    public Workhour isInputed(Integer employeeId, String workDate) {
        String sql = "select * from t_workhour where employee_id = ? and work_date = ?";
        List<Workhour> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), employeeId, workDate);
        return list.size() == 0 ? null : list.get(0);
    }

}
