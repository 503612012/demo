package com.oven.demo.core.user.service;

import com.oven.demo.core.user.dao.UserRoleDao;
import com.oven.demo.core.user.entity.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户-角色关系服务层
 *
 * @author Oven
 */
@Service
public class UserRoleService {

    @Resource
    private UserRoleDao userRoleDao;

    /**
     * 通过用户id查询
     *
     * @param userId 用户id
     */
    public List<UserRole> getByUserId(Integer userId) {
        return userRoleDao.getByUserId(userId);
    }

    /**
     * 通过用户id和角色id查询
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    public UserRole getByUserIdAndRoleId(Integer userId, Integer roleId) {
        return userRoleDao.getByUserIdAndRoleId(userId, roleId);
    }

    /**
     * 通过用户id删除
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Integer userId) {
        userRoleDao.deleteByUserId(userId);
    }

    /**
     * 添加
     */
    public void save(UserRole userRole) throws Exception {
        userRoleDao.save(userRole);
    }

    /**
     * 通过角色id获取
     *
     * @param roleId 角色id
     */
    public List<UserRole> getByRoleId(Integer roleId) {
        return userRoleDao.getByRoleId(roleId);
    }

}
