package com.skyer.service;

import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.UserRoleMapper;
import com.skyer.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户-角色关系服务层
 *
 * @author SKYER
 */
@Service
public class UserRoleService extends BaseService {

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 通过用户ID查询
     *
     * @param userId 用户ID
     */
    public List<UserRole> getByUserId(Integer userId) {
        List<UserRole> list = super.get(RedisCacheKey.USERROLE_GET_BY_USERID + userId); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.USERROLE_GET_BY_USERID + userId); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userRoleMapper.getByUserId(userId);
                    super.set(RedisCacheKey.USERROLE_GET_BY_USERID + userId, list);
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
        UserRole userRole = super.get(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID + userId + roleId); // 先读取缓存
        if (userRole == null) { // double check
            synchronized (this) {
                userRole = super.get(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID + userId + roleId); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (userRole == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    userRole = userRoleMapper.getByUserIdAndRoleId(userId, roleId);
                    super.set(RedisCacheKey.USERROLE_GET_BY_USERID_AND_ROLEID + userId + roleId, userRole);
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
        userRoleMapper.deleteByUserId(userId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 添加
     */
    public void add(UserRole userRole) {
        userRoleMapper.add(userRole);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 通过角色ID获取
     *
     * @param roleId 角色ID
     */
    public List<UserRole> getByRoleId(Integer roleId) {
        return userRoleMapper.getByRoleId(roleId);
    }

}
