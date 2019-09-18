package com.oven.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.vo.Role;
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
 * 角色dao层
 *
 * @author Oven
 */
@Repository
public class RoleDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过id获取
     *
     * @param id 角色ID
     */
    public Role getById(Integer id) {
        String sql = "select * from t_role where dbid = ?";
        List<Role> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Role.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页获取角色
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Role> getByPage(Integer pageNum, Integer pageSize, Role role) {
        StringBuilder sb = new StringBuilder("select * from t_role");
        addCondition(sb, role);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Role.class), (pageNum - 1) * pageSize, pageSize);
    }

    /**
     * 获取角色总数量
     */
    public Integer getTotalNum(Role role) {
        StringBuilder sb = new StringBuilder("select count(*) from t_role");
        addCondition(sb, role);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, Integer.class);
    }

    /**
     * 添加
     */
    public int add(Role role) {
        String sql = "insert into t_role (`dbid`," +
                "                         `role_name`," +
                "                         `status`," +
                "                         `create_time`," +
                "                         `create_id`," +
                "                         `last_modify_time`," +
                "                         `last_modify_id`)" +
                "                   values (null, ?, 0, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getCreateTime());
            ps.setInt(3, role.getCreateId());
            ps.setString(4, role.getLastModifyTime());
            ps.setInt(5, role.getLastModifyId());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 修改
     */
    public int update(Role roleInDb) {
        String sql = "update t_role set `role_name` = ?," +
                "                       `status` = ?," +
                "                       `create_id` = ?," +
                "                       `create_time` = ?," +
                "                       `last_modify_id` = ?," +
                "                       `last_modify_time` = ?" +
                "                 where `dbid` = ?";
        return this.jdbcTemplate.update(sql, roleInDb.getRoleName(), roleInDb.getStatus(), roleInDb.getCreateId(),
                roleInDb.getCreateTime(), roleInDb.getLastModifyId(), roleInDb.getLastModifyTime(), roleInDb.getId());
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_role where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 查询所有
     */
    public List<Role> getAll() {
        String sql = "select * from t_role where `status` = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Role.class));
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, Role role) {
        if (!StringUtils.isEmpty(role.getRoleName())) {
            sb.append(" and role_name like '%").append(role.getRoleName()).append("%'");
        }
    }

}
