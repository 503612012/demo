package com.oven.demo.core.role.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.menu.entity.Menu;
import com.oven.demo.core.menu.service.MenuService;
import com.oven.demo.core.role.dao.RoleDao;
import com.oven.demo.core.role.entity.Role;
import com.oven.demo.core.role.entity.RoleMenu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色服务层
 *
 * @author Oven
 */
@Service
public class RoleService {

    @Resource
    private RoleDao roleDao;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 通过id获取
     *
     * @param id 角色id
     */
    public Role getById(Integer id) {
        return roleDao.getById(id);
    }

    /**
     * 分页获取角色
     */
    public List<Role> getByPage(Role role) {
        return roleDao.getByPage(role);
    }

    /**
     * 获取角色总数量
     */
    public Integer getTotalNum(Role role) {
        return roleDao.getTotalNum(role);
    }

    /**
     * 添加角色
     */
    public void save(Role role) throws Exception {
        role.setStatus(0);
        role.setCreateId(CommonUtils.getCurrentUser().getId());
        role.setCreateTime(DateUtils.getCurrentTime());
        role.setLastModifyId(CommonUtils.getCurrentUser().getId());
        role.setLastModifyTime(DateUtils.getCurrentTime());
        roleDao.save(role);
    }

    /**
     * 修改角色
     */
    public void update(Role role) throws Exception {
        role.setLastModifyTime(DateUtils.getCurrentTime());
        role.setLastModifyId(CommonUtils.getCurrentUser().getId());
        roleDao.update(role);
    }

    /**
     * 删除角色
     */
    public void delete(Integer id) throws Exception {
        roleDao.delete(id);
    }

    /**
     * 获取所有角色
     */
    public List<Role> getAll() {
        return roleDao.getAll();
    }

    /**
     * 获取菜单目录树
     *
     * @param id 角色id
     */
    public JSONArray getMenuTreeByRoleId(Integer id) {
        JSONArray result = new JSONArray();
        List<Menu> levelOne = menuService.getByPid(0); // 获取所有一级菜单
        for (Menu levelOneItem : levelOne) {
            JSONObject oneObj = new JSONObject();
            oneObj.put("label", levelOneItem.getMenuName());
            oneObj.put("id", levelOneItem.getId());
            RoleMenu levelOneRoleMenu = roleMenuService.getByRoleIdAndMenuId(id, levelOneItem.getId());
            if (levelOneRoleMenu == null) {
                oneObj.put("checked", false);
            } else {
                oneObj.put("checked", true);
            }
            List<Menu> levelTwo = menuService.getByPid(levelOneItem.getId()); // 获取所有二级菜单
            JSONArray levelOneChildren = new JSONArray();
            for (Menu levelTwoItem : levelTwo) {
                JSONObject twoObj = new JSONObject();
                twoObj.put("label", levelTwoItem.getMenuName());
                twoObj.put("id", levelTwoItem.getId());
                RoleMenu levelTwoRoleMenu = roleMenuService.getByRoleIdAndMenuId(id, levelTwoItem.getId());
                if (levelTwoRoleMenu == null) {
                    twoObj.put("checked", false);
                } else {
                    twoObj.put("checked", true);
                }
                List<Menu> levelThree = menuService.getByPid(levelTwoItem.getId()); // 获取所有三级菜单
                JSONArray levelTwoChildren = new JSONArray();
                for (Menu levelThreeItem : levelThree) {
                    JSONObject threeObj = new JSONObject();
                    threeObj.put("label", levelThreeItem.getMenuName());
                    threeObj.put("id", levelThreeItem.getId());
                    RoleMenu levelThreeRoleMenu = roleMenuService.getByRoleIdAndMenuId(id, levelThreeItem.getId());
                    if (levelThreeRoleMenu == null) {
                        threeObj.put("checked", false);
                    } else {
                        threeObj.put("checked", true);
                    }
                    levelTwoChildren.add(threeObj);
                }
                if (!levelTwoChildren.isEmpty()) {
                    twoObj.put("children", levelTwoChildren);
                    twoObj.put("state", "closed");
                }
                levelOneChildren.add(twoObj);
            }
            if (!levelOneChildren.isEmpty()) {
                oneObj.put("children", levelOneChildren);
                oneObj.put("state", "closed");
            }
            result.add(oneObj);
        }
        return result;
    }

    /**
     * 设置角色权限
     *
     * @param roleId  角色id
     * @param menuIds 菜单id列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void setRoleMenu(Integer roleId, String menuIds) throws Exception {
        // 删除角色原有的所有权限
        roleMenuService.deleteByRoleId(roleId);
        if (StringUtils.isEmpty(menuIds)) { // 删除了该角色所有的权限
            return;
        }
        // 给角色添加新的菜单
        String[] menus = menuIds.split(",");
        for (String menuId : menus) {
            RoleMenu item = new RoleMenu();
            item.setRoleId(roleId);
            item.setMenuId(Integer.parseInt(menuId));
            roleMenuService.save(item);
        }
    }

}
