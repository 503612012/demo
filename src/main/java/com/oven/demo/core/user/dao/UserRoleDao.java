package com.oven.demo.core.user.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
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
     * 通过用户id查询
     *
     * @param userId 用户id
     */
    public List<UserRole> getByUserId(Integer userId) {
        return super.getAll(ConditionAndParams.build("and user_id = ?", userId));
    }

    /**
     * 通过用户id和角色id查询
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    public UserRole getByUserIdAndRoleId(Integer userId, Integer roleId) {
        return super.getOne(ConditionAndParams.build("and user_id = ? and role_id = ?", userId, roleId));
    }

    /**
     * 通过用户id删除
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Integer userId) {
        String sql = "delete from t_user_role where user_id = ?";
        this.jdbcTemplate.update(sql, userId);
    }

    /**
     * 通过角色id获取
     *
     * @param roleId 角色id
     */
    public List<UserRole> getByRoleId(Integer roleId) {
        return super.getAll(ConditionAndParams.build("and role_id = ?", roleId));
    }

}
