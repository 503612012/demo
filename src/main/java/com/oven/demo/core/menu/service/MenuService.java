package com.oven.demo.core.menu.service;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.base.service.BaseService;
import com.oven.demo.core.menu.dao.MenuDao;
import com.oven.demo.core.menu.vo.Menu;
import com.oven.demo.core.role.service.RoleMenuService;
import com.oven.demo.core.role.vo.RoleMenu;
import com.oven.demo.core.user.service.UserRoleService;
import com.oven.demo.core.user.vo.UserRole;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
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
public class MenuService extends BaseService {

    @Resource
    private MenuDao menuDao;
    @Resource
    private RoleMenuService roleMenuService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 通过id获取
     *
     * @param id 菜单ID
     */
    public Menu getById(Integer id) {
        Menu menu = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_BY_ID, id)); // 先读取缓存
        if (menu == null) { // double check
            synchronized (this) {
                menu = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (menu == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    menu = menuDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.MENU_GET_BY_ID, id), menu);
                }
            }
        }
        return menu;
    }

    /**
     * 修改菜单
     */
    public void update(Menu menu) throws Exception {
        menu.setLastModifyTime(DateTime.now().toString(AppConst.TIME_PATTERN));
        menu.setLastModifyId(CommonUtils.getCurrentUser().getId());
        menuDao.update(menu);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ROLE_PREFIX, RedisCacheKey.MENU_PREFIX, RedisCacheKey.ROLEMENU_PREFIX, RedisCacheKey.USER_MENU_CODES);
    }

    /**
     * 根据用户ID获取该用户的目录树
     *
     * @param userId 用户ID
     */
    public List<Map<String, Object>> getMenuTreeByUserId(Integer userId) {
        List<Map<String, Object>> list = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_MENU_TREE_BY_USERID, userId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_MENU_TREE_BY_USERID, userId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    List<List<RoleMenu>> menus = new ArrayList<>();
                    List<UserRole> roles = userRoleService.getByUserId(userId);
                    roles.forEach(role -> menus.add(roleMenuService.getByRoleId(role.getRoleId())));
                    list = installMenu(userId, menus);
                    super.set(MessageFormat.format(RedisCacheKey.MENU_GET_MENU_TREE_BY_USERID, userId), list);
                }
            }
        }
        return list;
    }

    /**
     * 组装菜单
     *
     * @param userId 用户ID
     * @param list   所有菜单的ID（不区分菜单的类型，即该用户下所有拥有的权限）
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
     * @param userId 用户ID
     * @param pid    菜单ID
     */
    private List<Menu> getByPidAndHasPermission(Integer userId, Integer pid) {
        List<Menu> list = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_BY_PID_AND_HASPERMISSION, userId, pid)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_BY_PID_AND_HASPERMISSION, userId, pid)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    List<Integer> menuIds = new ArrayList<>();
                    List<UserRole> roles = userRoleService.getByUserId(userId);
                    for (UserRole role : roles) {
                        List<RoleMenu> roleMenus = roleMenuService.getByRoleId(role.getRoleId());
                        if (roleMenus != null && roleMenus.size() > 0) {
                            roleMenus.forEach(roleMenu -> menuIds.add(roleMenu.getMenuId()));
                        }
                    }
                    list = menuDao.getByPidAndHasPermission(pid, menuIds);
                    Collections.sort(list);
                    super.set(MessageFormat.format(RedisCacheKey.MENU_GET_BY_PID_AND_HASPERMISSION, userId, pid), list);
                }
            }
        }
        return list;
    }

    /**
     * 通过用户ID获取该用户的所有权限编码
     *
     * @param userId 用户ID
     */
    public List<String> getAllMenuCodeByUserId(Integer userId) {
        List<String> list = super.get(MessageFormat.format(RedisCacheKey.USER_MENU_CODES, userId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.USER_MENU_CODES, userId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = new ArrayList<>();
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
                    super.set(MessageFormat.format(RedisCacheKey.USER_MENU_CODES, userId), list);
                }
            }
        }
        return list;
    }

    /**
     * 通过父ID获取
     */
    public List<Menu> getByPid(Integer pid) {
        List<Menu> list = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_BY_PID, pid)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.MENU_GET_BY_PID, pid)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = menuDao.getByPid(pid);
                    super.set(MessageFormat.format(RedisCacheKey.MENU_GET_BY_PID, pid), list);
                }
            }
        }
        return list;
    }

    /**
     * 分页菜单树形表格内容
     */
    public List<Menu> getMenuTreeTableData() {
        List<Menu> list = super.get(RedisCacheKey.MENU_GET_MENU_TREE_DATLE_DATA); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.MENU_GET_MENU_TREE_DATLE_DATA); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = menuDao.getMenuTreeTableData();
                    super.set(RedisCacheKey.MENU_GET_MENU_TREE_DATLE_DATA, list);
                }
            }
        }
        return list;
    }

}
