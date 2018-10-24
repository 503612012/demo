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

}
