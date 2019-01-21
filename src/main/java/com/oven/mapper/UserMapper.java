package com.oven.mapper;

import com.oven.vo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper层
 *
 * @author Oven
 */
public interface UserMapper {

    /**
     * 通过id获取
     *
     * @param id 用户ID
     */
    User getById(Integer id);

    /**
     * 分页获取用户
     *
     * @param index    偏移量
     * @param pageSize 每页显示数量
     */
    List<User> getByPage(@Param("index") Integer index, @Param("pageSize") Integer pageSize, @Param("user") User user);

    /**
     * 获取用户总数量
     */
    Long getTotalNum(@Param("user") User user);

    /**
     * 通过用户名查询
     *
     * @param userName 用户名
     */
    User getByUserName(String userName);

    /**
     * 添加
     */
    void add(User user);

    /**
     * 修改
     */
    void update(User userInDb);

    /**
     * 删除
     */
    void delete(Integer id);

    /**
     * 获取所有用户
     */
    List<User> getAll();

}
