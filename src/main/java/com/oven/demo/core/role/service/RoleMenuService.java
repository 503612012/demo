package com.oven.demo.core.role.service;

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
public class RoleMenuService {

    @Resource
    private RoleMenuDao roleMenuDao;

    /**
     * 通过角色id查询
     *
     * @param roleId 角色id
     */
    public List<RoleMenu> getByRoleId(Integer roleId) {
        return roleMenuDao.getByRoleId(roleId);
    }

    /**
     * 通过角色id和菜单id查询
     *
     * @param roleId 角色id
     * @param menuId 菜单id
     */
    public RoleMenu getByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        return roleMenuDao.getByRoleIdAndMenuId(roleId, menuId);
    }

    /**
     * 通过角色id删除
     *
     * @param roleId 角色id
     */
    public void deleteByRoleId(Integer roleId) {
        roleMenuDao.deleteByRoleId(roleId);
    }

    /**
     * 添加
     */
    public void save(RoleMenu roleMenu) throws Exception {
        roleMenuDao.save(roleMenu);
    }

}
