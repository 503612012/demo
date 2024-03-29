package com.oven.demo.core.user.service;

import cn.hutool.core.util.StrUtil;
import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.service.BaseService;
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
public class UserRoleService extends BaseService {

    @Resource
    private UserRoleDao userRoleDao;

    /**
     * 通过用户id查询
     *
     * @param userId 用户id
     */
    public List<UserRole> getByUserId(Integer userId) {
        List<UserRole> list = super.get(StrUtil.format(RedisCacheKey.USERROLE_GET_BY_USERID, userId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(StrUtil.format(RedisCacheKey.USERROLE_GET_BY_USERID, userId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userRoleDao.getByUserId(userId);
                    super.set(StrUtil.format(RedisCacheKey.USERROLE_GET_BY_USERID, userId), list);
                }
            }
        }
        return list;
    }

    /**
     * 通过用户id和角色id查询
     *
     * @param userId 用户id
     * @param roleId 角色id
     */
    public UserRole getByUserIdAndRoleId(Integer userId, Integer roleId) {
        UserRole userRole = super.get(StrUtil.format(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID, userId, roleId)); // 先读取缓存
        if (userRole == null) { // double check
            synchronized (this) {
                userRole = super.get(StrUtil.format(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID, userId, roleId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (userRole == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    userRole = userRoleDao.getByUserIdAndRoleId(userId, roleId);
                    super.set(StrUtil.format(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID, userId, roleId), userRole);
                }
            }
        }
        return userRole;
    }

    /**
     * 通过用户id删除
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Integer userId) {
        userRoleDao.deleteByUserId(userId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 添加
     */
    public void save(UserRole userRole) throws Exception {
        userRoleDao.save(userRole);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
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
