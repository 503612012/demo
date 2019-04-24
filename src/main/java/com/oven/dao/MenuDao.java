package com.oven.dao;

import com.oven.vo.Menu;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 菜单dao层
 *
 * @author Oven
 */
@Repository
public class MenuDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过id获取
     *
     * @param id 菜单ID
     */
    public Menu getById(Integer id) {
        String sql = "select * from t_menu where dbid = ?";
        List<Menu> list = this.jdbcTemplate.query(sql, (rs, rowNum) -> getMenu(rs), id);
        return list == null || list.size() == 0 ? null : list.get(0);
    }

    /**
     * 通过父ID获取
     *
     * @param pid 父ID
     */
    public List<Menu> getByPid(Integer pid) {
        String sql = "select * from t_menu where pid = ? and `status` = 0 order by sort";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> getMenu(rs), pid);
    }

    /**
     * 获取某个用户授过权的菜单的子菜单
     *
     * @param pid     用户ID
     * @param menuIds 菜单ID列表
     */
    public List<Menu> getByPidAndHasPermission(Integer pid, List<Integer> menuIds) {
        String in = StringUtils.collectionToDelimitedString(menuIds, ",");
        String sql = "select * from t_menu where pid = ? and `status` = 0 and dbid in (" + in + ")";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> getMenu(rs), pid);
    }

    /**
     * 修改菜单
     */
    public int update(Menu menuInDb) {
        String sql = "update t_menu set `menu_code` = ?," +
                "                       `menu_name` = ?," +
                "                       `pid` = ?," +
                "                       `sort` = ?," +
                "                       `url` = ?," +
                "                       `iconCls` = ?," +
                "                       `type` = ?," +
                "                       `create_id` = ?," +
                "                       `create_time` = ?," +
                "                       `last_modify_id` = ?," +
                "                       `last_modify_time` = ?," +
                "                       `status` = ?" +
                "                 where `dbid` = ?";
        return this.jdbcTemplate.update(sql, menuInDb.getMenuCode(), menuInDb.getMenuName(), menuInDb.getPid(), menuInDb.getSort(),
                menuInDb.getUrl(), menuInDb.getIconCls(), menuInDb.getType(), menuInDb.getCreateId(), menuInDb.getCreateTime(),
                menuInDb.getLastModifyId(), menuInDb.getLastModifyTime(), menuInDb.getStatus());
    }

    /**
     * 分页菜单树形表格内容
     */
    public List<Menu> getMenuTreeTableData() {
        String sql = "select dbid, menu_code, menu_name, pid, sort, url, iconCls, type, create_id, create_time, last_modify_time, last_modify_id, status, menu_name as title from t_menu";
        return this.jdbcTemplate.query(sql, (rs, rowNum) -> getMenu(rs));
    }

    /**
     * 关系映射
     */
    private Menu getMenu(ResultSet rs) throws SQLException {
        Menu menu = new Menu();
        menu.setId(rs.getInt("dbid"));
        menu.setMenuCode(rs.getString("menu_code"));
        menu.setMenuName(rs.getString("menu_name"));
        menu.setPid(rs.getInt("pid"));
        menu.setSort(rs.getInt("sort"));
        menu.setUrl(rs.getString("url"));
        menu.setIconCls(rs.getString("iconCls"));
        menu.setType(rs.getInt("type"));
        menu.setCreateId(rs.getInt("create_id"));
        menu.setCreateTime(rs.getString("create_time"));
        menu.setLastModifyId(rs.getInt("last_modify_id"));
        menu.setLastModifyTime(rs.getString("last_modify_time"));
        menu.setStatus(rs.getInt("status"));
        if (rs.getMetaData().getColumnCount() == 14) {
            menu.setTitle(rs.getString("title"));
        }
        return menu;
    }

}
