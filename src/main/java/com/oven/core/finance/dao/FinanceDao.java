package com.oven.core.finance.dao;

import com.oven.common.constant.AppConst;
import com.oven.common.util.VoPropertyRowMapper;
import com.oven.core.finance.vo.Finance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 财务管理dao层
 *
 * @author Oven
 */
@Repository
public class FinanceDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加
     */
    public int add(Finance finance) {
        String sql = "insert into t_finance (dbid, worksite_id, create_id, create_time, last_modify_id, last_modify_time, money, out_money, del_flag, finish_flag, remark)" +
                " values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, finance.getWorksiteId());
            ps.setInt(2, finance.getCreateId());
            ps.setString(3, finance.getCreateTime());
            ps.setInt(4, finance.getLastModifyId());
            ps.setString(5, finance.getLastModifyTime());
            ps.setDouble(6, finance.getMoney());
            ps.setDouble(7, finance.getOutMoney());
            ps.setInt(8, finance.getDelFlag());
            ps.setInt(9, finance.getFinishFlag());
            ps.setString(10, finance.getRemark());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 分页查询财务管理
     */
    public List<Finance> getByPage(Integer pageNum, Integer pageSize, Finance finance) {
        StringBuilder sb = new StringBuilder("select w.name       as worksite_name, " +
                "       cu.nick_name as create_name, " +
                "       mu.nick_name as last_modify_name, " +
                "       du.nick_name as del_name, " +
                "       fu.nick_name as finish_name, " +
                "       f.* " +
                "from t_finance f " +
                "       left join t_worksite w on w.dbid = f.worksite_id " +
                "       left join t_user cu on cu.dbid = f.create_id " +
                "       left join t_user mu on mu.dbid = f.last_modify_id " +
                "       left join t_user du on du.dbid = f.del_id " +
                "       left join t_user fu on fu.dbid = f.finish_id");
        sb.append(" where f.del_flag=0");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, finance);
        String sql = sb.append(" order by f.create_time desc limit ?,?").toString();
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(Finance.class));
    }

    /**
     * 统计财务管理总数量
     */
    public Integer getTotalNum(Finance finance) {
        StringBuilder sb = new StringBuilder("select count(*) " +
                "from t_finance f " +
                "       left join t_worksite w on w.dbid = f.worksite_id " +
                "       left join t_user cu on cu.dbid = f.create_id " +
                "       left join t_user mu on mu.dbid = f.last_modify_id " +
                "       left join t_user du on du.dbid = f.del_id " +
                "       left join t_user fu on fu.dbid = f.finish_id");
        sb.append(" where f.del_flag=0");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, finance);
        String sql = sb.toString();
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
    }

    /**
     * 通过主键查询
     */
    public Finance getById(Integer id) {
        String sql = "select * from t_finance where dbid = ?";
        List<Finance> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Finance.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 通过工地获取
     */
    public Finance getByWorksiteId(Integer worksiteId) {
        String sql = "select * from t_finance where worksite_id = ? and del_flag = 0";
        List<Finance> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Finance.class), worksiteId);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 删除
     */
    public int delete(Integer dbid, Integer delFlag, Integer delId, String delTime) {
        String sql = "update t_finance set del_id=?, del_time=?, del_flag=? where dbid=?";
        return this.jdbcTemplate.update(sql, delId, delTime, delFlag, dbid);
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, Finance finance) {
        if (!StringUtils.isEmpty(finance.getWorksiteName())) {
            sb.append(" and w.name like ?");
            params.add("%" + finance.getWorksiteName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
    }

    /**
     * 更新支出金额
     */
    public void updateOutMoney(Finance finance) {
        String sql = "update t_finance set out_money = ? where dbid = ?";
        this.jdbcTemplate.update(sql, finance.getOutMoney(), finance.getId());
    }

}
