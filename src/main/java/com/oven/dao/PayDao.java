package com.oven.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.vo.Workhour;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 薪资发放dao层
 *
 * @author Oven
 */
@Repository
public class PayDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取员工未发放的薪资的工时
     */
    public List<Workhour> getWorkhourData(Integer employeeId, Integer worksiteId) {
        String sql = "select wh.*, e.name as employee_name, ws.name as worksite_name, u.nick_name as create_name from t_workhour wh " +
                "         left join t_employee e on wh.employee_id = e.dbid " +
                "         left join t_worksite ws on ws.dbid = wh.worksite_id " +
                "         left join t_user u on u.dbid = wh.create_id" +
                "         where wh.employee_id=? and wh.worksite_id=? and wh.has_pay = 0" +
                "         order by wh.work_date desc";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Workhour.class), employeeId, worksiteId);
    }

    /**
     * 取字符串连接的实现
     */
    private String getConcat(String[] columns) {
        StringBuilder result = new StringBuilder();
        result.append("(");
        for (int i = 0; i < columns.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(columns[i]);
        }
        result.append(")");
        return result.toString();
    }


    /**
     * 下发薪资
     */
    public void doPay(String workhourIds) {
        String sql = "update t_workhour set has_pay = 1 where dbid in " + this.getConcat(workhourIds.split(","));
        this.jdbcTemplate.update(sql);
    }

}
