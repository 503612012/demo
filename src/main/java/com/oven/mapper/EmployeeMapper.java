package com.oven.mapper;

import com.oven.vo.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工mapper层
 *
 * @author Oven
 */
public interface EmployeeMapper {

    /**
     * 添加
     */
    void add(Employee employee);

    /**
     * 更新
     */
    void update(Employee employee);

    /**
     * 通过主键查询
     */
    Employee getById(Integer id);

    /**
     * 分页查询员工
     */
    List<Employee> getByPage(@Param("index") Integer index, @Param("pageSize") Integer pageSize, @Param("employee") Employee employee);

    /**
     * 统计员工总数量
     */
    Long getTotalNum(@Param("employee") Employee employee);

    /**
     * 删除
     */
    void delete(Integer id);

}
