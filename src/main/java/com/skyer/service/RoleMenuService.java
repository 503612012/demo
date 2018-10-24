package com.skyer.service;

import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.RoleMenuMapper;
import com.skyer.vo.RoleMenu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色-菜单关系服务层
 *
 * @author SKYER
 */
@Service
public class RoleMenuService extends BaseService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * 通过角色ID查询
     *
     * @param roleId 角色ID
     */
    public List<RoleMenu> getByRoleId(Integer roleId) {
        List<RoleMenu> list = super.get(RedisCacheKey.ROLEMENU_GET_BY_ROLEID + roleId); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.ROLEMENU_GET_BY_ROLEID + roleId); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = roleMenuMapper.getByRoleId(roleId);
                    super.set(RedisCacheKey.ROLEMENU_GET_BY_ROLEID + roleId, list);
                }
            }
        }
        return list;
    }

}
