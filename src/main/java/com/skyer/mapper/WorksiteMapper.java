package com.skyer.mapper;

import com.skyer.vo.Worksite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WorksiteMapper
 *
 * @author SKYER
 */
public interface WorksiteMapper {

    /**
     * 分页查询工地
     */
    List<Worksite> getByPage(@Param("index") Integer index, @Param("pageSize") Integer pageSize, @Param("worksite") Worksite worksite);

    /**
     * 统计工地总数量
     */
    Long getTotalNum(@Param("worksite") Worksite worksite);

    /**
     * 添加
     */
    int add(Worksite worksite);

    /**
     * 更新
     */
    void update(Worksite worksite);

    /**
     * 删除
     */
    int delete(Integer id);

    /**
     * 通过主键查询
     */
    Worksite getById(Integer id);

    /**
     * 查询所有工地
     */
    List<Worksite> getAll();

}
