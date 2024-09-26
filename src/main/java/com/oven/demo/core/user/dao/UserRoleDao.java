package com.oven.demo.core.user.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.demo.core.user.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户-角色关系dao层
 *
 * @author Oven
 */
@Repository
public class UserRoleDao extends BaseDao<UserRole> {

    /**
     * 通过用户id查询
     *
     * @param userId 用户id
     */
    public List<UserRole> getByUserId(Integer userId) {
        return super.getAll(ConditionAndParams.eq("user_id", userId));
    }

    /**
     * 通过用户id和角色id查询
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    public UserRole getByUserIdAndRoleId(Integer userId, Integer roleId) {
        return super.getOne(ConditionAndParams.eq("user_id", userId).andEq("role_id", roleId));
    }

    /**
     * 通过用户id删除
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Integer userId) {
        super.delete(ConditionAndParams.eq("user_id", userId));
    }

    /**
     * 通过角色id获取
     *
     * @param roleId 角色id
     */
    public List<UserRole> getByRoleId(Integer roleId) {
        return super.getAll(ConditionAndParams.eq("role_id", roleId));
    }

}
