package com.oven.demo.core.user.service;

import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.role.entity.Role;
import com.oven.demo.core.role.service.RoleService;
import com.oven.demo.core.user.dao.UserDao;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.core.user.entity.UserRole;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务层
 *
 * @author Oven
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 通过id获取
     *
     * @param id 用户id
     */
    public User getById(Integer id) {
        return userDao.getById(id);
    }

    /**
     * 分页获取用户
     */
    public List<User> getByPage(User user) {
        return userDao.getByPage(user);
    }

    /**
     * 获取用户总数量
     */
    public Integer getTotalNum(User user) {
        return userDao.getTotalNum(user);
    }

    /**
     * 通过用户名查询
     *
     * @param userName 用户名
     */
    public User getByUserName(String userName) {
        return userDao.getByUserName(userName);
    }

    /**
     * 添加用户
     */
    public void save(User user) throws Exception {
        user.setErrNum(0);
        user.setStatus(0);
        user.setCreateId(CommonUtils.getCurrentUser().getId());
        user.setCreateTime(DateUtils.getCurrentTime());
        user.setLastModifyId(CommonUtils.getCurrentUser().getId());
        user.setLastModifyTime(DateUtils.getCurrentTime());
        Md5Hash md5 = new Md5Hash(user.getPassword(), AppConst.MD5_SALT, 2);
        user.setPassword(md5.toString());
        userDao.save(user);
    }

    /**
     * 修改用户
     */
    public void update(User user) throws Exception {
        user.setLastModifyTime(DateUtils.getCurrentTime());
        user.setLastModifyId(CommonUtils.getCurrentUser().getId());
        userDao.update(user);
    }

    /**
     * 删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws Exception {
        // 删除该用户的所有角色信息
        userRoleService.deleteByUserId(id);
        userDao.delete(id);
    }

    /**
     * 通过用户id获取角色列表
     *
     * @param id 用户id
     */
    public List<JSONObject> getRoleByUserId(Integer id) {
        List<JSONObject> list = new ArrayList<>();
        List<Role> roles = roleService.getAll();
        for (Role role : roles) {
            JSONObject obj = new JSONObject();
            obj.put("role", role);
            if (userRoleService.getByUserIdAndRoleId(id, role.getId()) != null) {
                obj.put("checked", true);
            } else {
                obj.put("checked", false);
            }
            list.add(obj);
        }
        return list;
    }

    /**
     * 设置用户角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUserRole(Integer userId, String roleIds) throws Exception {
        // 删除用户原有的所有角色
        userRoleService.deleteByUserId(userId);
        // 给用户添加新的角色
        String[] roles = roleIds.split(",");
        for (String roleId : roles) {
            UserRole item = new UserRole();
            item.setUserId(userId);
            item.setRoleId(Integer.parseInt(roleId));
            userRoleService.save(item);
        }
    }

    /**
     * 获取所有用户
     */
    public List<User> getAll() {
        return userDao.getAll();
    }

    public void updateLastLoginTime(String time, Integer userId) {
        userDao.updateLastLoginTime(time, userId);
    }

    /**
     * 登录密码错误次数加一
     */
    public void logPasswordWrong(Integer userId) {
        userDao.logPasswordWrong(userId);
    }

    /**
     * 更新头像
     */
    public void updateAvatar(Integer id, String avatarFileName) {
        userDao.updateAvatar(id, avatarFileName);
    }

    /**
     * 重置错误次数
     */
    public void resetErrNum(Integer userId) {
        userDao.resetErrNum(userId);
    }

    /**
     * 修改用户个性化配置
     */
    public void updateConfig(String key, String value) {
        User currentUser = CommonUtils.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        JSONObject config;
        if (StringUtils.isEmpty(currentUser.getConfig())) {
            config = new JSONObject();
        } else {
            config = JSONObject.parseObject(currentUser.getConfig());
        }
        config.put(key, value);
        userDao.updateConfig(currentUser.getId(), config.toJSONString());
    }

}
