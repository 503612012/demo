package com.oven.demo.core.menu.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.demo.core.menu.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单dao层
 *
 * @author Oven
 */
@Repository
public class MenuDao extends BaseDao<Menu> {

    /**
     * 通过父id获取
     *
     * @param pid 父id
     */
    public List<Menu> getByPid(Integer pid) {
        return super.getAll(ConditionAndParams.eq("pid", pid).andEq("`status`", 0), "sort");
    }

    /**
     * 获取某个用户授过权的菜单的子菜单
     *
     * @param pid     用户id
     * @param menuIds 菜单id列表
     */
    public List<Menu> getByPidAndHasPermission(Integer pid, List<Integer> menuIds) {
        return super.getAll(ConditionAndParams.eq("pid", pid).andEq("status", 0).andIn("dbid", menuIds), "sort");
    }

    /**
     * 分页菜单树形表格内容
     */
    public List<Menu> getMenuTreeTableData() {
        return super.getAll("sort");
    }

}
