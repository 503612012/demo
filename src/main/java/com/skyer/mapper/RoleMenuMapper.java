package com.skyer.mapper;

import com.skyer.vo.RoleMenu;

import java.util.List;

/**
 * 角色-菜单关系mapper层
 *
 * @author SKYER
 */
public interface RoleMenuMapper {

    /**
     * 通过角色ID查询
     *
     * @param roleId 角色ID
     */
    List<RoleMenu> getByRoleId(Integer roleId);

}
