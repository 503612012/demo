package com.skyer.mapper;

import com.skyer.vo.Menu;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取某个用户授过权的菜单的子菜单
     *
     * @param pid     用户ID
     * @param menuIds 菜单ID列表
     */
    List<Menu> getByPidAndHasPermission(@Param("pid") Integer pid, @Param("menuIds") List<Integer> menuIds);

}
