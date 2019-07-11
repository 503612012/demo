package com.oven.service;

import com.oven.constant.RedisCacheKey;
import com.oven.dao.UserRoleDao;
import com.oven.vo.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 用户-角色关系服务层
 *
 * @author Oven
 */
@Service
@Transactional
public class UserRoleService extends BaseService {

    @Resource
    private UserRoleDao userRoleDao;

    /**
     * 通过用户ID查询
     *
     * @param userId 用户ID
     */
    public List<UserRole> getByUserId(Integer userId) {
        List<UserRole> list = super.get(MessageFormat.format(RedisCacheKey.USERROLE_GET_BY_USERID, userId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.USERROLE_GET_BY_USERID, userId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userRoleDao.getByUserId(userId);
                    super.set(MessageFormat.format(RedisCacheKey.USERROLE_GET_BY_USERID, userId), list);
                }
            }
        }
        return list;
    }

    /**
     * 通过用户ID和角色ID查询
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    public UserRole getByUserIdAndRoleId(Integer userId, Integer roleId) {
        UserRole userRole = super.get(MessageFormat.format(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID, userId, roleId)); // 先读取缓存
        if (userRole == null) { // double check
            synchronized (this) {
                userRole = super.get(MessageFormat.format(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID, userId, roleId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (userRole == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    userRole = userRoleDao.getByUserIdAndRoleId(userId, roleId);
                    super.set(MessageFormat.format(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID, userId, roleId), userRole);
                }
            }
        }
        return userRole;
    }

    /**
     * 通过用户ID删除
     *
     * @param userId 用户ID
     */
    public void deleteByUserId(Integer userId) {
        userRoleDao.deleteByUserId(userId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 添加
     */
    public void add(UserRole userRole) {
        userRoleDao.add(userRole);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 通过角色ID获取
     *
     * @param roleId 角色ID
     */
    public List<UserRole> getByRoleId(Integer roleId) {
        return userRoleDao.getByRoleId(roleId);
    }

}
