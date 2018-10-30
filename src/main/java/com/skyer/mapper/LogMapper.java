package com.skyer.mapper;

import com.skyer.vo.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志mapper层
 *
 * @author SKYER
 */
public interface LogMapper {

    /**
     * 通过id获取
     *
     * @param id 日志ID
     */
    Log getById(Integer id);

    /**
     * 分页获取日志
     *
     * @param index    偏移量
     * @param pageSize 每页显示数量
     */
    List<Log> getByPage(@Param("index") Integer index, @Param("pageSize") Integer pageSize, @Param("log") Log log);

    /**
     * 获取日志总数量
     */
    Long getTotalNum(@Param("log") Log log);

    /**
     * 添加
     */
    void add(Log log);

}
