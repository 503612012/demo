package com.oven.demo.core.menu.service;

import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.menu.dao.MenuDao;
import com.oven.demo.core.menu.entity.Menu;
import com.oven.demo.core.role.entity.RoleMenu;
import com.oven.demo.core.role.service.RoleMenuService;
import com.oven.demo.core.user.entity.UserRole;
import com.oven.demo.core.user.service.UserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单服务层
 *
 * @author Oven
 */
@Service
public class MenuService {

    @Resource
    private MenuDao menuDao;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 通过id获取
     *
     * @param id 菜单id
     */
    public Menu getById(Integer id) {
        return menuDao.getById(id);
    }

    /**
     * 修改菜单
     */
    public void update(Menu menu) throws Exception {
        menu.setLastModifyTime(DateUtils.getCurrentTime());
        menu.setLastModifyId(CommonUtils.getCurrentUser().getId());
        menuDao.update(menu);
    }

    /**
     * 根据用户id获取该用户的目录树
     *
     * @param userId 用户id
     */
    public List<Map<String, Object>> getMenuTreeByUserId(Integer userId) {
        List<List<RoleMenu>> menus = new ArrayList<>();
        List<UserRole> roles = userRoleService.getByUserId(userId);
        roles.forEach(role -> menus.add(roleMenuService.getByRoleId(role.getRoleId())));
        return installMenu(userId, menus);
    }

    /**
     * 组装菜单
     *
     * @param userId 用户id
     * @param list   所有菜单的id（不区分菜单的类型，即该用户下所有拥有的权限）
     */
    private List<Map<String, Object>> installMenu(Integer userId, List<List<RoleMenu>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        list.forEach(item -> item.forEach(mid -> menus.add(menuDao.getById(mid.getMenuId()))));
        Collections.sort(menus);
        for (Menu menu : menus) {
            if (menu.getPid() == 0 && menu.getStatus() == 0) { // 是一级菜单，直接放入map，并添加其子菜单
                Map<String, Object> item = new HashMap<>();
                item.put("menu", menu);
                // 添加该菜单的子菜单（授过权的子菜单）
                List<Menu> childMenus = this.getByPidAndHasPermission(userId, menu.getId());
                item.put("children", childMenus);
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 获取某个用户授过权的菜单的子菜单
     *
     * @param userId 用户id
     * @param pid    菜单id
     */
    private List<Menu> getByPidAndHasPermission(Integer userId, Integer pid) {
        List<Integer> menuIds = new ArrayList<>();
        List<UserRole> roles = userRoleService.getByUserId(userId);
        for (UserRole role : roles) {
            List<RoleMenu> roleMenus = roleMenuService.getByRoleId(role.getRoleId());
            if (roleMenus != null && !roleMenus.isEmpty()) {
                roleMenus.forEach(roleMenu -> menuIds.add(roleMenu.getMenuId()));
            }
        }
        List<Menu> list = menuDao.getByPidAndHasPermission(pid, menuIds);
        Collections.sort(list);
        return list;
    }

    /**
     * 通过用户id获取该用户的所有权限编码
     *
     * @param userId 用户id
     */
    public List<String> getAllMenuCodeByUserId(Integer userId) {
        List<String> list = new ArrayList<>();
        List<UserRole> roles = userRoleService.getByUserId(userId);
        for (UserRole role : roles) {
            List<RoleMenu> menus = roleMenuService.getByRoleId(role.getRoleId());
            for (RoleMenu menu : menus) {
                Menu item = menuDao.getById(menu.getMenuId());
                if (item != null) {
                    list.add(item.getMenuCode());
                }
            }
        }
        return list;
    }

    /**
     * 通过父id获取
     */
    public List<Menu> getByPid(Integer pid) {
        return menuDao.getByPid(pid);
    }

    /**
     * 分页菜单树形表格内容
     */
    public List<Menu> getMenuTreeTableData() {
        return menuDao.getMenuTreeTableData();
    }

}
