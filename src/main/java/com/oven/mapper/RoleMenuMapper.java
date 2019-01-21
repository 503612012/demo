package com.oven.mapper;

import com.oven.vo.RoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色-菜单关系mapper层
 *
 * @author Oven
 */
public interface RoleMenuMapper {

    /**
     * 通过角色ID查询
     *
     * @param roleId 角色ID
     */
    List<RoleMenu> getByRoleId(Integer roleId);

    /**
     * 通过角色ID和菜单ID查询
     *
     * @param roleId 角色ID
     * @param menuId 菜单ID
     */
    RoleMenu getByRoleIdAndMenuId(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    /**
     * 通过角色ID删除
     *
     * @param roleId 角色ID
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 添加
     */
    void add(RoleMenu item);

}
