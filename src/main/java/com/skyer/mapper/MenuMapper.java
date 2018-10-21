package com.skyer.mapper;

import com.skyer.vo.Menu;

import java.util.List;

/**
 * 菜单mapper层
 *
 * @author SKYER
 */
public interface MenuMapper {

    /**
     * 通过id获取
     *
     * @param id 菜单ID
     */
    Menu getById(Integer id);

    /**
     * 通过父ID获取
     *
     * @param pid 父ID
     */
    List<Menu> getByPid(Integer pid);

}
