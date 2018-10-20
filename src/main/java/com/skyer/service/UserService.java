package com.skyer.service;

import com.skyer.cache.CacheService;
import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.UserMapper;
import com.skyer.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户服务层
 *
 * @author SKYER
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private CacheService cacheService;

    /**
     * 通过id获取
     *
     * @param id 用户ID
     */
    public User getById(Integer id) {
        User user = cacheService.get(RedisCacheKey.GET_USER_BY_ID + id); // 先读取缓存
        if (user == null) { // double check
            synchronized (this) {
                user = cacheService.get(RedisCacheKey.GET_USER_BY_ID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (user == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    user = userMapper.getById(id);
                    cacheService.set(RedisCacheKey.GET_USER_BY_ID + id, user);
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
    public List<User> getByPage(Integer pageNum, Integer pageSize) {
        List<User> list = cacheService.get(RedisCacheKey.GET_USER_BY_PAGE + pageNum); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = cacheService.get(RedisCacheKey.GET_USER_BY_PAGE + pageNum); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = userMapper.getByPage((pageNum - 1) * pageSize, pageSize);
                    cacheService.set(RedisCacheKey.GET_USER_BY_PAGE + pageNum, list);
                }
            }
        }
        return list;
    }

    /**
     * 获取用户总数量
     */
    public Long getTotalNum() {
        Long totalNum = cacheService.get(RedisCacheKey.GET_USER_TOTAL_NUM); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = cacheService.get(RedisCacheKey.GET_USER_TOTAL_NUM); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = userMapper.getTotalNum();
                    cacheService.set(RedisCacheKey.GET_USER_TOTAL_NUM, totalNum);
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
        User user = cacheService.get(RedisCacheKey.GET_USER_BY_USERNAME + userName); // 先读取缓存
        if (user == null) { // double check
            synchronized (this) {
                user = cacheService.get(RedisCacheKey.GET_USER_BY_USERNAME + userName); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (user == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    user = userMapper.getByUserName(userName);
                    cacheService.set(RedisCacheKey.GET_USER_BY_USERNAME + userName, user);
                }
            }
        }
        return user;
    }

}
