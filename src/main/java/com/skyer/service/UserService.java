package com.skyer.service;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.UserMapper;
import com.skyer.vo.Role;
import com.skyer.vo.User;
import com.skyer.vo.UserRole;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务层
 *
 * @author SKYER
 */
@Service
@Transactional
public class UserService extends BaseService {

    @Resource
    private UserMapper userMapper;
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
        User user = super.get(RedisCacheKey.USER_GET_BY_ID + id); // 先读取缓存
        if (user == null) { // double check
            synchronized (this) {
                user = super.get(RedisCacheKey.USER_GET_BY_ID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (user == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    user = userMapper.getById(id);
                    super.set(RedisCacheKey.USER_GET_BY_ID + id, user);
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
        List<User> list = super.get(RedisCacheKey.USER_GET_BY_PAGE + pageNum + "_" + pageSize + "_" + user.toString()); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.USER_GET_BY_PAGE + pageNum + "_" + pageSize + "_" + user.toString()); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userMapper.getByPage((pageNum - 1) * pageSize, pageSize, user);
                    super.set(RedisCacheKey.USER_GET_BY_PAGE + pageNum + "_" + pageSize + "_" + user.toString(), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取用户总数量
     */
    public Long getTotalNum(User user) {
        Long totalNum = super.get(RedisCacheKey.USER_GET_TOTAL_NUM + user.toString()); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(RedisCacheKey.USER_GET_TOTAL_NUM + user.toString()); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = userMapper.getTotalNum(user);
                    super.set(RedisCacheKey.USER_GET_TOTAL_NUM + user.toString(), totalNum);
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
        User user = super.get(RedisCacheKey.USER_GET_BY_USERNAME + userName); // 先读取缓存
        if (user == null) { // double check
            synchronized (this) {
                user = super.get(RedisCacheKey.USER_GET_BY_USERNAME + userName); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (user == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    user = userMapper.getByUserName(userName);
                    super.set(RedisCacheKey.USER_GET_BY_USERNAME + userName, user);
                }
            }
        }
        return user;
    }

    /**
     * 添加用户
     */
    public void add(User user) {
        user.setCreateId(super.getCurrentUser().getId());
        user.setCreateTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        user.setLastModifyId(super.getCurrentUser().getId());
        user.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        Md5Hash md5 = new Md5Hash(user.getPassword(), AppConst.MD5_SALT, 2);
        user.setPassword(md5.toString());
        // 移除缓存
        super.batchRemove(RedisCacheKey.USER_PREFIX);
        // 记录日志
        super.addLog("添加用户", user.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        userMapper.add(user);
    }

    /**
     * 修改用户
     */
    public void update(User user) {
        User userInDb = this.getById(user.getId());
        String nickName = userInDb.getNickName();
        StringBuilder content = new StringBuilder();
        if (!userInDb.getNickName().equals(user.getNickName())) {
            content.append("昵称由[").append(userInDb.getNickName()).append("]改为[").append(user.getNickName()).append("]，");
            userInDb.setNickName(user.getNickName());
        }
        if (!StringUtils.isEmpty(user.getPassword())) {
            if (!userInDb.getPassword().equals(user.getPassword())) {
                Md5Hash md5 = new Md5Hash(user.getPassword(), AppConst.MD5_SALT, 2);
                userInDb.setPassword(md5.toString());
                content.append("密码修改了，");
            }
        }
        if (user.getStatus() == null) {
            user.setStatus(0);
        }
        if (!userInDb.getStatus().equals(user.getStatus())) {
            content.append("状态由[").append(userInDb.getStatus() == 0 ? "正常" : "锁定").append("]改为[").append(user.getStatus() == 0 ? "正常" : "锁定").append("]，");
            userInDb.setStatus(user.getStatus());
        }
        if (!userInDb.getAge().equals(user.getAge())) {
            content.append("年龄由[").append(userInDb.getAge()).append("]改为[").append(user.getAge()).append("]，");
            userInDb.setAge(user.getAge());
        }
        if (!userInDb.getEmail().equals(user.getEmail())) {
            content.append("邮箱由[").append(userInDb.getEmail()).append("]改为[").append(user.getEmail()).append("]，");
            userInDb.setEmail(user.getEmail());
        }
        if (!userInDb.getGender().equals(user.getGender())) {
            content.append("性别由[").append(userInDb.getGender() == 1 ? "男" : "女").append("]改为[").append(user.getGender() == 1 ? "男" : "女").append("]，");
            userInDb.setGender(user.getGender());
        }
        if (!userInDb.getPhone().equals(user.getPhone())) {
            content.append("手机号由[").append(userInDb.getPhone()).append("]改为[").append(user.getPhone()).append("]，");
            userInDb.setPhone(user.getPhone());
        }
        String str = content.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            userInDb.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            userInDb.setLastModifyId(super.getCurrentUser().getId());
            // 移除缓存
            super.batchRemove(RedisCacheKey.USER_PREFIX);
            // 记录日志
            super.addLog("修改用户", "[" + nickName + "]" + str, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
            userMapper.update(userInDb);
        }
    }

    /**
     * 删除用户
     */
    public void delete(Integer id) {
        User user = this.getById(id);
        // 删除该用户的所有角色信息
        userRoleService.deleteByUserId(id);
        // 移除缓存
        super.batchRemove(RedisCacheKey.USER_PREFIX);
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
        // 记录日志
        super.addLog("删除用户", user.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        userMapper.delete(id);
    }

    /**
     * 通过用户ID获取角色列表
     *
     * @param id 用户ID
     */
    public List<JSONObject> getRoleByUserId(Integer id) {
        List<JSONObject> list = super.get(RedisCacheKey.USERROLE_GET_ROLE_BY_USERID + id); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.USERROLE_GET_ROLE_BY_USERID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
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
                    super.set(RedisCacheKey.USERROLE_GET_ROLE_BY_USERID + id, list);
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
    public void setUserRole(Integer userId, String roleIds) {
        // 删除用户原有的所有角色
        userRoleService.deleteByUserId(userId);
        // 给用户添加新的角色
        List<UserRole> userRoles = new ArrayList<>();
        String[] roles = roleIds.split(",");
        for (String roleId : roles) {
            UserRole item = new UserRole();
            item.setUserId(userId);
            item.setRoleId(Integer.parseInt(roleId));
            userRoleService.add(item);
            userRoles.add(item);
        }
        User user = this.getById(userId);
        StringBuilder roleNames = new StringBuilder();
        for (UserRole item : userRoles) {
            roleNames.append(roleService.getById(item.getRoleId()).getRoleName()).append("，");
        }
        String content = roleNames.toString();
        if (content.length() > 0) {
            content = content.substring(0, content.length() - 1);
        }
        // 移除缓存
        super.batchRemove(RedisCacheKey.USERROLE_PREFIX);
        super.batchRemove(RedisCacheKey.ROLEMENU_PREFIX);
        super.batchRemove(RedisCacheKey.ROLE_PREFIX);
        super.batchRemove(RedisCacheKey.MENU_PREFIX);
        // 记录日志
        super.addLog("分配角色", "用户[" + user.getNickName() + "]分配角色[" + content + "]", super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
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
                    list = userMapper.getAll();
                    super.set(RedisCacheKey.USER_GET_ALL, list);
                }
            }
        }
        return list;
    }

}
