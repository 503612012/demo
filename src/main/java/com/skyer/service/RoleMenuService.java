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

    /**
     * 通过角色ID和菜单ID查询
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    public RoleMenu getByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        RoleMenu roleMenu = super.get(RedisCacheKey.ROLEMENU_GET_BY_ROLEID_AND_MENUID + roleId + "_" + menuId); // 先读取缓存
        if (roleMenu == null) { // double check
            synchronized (this) {
                roleMenu = super.get(RedisCacheKey.ROLEMENU_GET_BY_ROLEID_AND_MENUID + roleId + "_" + menuId); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (roleMenu == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    roleMenu = roleMenuMapper.getByRoleIdAndMenuId(roleId, menuId);
                    super.set(RedisCacheKey.ROLEMENU_GET_BY_ROLEID_AND_MENUID + roleId + "_" + menuId, roleMenu);
                }
            }
        }
        return roleMenu;
    }

    /**
     * 通过角色ID删除
     *
     * @param roleId 角色ID
     */
    public void deleteByRoleId(Integer roleId) {
        roleMenuMapper.deleteByRoleId(roleId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ROLEMENU_PREFIX);
    }

    /**
     * 添加
     */
    public void add(RoleMenu item) {
        roleMenuMapper.add(item);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ROLEMENU_PREFIX);
    }

}
