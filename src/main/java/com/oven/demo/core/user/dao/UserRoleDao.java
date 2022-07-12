package com.oven.demo.core.user.dao;

import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.user.entity.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户-角色关系dao层
 *
 * @author Oven
 */
@Repository
public class UserRoleDao extends BaseDao<UserRole> {

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
    public void deleteByUserId(Integer userId) {
        String sql = "delete from t_user_role where user_id = ?";
        this.jdbcTemplate.update(sql, userId);
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
