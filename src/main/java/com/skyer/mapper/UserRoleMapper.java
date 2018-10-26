package com.skyer.mapper;

import com.skyer.vo.UserRole;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 通过用户ID和角色ID查询
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    UserRole getByUserIdAndRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 通过用户ID删除
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Integer userId);

    /**
     * 添加
     */
    void add(UserRole userRole);

    /**
     * 通过角色ID获取
     *
     * @param roleId 角色ID
     */
    List<UserRole> getByRoleId(Integer roleId);

}
