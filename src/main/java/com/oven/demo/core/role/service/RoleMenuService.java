package com.oven.demo.core.role.service;

import cn.hutool.core.util.StrUtil;
import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.service.BaseService;
import com.oven.demo.core.role.dao.RoleMenuDao;
import com.oven.demo.core.role.entity.RoleMenu;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色-菜单关系服务层
 *
 * @author Oven
 */
@Service
public class RoleMenuService extends BaseService {

    @Resource
    private RoleMenuDao roleMenuDao;

    /**
     * 通过角色id查询
     *
     * @param roleId 角色id
     */
    public List<RoleMenu> getByRoleId(Integer roleId) {
        List<RoleMenu> list = super.get(StrUtil.format(RedisCacheKey.ROLEMENU_GET_BY_ROLEID, roleId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(StrUtil.format(RedisCacheKey.ROLEMENU_GET_BY_ROLEID, roleId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = roleMenuDao.getByRoleId(roleId);
                    super.set(StrUtil.format(RedisCacheKey.ROLEMENU_GET_BY_ROLEID, roleId), list);
                }
            }
        }
        return list;
    }

    /**
     * 通过角色id和菜单id查询
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    public RoleMenu getByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        RoleMenu roleMenu = super.get(StrUtil.format(RedisCacheKey.ROLEMENU_GET_BY_ROLEID_AND_MENUID, roleId, menuId)); // 先读取缓存
        if (roleMenu == null) { // double check
            synchronized (this) {
                roleMenu = super.get(StrUtil.format(RedisCacheKey.ROLEMENU_GET_BY_ROLEID_AND_MENUID, roleId, menuId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (roleMenu == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    roleMenu = roleMenuDao.getByRoleIdAndMenuId(roleId, menuId);
                    super.set(StrUtil.format(RedisCacheKey.ROLEMENU_GET_BY_ROLEID_AND_MENUID, roleId, menuId), roleMenu);
                }
            }
        }
        return roleMenu;
    }

    /**
     * 通过角色id删除
     *
     * @param roleId 角色id
     */
    public void deleteByRoleId(Integer roleId) {
        roleMenuDao.deleteByRoleId(roleId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ROLEMENU_PREFIX);
    }

    /**
     * 添加
     */
    public void save(RoleMenu roleMenu) throws Exception {
        roleMenuDao.save(roleMenu);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ROLEMENU_PREFIX);
    }

}
