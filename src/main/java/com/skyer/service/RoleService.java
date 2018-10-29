package com.skyer.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.RoleMapper;
import com.skyer.vo.*;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务层
 *
 * @author SKYER
 */
@Service
public class RoleService extends BaseService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MenuService menuService;
    @Resource
    private RoleMenuService roleMenuService;

    /**
     * 通过id获取
     *
     * @param id 角色ID
     */
    public Role getById(Integer id) {
        Role role = super.get(RedisCacheKey.ROLE_GET_BY_ID + id); // 先读取缓存
        if (role == null) { // double check
            synchronized (this) {
                role = super.get(RedisCacheKey.ROLE_GET_BY_ID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (role == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    role = roleMapper.getById(id);
                    super.set(RedisCacheKey.ROLE_GET_BY_ID + id, role);
                }
            }
        }
        return role;
    }

    /**
     * 分页获取角色
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     */
    public List<Role> getByPage(Integer pageNum, Integer pageSize, Role role) {
        List<Role> list = super.get(RedisCacheKey.ROLE_GET_BY_PAGE + pageNum + pageSize + role.toString()); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.ROLE_GET_BY_PAGE + pageNum + pageSize + role.toString()); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = roleMapper.getByPage((pageNum - 1) * pageSize, pageSize, role);
                    super.set(RedisCacheKey.ROLE_GET_BY_PAGE + pageNum + pageSize + role.toString(), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取角色总数量
     */
    public Long getTotalNum(Role role) {
        Long totalNum = super.get(RedisCacheKey.ROLE_GET_TOTAL_NUM + role.toString()); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(RedisCacheKey.ROLE_GET_TOTAL_NUM + role.toString()); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = roleMapper.getTotalNum(role);
                    super.set(RedisCacheKey.ROLE_GET_TOTAL_NUM + role.toString(), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 添加角色
     */
    public void add(Role role) {
        role.setCreateId(super.getCurrentUser().getId());
        role.setCreateTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        role.setLastModifyId(super.getCurrentUser().getId());
        role.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        // 移除角色相关的缓存
        super.batchRemove(RedisCacheKey.ROLE_PREFIX);
        // 记录日志
        super.addLog("添加角色", role.toString(), super.getCurrentUser().getId(), super.getCurrentUserIp());
        roleMapper.add(role);
    }

    /**
     * 修改角色
     */
    public void update(Role role) {
        Role roleInDb = this.getById(role.getId());
        StringBuilder content = new StringBuilder();
        if (!role.getRoleName().equals(roleInDb.getRoleName())) {
            content.append("角色名称由[").append(roleInDb.getRoleName()).append("]改为[").append(role.getRoleName()).append("]，");
            roleInDb.setRoleName(role.getRoleName());
        }
        if (role.getStatus() == null) {
            role.setStatus(0);
        }
        if (!role.getStatus().equals(roleInDb.getStatus())) {
            content.append("状态由[").append(roleInDb.getStatus() == 0 ? "正常" : "锁定").append("]改为[").append(role.getStatus() == 0 ? "正常" : "锁定").append("]，");
            roleInDb.setStatus(role.getStatus());
        }
        String str = content.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            roleInDb.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            roleInDb.setLastModifyId(super.getCurrentUser().getId());
            // 移除角色相关的缓存
            super.batchRemove(RedisCacheKey.ROLE_PREFIX);
            super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
            // 记录日志
            super.addLog("修改角色", str, super.getCurrentUser().getId(), super.getCurrentUserIp());
            roleMapper.update(roleInDb);
        }
    }

    /**
     * 删除角色
     */
    public void delete(Integer id) {
        Role role = this.getById(id);
        // 移除角色相关的缓存
        super.batchRemove(RedisCacheKey.ROLE_PREFIX);
        // 记录日志
        super.addLog("删除角色", role.toString(), super.getCurrentUser().getId(), super.getCurrentUserIp());
        roleMapper.delete(id);
    }

    /**
     * 获取所有角色
     */
    public List<Role> getAll() {
        List<Role> list = super.get(RedisCacheKey.ROLE_GET_ALL); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.ROLE_GET_ALL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = roleMapper.getAll();
                    super.set(RedisCacheKey.ROLE_GET_ALL, list);
                }
            }
        }
        return list;
    }

    /**
     * 获取菜单目录树
     *
     * @param id 角色ID
     */
    public JSONArray getMenuTreeByRoleId(Integer id) {
        JSONArray result = new JSONArray();
        List<Menu> levelOne = menuService.getByPid(0); // 获取所有一级菜单
        for (Menu levelOneItem : levelOne) {
            JSONObject oneObj = new JSONObject();
            oneObj.put("text", levelOneItem.getMenuName());
            oneObj.put("state", "closed");
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
                twoObj.put("text", levelTwoItem.getMenuName());
                twoObj.put("state", "closed");
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
                    threeObj.put("text", levelThreeItem.getMenuName());
                    threeObj.put("id", levelThreeItem.getId());
                    RoleMenu levelThreeRoleMenu = roleMenuService.getByRoleIdAndMenuId(id, levelThreeItem.getId());
                    if (levelThreeRoleMenu == null) {
                        threeObj.put("checked", false);
                    } else {
                        threeObj.put("checked", true);
                    }
                    levelTwoChildren.add(threeObj);
                }
                twoObj.put("children", levelTwoChildren);
                levelOneChildren.add(twoObj);
            }
            oneObj.put("children", levelOneChildren);
            result.add(oneObj);
        }
        return result;
    }

    /**
     * 设置角色权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    public void setRoleMenu(Integer roleId, String menuIds) {
        // 删除用户原有的所有角色
        roleMenuService.deleteByRoleId(roleId);
        // 给角色添加新的菜单
        List<RoleMenu> roleMenus = new ArrayList<>();
        String[] menus = menuIds.split(",");
        for (String menuId : menus) {
            RoleMenu item = new RoleMenu();
            item.setRoleId(roleId);
            item.setMenuId(Integer.parseInt(menuId));
            roleMenuService.add(item);
            roleMenus.add(item);
        }
        Role role = this.getById(roleId);
        StringBuilder menuNames = new StringBuilder();
        for (RoleMenu item : roleMenus) {
            menuNames.append(menuService.getById(item.getMenuId()).getMenuName()).append("，");
        }
        String content = menuNames.toString();
        if (content.length() > 0) {
            content = content.substring(0, content.length() - 1);
        }
        // 移除缓存
        super.batchRemove(RedisCacheKey.ROLEMENU_PREFIX);
        super.batchRemove(RedisCacheKey.USER_MENU_CODES);
        super.batchRemove(RedisCacheKey.MENU_PREFIX);
        // 记录日志
        super.addLog("分配权限", "角色[" + role.getRoleName() + "]分配权限[" + content + "]", super.getCurrentUser().getId(), super.getCurrentUserIp());
    }

}
