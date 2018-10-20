package com.skyer.mapper;

import com.skyer.vo.Menu;

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

}
