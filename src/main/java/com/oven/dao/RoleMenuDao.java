package com.oven.dao;

import com.oven.vo.RoleMenu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * 角色-菜单关系dao层
 *
 * @author Oven
 */
@Repository
public class RoleMenuDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过角色ID查询
     *
     * @param roleId 角色ID
     */
    public List<RoleMenu> getByRoleId(Integer roleId) {
        String sql = "select * from t_role_menu where role_id = ?";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> getRoleMenu(rs), roleId);
    }

    /**
     * 通过角色ID和菜单ID查询
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    public RoleMenu getByRoleIdAndMenuId(Integer roleId, Integer menuId) {
        String sql = "select * from t_role_menu where role_id = ? and menu_id = ?";
        List<RoleMenu> list = this.jdbcTemplate.query(sql, (rs, rowNum) -> getRoleMenu(rs), roleId, menuId);
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    /**
     * 通过角色ID删除
     *
     * @param roleId 角色ID
     */
    public int deleteByRoleId(Integer roleId) {
        String sql = "delete from t_role_menu where role_id = ?";
        return this.jdbcTemplate.update(sql, roleId);
    }

    /**
     * 添加
     */
    public int add(RoleMenu item) {
        String sql = "insert into t_role_menu (dbid, role_id, menu_id) values (null, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setInt(1, item.getRoleId());
            ps.setInt(2, item.getMenuId());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 关系映射
     */
    private RoleMenu getRoleMenu(ResultSet rs) throws SQLException {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setId(rs.getInt("dbid"));
        roleMenu.setRoleId(rs.getInt("role_id"));
        roleMenu.setMenuId(rs.getInt("menu_id"));
        return roleMenu;
    }

}
