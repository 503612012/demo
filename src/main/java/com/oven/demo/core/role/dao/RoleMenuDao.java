package com.oven.demo.core.role.dao;

import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.role.vo.RoleMenu;
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
        String sql = "select * from t_role_menu where role_id = ?";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(RoleMenu.class), roleId);
    }

    /**
     * 通过角色ID和菜单ID查询
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    public RoleMenu getByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        String sql = "select * from t_role_menu where role_id = ? and menu_id = ?";
        List<RoleMenu> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(RoleMenu.class), roleId, menuId);
        return list.size() == 0 ? null : list.get(0);
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

    /**
     * 添加
     */
    public int add(RoleMenu roleMenu) throws Exception {
        return super.add(jdbcTemplate, roleMenu);
    }

}
