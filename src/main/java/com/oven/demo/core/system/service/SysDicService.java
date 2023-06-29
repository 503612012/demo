package com.oven.demo.core.system.service;

import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.common.redis.IRedisService;
import com.oven.demo.common.service.BaseService;
import com.oven.demo.core.system.dao.SysDicDao;
import com.oven.demo.core.system.entity.SysDicEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统级字典Service层
 *
 * @author Oven
 */
@Service
public class SysDicService extends BaseService {

    @Resource
    private SysDicDao sysDicDao;
    @Resource
    private IRedisService redisService;

    /**
     * 模拟秒杀接口
     */
    public void secKill() {
        // 开始多线程去秒杀
        for (int i = 0; i < 50; i++) {
            int thread_index = i;
            new Thread(() -> {
                for (int n = 0; n < 30; n++) {
                    doSecKill(thread_index);
                }
            }).start();
        }
    }

    /**
     * 开始秒杀
     */
    private void doSecKill(int thread_index) {
        // 获取分布式锁
        String lockId = redisService.acquireLock("SEC_KILL_LOCK", TimeUnit.MINUTES.toSeconds(30));
        try {
            while (StringUtils.isEmpty(lockId)) {
                TimeUnit.MILLISECONDS.sleep(100); // 等待100毫秒后重新获取分布式锁
                lockId = redisService.acquireLock("SEC_KILL_LOCK", TimeUnit.MINUTES.toSeconds(30));
            }
            reduceNum(thread_index); // 生成订单
        } catch (Exception e) {
            System.out.println("秒杀活动异常！");
            e.printStackTrace();
        } finally {
            // 释放分布式锁
            redisService.releaseLock("SEC_KILL_LOCK", lockId);
        }
    }

    /**
     * 生成订单
     */
    private void reduceNum(int thread_index) {
        try {
            // 获取库存剩余量
            int num = Integer.parseInt(sysDicDao.getByKey("secKill").getValue());
            if (num > 0) { // 库存还有
                System.out.println(thread_index + "，当前库存：" + num + "，线程【" + thread_index + "】售出一件！");
                sysDicDao.reduceNum();
            } else {
                System.out.println("库存不足！");
            }
        } catch (Exception e) {
            System.out.println("生成订单异常！");
            e.printStackTrace();
        }
    }

    /**
     * 查询所有
     */
    public List<SysDicEntity> getAll() {
        List<SysDicEntity> list = super.get(RedisCacheKey.SYS_DIC_FIND_ALL); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.SYS_DIC_FIND_ALL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = sysDicDao.getAll();
                    super.set(RedisCacheKey.SYS_DIC_FIND_ALL, list);
                }
            }
        }
        return list;
    }

    /**
     * 添加数据字典
     */
    public void save(SysDicEntity sysDic) throws Exception {
        sysDicDao.save(sysDic);
        // 移除缓存
        super.remove(RedisCacheKey.SYS_DIC_FIND_ALL);
    }

    /**
     * 更新
     */
    public void update(SysDicEntity sysDic) {
        sysDicDao.update(sysDic);
        // 移除缓存
        super.remove(RedisCacheKey.SYS_DIC_FIND_ALL);
        super.remove(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_ID, sysDic.getId()));
        super.remove(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_KEY, sysDic.getKey()));
    }

    /**
     * 通过主键查询
     */
    public SysDicEntity getById(Integer id) {
        SysDicEntity result = super.get(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_ID, id)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = sysDicDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_ID, id), result);
                }
            }
        }
        return result;
    }

    /**
     * 分页查询数据字典
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<SysDicEntity> getByPage(Integer pageNum, Integer pageSize, SysDicEntity sysDic) {
        return sysDicDao.getByPage(pageNum, pageSize, sysDic);
    }

    /**
     * 获取数据字典总数量
     */
    public Integer getTotalNum(SysDicEntity sysDic) {
        return sysDicDao.getTotalNum(sysDic);
    }

    /**
     * 删除数据字典
     */
    public boolean delete(Integer id) throws Exception {
        boolean flag = sysDicDao.delete(id) > 0;
        if (flag) {
            // 移除缓存
            super.remove(RedisCacheKey.SYS_DIC_FIND_ALL);
            super.remove(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_ID, id));
        }
        return flag;
    }

    /**
     * 修改状态
     */
    public void updateStatus(Integer id, Integer status) {
        sysDicDao.updateStatus(id, status);
        // 移除缓存
        super.remove(RedisCacheKey.SYS_DIC_FIND_ALL);
        super.remove(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_ID, id));
    }

    /**
     * 根据key查询
     */
    public SysDicEntity getByKey(String key) {
        SysDicEntity result = super.get(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_KEY, key)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_KEY, key)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = sysDicDao.getByKey(key);
                    super.set(MessageFormat.format(RedisCacheKey.SYS_DIC_FIND_BY_KEY, key), result);
                }
            }
        }
        return result;
    }

}
