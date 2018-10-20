package com.skyer.mapper;

import com.skyer.vo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户mapper层
 *
 * @author SKYER
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
    List<User> getByPage(@Param("index") Integer index, @Param("pageSize") Integer pageSize);

    /**
     * 获取用户总数量
     */
    Long getTotalNum();

    /**
     * 通过用户名查询
     *
     * @param userName 用户名
     */
    User getByUserName(String userName);

}
