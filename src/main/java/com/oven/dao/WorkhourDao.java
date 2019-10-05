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
                "         left join t_user u on u.dbid = wh.create_id");
        addCondition(sb, workhour);
        String sql = sb.append(" order by wh.create_time desc").append(" limit ?,?").toString().replaceFirst("and", "where");
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), (pageNum - 1) * pageSize, pageSize);
    }

    /**
     * 统计工时总数量
     */
    public Integer getTotalNum(Workhour workhour) {
        StringBuilder sb = new StringBuilder("select count(*) from t_workhour wh " +
                "         left join t_employee e on wh.employee_id = e.dbid " +
                "         left join t_worksite ws on ws.dbid = wh.worksite_id " +
                "         left join t_user u on u.dbid = wh.create_id");
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
        String sql = "insert into t_workhour (dbid, employee_id, worksite_id, work_date, work_hour, hour_salary, create_id, create_time, has_pay, remark) values (null, ?, ?, ?, ?, ?, ?, ?, 0, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, workhour.getEmployeeId());
            ps.setInt(2, workhour.getWorksiteId());
            ps.setString(3, workhour.getWorkDate());
            ps.setInt(4, workhour.getWorkhour());
            ps.setDouble(5, workhour.getHourSalary());
            ps.setInt(6, workhour.getCreateId());
            ps.setString(7, workhour.getCreateTime());
            ps.setString(8, workhour.getRemark());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 判断该员工该日期是否有录入过
     */
    public Workhour isInputed(Integer employeeId, String workDate, Integer worksiteId) {
        String sql = "select * from t_workhour where employee_id = ? and work_date = ? and worksite_id = ?";
        List<Workhour> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), employeeId, workDate, worksiteId);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 获取总工时
     */
    public Double getTotalWorkhour() {
        String sql = "select sum(work_hour) from t_workhour";
        return this.jdbcTemplate.queryForObject(sql, Double.class);
    }

    /**
     * 获取已发薪资工时占比
     */
    public List<String> getWorkhourProportion() {
        String sql = "select sum(work_hour) from t_workhour where has_pay=1 " +
                "union  " +
                "select sum(work_hour) from t_workhour";
        return this.jdbcTemplate.queryForList(sql, String.class);
    }

    /**
     * 获取一个员工未发薪资的所有工时
     */
    public List<Workhour> getUnPayByEmployeeId(Integer employeeId) {
        String sql = "select * from t_workhour where employee_id = ? and has_pay = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), employeeId);
    }

    /**
     * 获取薪资统计图表X轴数据
     */
    public List<String> getCategories(String date, Integer dateType) {
        String sql;
        if (dateType == 1) {
            sql = "select * " +
                    "from (select distinct substr(work_date, 1, 7) as date " +
                    "      from t_workhour " +
                    "      where substr(work_date, 1, 4) = ? " +
                    "      union " +
                    "      select distinct substr(advance_time, 1, 7) as date " +
                    "      from t_advance_salary " +
                    "      where substr(advance_time, 1, 4) = ?) a " +
                    "order by date";
        } else if (dateType == 2) {
            sql = "select * " +
                    "from (select distinct substr(work_date, 1, 10) as date " +
                    "      from t_workhour " +
                    "      where substr(work_date, 1, 7) = ? " +
                    "      union " +
                    "      select distinct substr(advance_time, 1, 10) as date " +
                    "      from t_advance_salary " +
                    "      where substr(advance_time, 1, 7) = ?) a " +
                    "order by date";
        } else {
            return null;
        }
        return this.jdbcTemplate.queryForList(sql, String.class, date, date);
    }

    /**
     * 获取薪资信息
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月，3传入的是年月日
     * @param dataType 获取数据类型，in获取录入薪资，out获取发放薪资
     */
    public Double getSalaryByDateAndDateType(String date, Integer dateType, String dataType) {
        String sql;
        if (dateType == 1) { // 传入的年
            if ("in".equals(dataType)) { // 录入
                sql = "select sum(work_hour * hour_salary) as money from t_workhour where substr(work_date, 1, 4) = ?";
            } else if ("out".equals(dataType)) { // 发放
                sql = "select sum(work_hour * hour_salary) as money from t_workhour where substr(work_date, 1, 4) = ? and has_pay = 1";
            } else {
                return null;
            }
        } else if (dateType == 2) { // 传入的是年月
            if ("in".equals(dataType)) { // 录入
                sql = "select sum(work_hour * hour_salary) as money from t_workhour where substr(work_date, 1, 7) = ?";
            } else if ("out".equals(dataType)) { // 发放
                sql = "select sum(work_hour * hour_salary) as money from t_workhour where substr(work_date, 1, 7) = ? and has_pay = 1";
            } else {
                return null;
            }
        } else if (dateType == 3) { // 传入的是年月日
            if ("in".equals(dataType)) { // 录入
                sql = "select sum(work_hour * hour_salary) as money from t_workhour where substr(work_date, 1, 10) = ?";
            } else if ("out".equals(dataType)) { // 发放
                sql = "select sum(work_hour * hour_salary) as money from t_workhour where substr(work_date, 1, 10) = ? and has_pay = 1";
            } else {
                return null;
            }
        } else {
            return null;
        }
        return this.jdbcTemplate.queryForObject(sql, Double.class, date);
    }

    /**
     * 获取薪资统计图表X轴数据-工时报表
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月
     */
    public List<String> getCategoriesForWorkhour(String date, Integer dateType, Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        if (dateType == 1) {
            sql.append("select distinct substr(work_date, 1, 7) as date from t_workhour where substr(work_date, 1, 4) = ?");
            if (employeeId != null) {
                sql.append(" and employee_id = ").append(employeeId);
            }
            sql.append(" order by date");
        } else if (dateType == 2) {
            sql.append("select distinct substr(work_date, 1, 10) as date from t_workhour where substr(work_date, 1, 7) = ?");
            if (employeeId != null) {
                sql.append(" and employee_id = ").append(employeeId);
            }
            sql.append(" order by date");
        } else {
            return null;
        }
        return this.jdbcTemplate.queryForList(sql.toString(), String.class, date);
    }

    /**
     * 获取薪资信息
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年月，2传入的是年月日
     */
    public Double getWorkhourByDateAndDateType(String date, Integer dateType, Integer employeeId) {
        StringBuilder sql = new StringBuilder();
        if (dateType == 1) { // 传入的年月
            sql.append("select sum(work_hour) as totalWorkhour from t_workhour where substr(work_date, 1, 7) = ?");
        } else if (dateType == 2) { // 传入的是年月日
            sql.append("select sum(work_hour) as totalWorkhour from t_workhour where substr(work_date, 1, 10) = ?");
        } else {
            return null;
        }
        if (employeeId != null) {
            sql.append(" and employee_id = ").append(employeeId);
        }
        return this.jdbcTemplate.queryForObject(sql.toString(), Double.class, date);
    }

    /**
     * 通过工地获取
     */
    public List<Workhour> getByWorksiteId(Integer worksiteId) {
        String sql = "select * from t_workhour where worksite_id = ? and has_pay = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), worksiteId);
    }

}
