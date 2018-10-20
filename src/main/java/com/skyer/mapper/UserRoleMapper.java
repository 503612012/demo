package com.skyer.mapper;

import com.skyer.vo.UserRole;

import java.util.List;

/**
 * 用户-角色关系mapper层
 *
 * @author SKYER
 */
public interface UserRoleMapper {

    /**
     * 通过用户ID查询
     *
     * @param userId 用户ID
     */
    List<UserRole> getByUserId(Integer userId);

}
