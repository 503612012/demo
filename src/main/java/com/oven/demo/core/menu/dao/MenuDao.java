package com.oven.demo.core.menu.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.basic.common.util.PropertyRowMapper;
import com.oven.demo.core.menu.entity.Menu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单dao层
 *
 * @author Oven
 */
@Repository
public class MenuDao extends BaseDao<Menu> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过父id获取
     *
     * @param pid 父id
     */
    public List<Menu> getByPid(Integer pid) {
        return super.getAll(ConditionAndParams.build("and pid = ? and `status` = 0", pid), "sort");
    }

    /**
     * 获取某个用户授过权的菜单的子菜单
     *
     * @param pid     用户id
     * @param menuIds 菜单id列表
     */
    public List<Menu> getByPidAndHasPermission(Integer pid, List<Integer> menuIds) {
        String in = StringUtils.collectionToDelimitedString(menuIds, ",");
        String sql = "select * from t_menu where pid = ? and `status` = 0 and dbid in (" + in + ") order by sort";
        return this.jdbcTemplate.query(sql, PropertyRowMapper.build(Menu.class), pid);
    }

    /**
     * 分页菜单树形表格内容
     */
    public List<Menu> getMenuTreeTableData() {
        String sql = "select dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_time, last_modify_id, status, menu_name as title from t_menu order by sort";
        return this.jdbcTemplate.query(sql, PropertyRowMapper.build(Menu.class));
    }

}
