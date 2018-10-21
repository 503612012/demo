package com.skyer.service;

import com.skyer.cache.CacheService;
import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.MenuMapper;
import com.skyer.vo.Menu;
import com.skyer.vo.RoleMenu;
import com.skyer.vo.UserRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单服务层
 *
 * @author SKYER
 */
@Service
public class MenuService {

    @Resource
    private MenuMapper menuMapper;
    @Resource
    private CacheService cacheService;
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
        Menu menu = cacheService.get(RedisCacheKey.GET_MENU_BY_ID + id); // 先读取缓存
        if (menu == null) { // double check
            synchronized (this) {
                menu = cacheService.get(RedisCacheKey.GET_MENU_BY_ID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (menu == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    menu = menuMapper.getById(id);
                    cacheService.set(RedisCacheKey.GET_MENU_BY_ID + id, menu);
                }
            }
        }
        return menu;
    }

    /**
     * 根据用户ID获取该用户的目录树
     *
     * @param userId 用户ID
     */
    public List<Map<String, Object>> getMenuTreeByUserId(Integer userId) {
        List<Map<String, Object>> list = cacheService.get(RedisCacheKey.GET_MENU_TREE_BY_USERID + userId); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = cacheService.get(RedisCacheKey.GET_MENU_TREE_BY_USERID + userId); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    List<List<RoleMenu>> menus = new ArrayList<>();
                    List<UserRole> roles = userRoleService.getByUserId(userId);
                    for (UserRole role : roles) {
                        List<RoleMenu> item = roleMenuService.getByRoleId(role.getRoleId());
                        menus.add(item);
                    }
                    list = installMenu(menus);
                    cacheService.set(RedisCacheKey.GET_MENU_TREE_BY_USERID + userId, list);
                }
            }
        }
        return list;
    }

    /**
     * 组装菜单
     *
     * @param list 所有菜单的ID（不区分菜单的类型，即该用户下所有拥有的权限）
     */
    private List<Map<String, Object>> installMenu(List<List<RoleMenu>> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        for (List<RoleMenu> item : list) {
            for (RoleMenu mid : item) {
                Menu menu = menuMapper.getById(mid.getMenuId());
                menus.add(menu);
            }
        }
        for (Menu menu : menus) {
            if (menu.getPid() == 0) { // 是一级菜单，直接放入map，并添加其子菜单
                Map<String, Object> item = new HashMap<>();
                item.put("menu", menu);
                // 添加该菜单的子菜单
                List<Menu> childMenus = menuMapper.getByPid(menu.getId());
                item.put("children", childMenus);
                result.add(item);
            }
        }
        return result;
    }

}
