package com.skyer.mapper;

import com.skyer.vo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色mapper层
 *
 * @author SKYER
 */
public interface RoleMapper {

    /**
     * 通过id获取
     *
     * @param id 角色ID
     */
    Role getById(Integer id);

    /**
     * 分页获取角色
     *
     * @param index    偏移量
     * @param pageSize 每页显示数量
     */
    List<Role> getByPage(@Param("index") Integer index, @Param("pageSize") Integer pageSize, @Param("role") Role role);

    /**
     * 获取角色总数量
     */
    Long getTotalNum(@Param("role") Role role);

    /**
     * 添加
     */
    void add(Role role);

    /**
     * 修改
     */
    void update(Role roleInDb);

    /**
     * 删除
     */
    void delete(Integer id);

    /**
     * 查询所有
     */
    List<Role> getAll();

}
