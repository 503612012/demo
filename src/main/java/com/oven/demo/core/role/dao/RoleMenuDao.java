package com.oven.demo.core.role.dao;

import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.base.entity.ConditionAndParams;
import com.oven.demo.core.role.entity.RoleMenu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色-菜单关系dao层
 *
 * @author Oven
 */
@Repository
public class RoleMenuDao extends BaseDao<RoleMenu> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过角色ID查询
     *
     * @param roleId 角色ID
     */
    public List<RoleMenu> getByRoleId(Integer roleId) {
        return super.getAll(ConditionAndParams.build("and role_id = ?", roleId));
    }

    /**
     * 通过角色ID和菜单ID查询
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    public RoleMenu getByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        return super.getOne(ConditionAndParams.build("and role_id = ? and menu_id = ?", roleId, menuId));
    }

    /**
     * 通过角色ID删除
     *
     * @param roleId 角色ID
     */
    public void deleteByRoleId(Integer roleId) {
        String sql = "delete from t_role_menu where role_id = ?";
        this.jdbcTemplate.update(sql, roleId);
    }

}
