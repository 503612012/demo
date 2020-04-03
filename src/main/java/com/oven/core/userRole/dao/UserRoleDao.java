package com.oven.core.userRole.dao;

import com.oven.util.VoPropertyRowMapper;
import com.oven.core.userRole.vo.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

/**
 * 用户-角色关系dao层
 *
 * @author Oven
 */
@Repository
public class UserRoleDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过用户ID查询
     *
     * @param userId 用户ID
     */
    public List<UserRole> getByUserId(Integer userId) {
        String sql = "select * from t_user_role where user_id = ?";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(UserRole.class), userId);
    }

    /**
     * 通过用户ID和角色ID查询
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    public UserRole getByUserIdAndRoleId(Integer userId, Integer roleId) {
        String sql = "select * from t_user_role where user_id = ? and role_id = ?";
        List<UserRole> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(UserRole.class), userId, roleId);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 通过用户ID删除
     *
     * @param userId 用户ID
     */
    public int deleteByUserId(Integer userId) {
        String sql = "delete from t_user_role where user_id = ?";
        return this.jdbcTemplate.update(sql, userId);
    }

    /**
     * 添加
     */
    public int add(UserRole userRole) {
        String sql = "insert into t_user_role (dbid, user_id, role_id) values (null, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, userRole.getUserId());
            ps.setInt(2, userRole.getRoleId());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 通过角色ID获取
     *
     * @param roleId 角色ID
     */
    public List<UserRole> getByRoleId(Integer roleId) {
        String sql = "select * from t_user_role where role_id = ?";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(UserRole.class), roleId);
    }

}
