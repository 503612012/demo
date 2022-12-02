package com.oven.demo.core.user.service;

import com.alibaba.fastjson.JSONObject;
import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.core.base.service.BaseService;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务层
 *
 * @author Oven
 */
@Service
public class UserService extends BaseService {

    @Resource
    private UserDao userDao;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleService userRoleService;

    /**
     * 通过id获取
     *
     * @param id 用户ID
     */
    public User getById(Integer id) {
        User user = super.get(MessageFormat.format(RedisCacheKey.USER_GET_BY_ID, id)); // 先读取缓存
        if (user == null) { // double check
            synchronized (this) {
                user = super.get(MessageFormat.format(RedisCacheKey.USER_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (user == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    user = userDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.USER_GET_BY_ID, id), user);
                }
            }
        }
        return user;
    }

    /**
     * 分页获取用户
     *
     * @param pageNum  页码
     * @param pageSize 每页显示数量
     */
    public List<User> getByPage(Integer pageNum, Integer pageSize, User user) {
        List<User> list = super.get(MessageFormat.format(RedisCacheKey.USER_GET_BY_PAGE, pageNum, pageSize, user.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.USER_GET_BY_PAGE, pageNum, pageSize, user.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userDao.getByPage(pageNum, pageSize, user);
                    super.set(MessageFormat.format(RedisCacheKey.USER_GET_BY_PAGE, pageNum, pageSize, user.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取用户总数量
     */
    public Integer getTotalNum(User user) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.USER_GET_TOTAL_NUM, user.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.USER_GET_TOTAL_NUM, user.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = userDao.getTotalNum(user);
                    super.set(MessageFormat.format(RedisCacheKey.USER_GET_TOTAL_NUM, user.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 通过用户名查询
     *
     * @param userName 用户名
     */
    public User getByUserName(String userName) {
        User user = super.get(MessageFormat.format(RedisCacheKey.USER_GET_BY_USERNAME, userName)); // 先读取缓存
        if (user == null) { // double check
            synchronized (this) {
                user = super.get(MessageFormat.format(RedisCacheKey.USER_GET_BY_USERNAME, userName)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (user == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    user = userDao.getByUserName(userName);
                    super.set(MessageFormat.format(RedisCacheKey.USER_GET_BY_USERNAME, userName), user);
                }
            }
        }
        return user;
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
        // 移除缓存
        super.batchRemove(RedisCacheKey.USER_PREFIX);
    }

    /**
     * 修改用户
     */
    public void update(User user) throws Exception {
        user.setLastModifyTime(DateUtils.getCurrentTime());
        user.setLastModifyId(CommonUtils.getCurrentUser().getId());
        userDao.update(user);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USER_PREFIX);
    }

    /**
     * 删除用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) throws Exception {
        // 删除该用户的所有角色信息
        userRoleService.deleteByUserId(id);
        userDao.delete(id);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USER_PREFIX, RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 通过用户ID获取角色列表
     *
     * @param id 用户ID
     */
    public List<JSONObject> getRoleByUserId(Integer id) {
        List<JSONObject> list = super.get(MessageFormat.format(RedisCacheKey.USERROLE_GET_ROLE_BY_USERID, id)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.USERROLE_GET_ROLE_BY_USERID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = new ArrayList<>();
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
                    super.set(MessageFormat.format(RedisCacheKey.USERROLE_GET_ROLE_BY_USERID, id), list);
                }
            }
        }
        return list;
    }

    /**
     * 设置用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
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
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX, RedisCacheKey.ROLEMENU_PREFIX, RedisCacheKey.ROLE_PREFIX, RedisCacheKey.MENU_PREFIX);
    }

    /**
     * 获取所有用户
     */
    public Object getAll() {
        List<User> list = super.get(RedisCacheKey.USER_GET_ALL); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.USER_GET_ALL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userDao.getAll();
                    super.set(RedisCacheKey.USER_GET_ALL, list);
                }
            }
        }
        return list;
    }

    public void updateLastLoginTime(String time, Integer userId) {
        userDao.updateLastLoginTime(time, userId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USER_PREFIX, RedisCacheKey.USERROLE_PREFIX);
    }

    /**
     * 登录密码错误次数加一
     */
    public void logPasswordWrong(Integer userId) {
        userDao.logPasswordWrong(userId);
        super.batchRemove(RedisCacheKey.USER_PREFIX);
    }

    /**
     * 更新头像
     */
    public void updateAvatar(Integer id, String avatarFileName) {
        userDao.updateAvatar(id, avatarFileName);
        super.batchRemove(RedisCacheKey.USER_PREFIX);
    }

    /**
     * 重置错误次数
     */
    public void resetErrNum(Integer userId) {
        userDao.resetErrNum(userId);
        super.batchRemove(RedisCacheKey.USER_PREFIX);
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
        super.batchRemove(RedisCacheKey.USER_PREFIX);
    }

}
