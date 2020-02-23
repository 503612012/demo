package com.oven.service;

import com.oven.dao.SysDicDao;
import com.oven.redis.IRedisService;
import com.oven.vo.SysDicVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统级字典Service层
 *
 * @author Oven
 */
@Service
public class SysDicService {

    @Resource
    private SysDicDao sysDicDao;
    @Resource
    private IRedisService redisService;


    /**
     * 查询所有
     */
    public List<SysDicVo> findAll() {
        return sysDicDao.findAll();
    }

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
            int num = Integer.parseInt(sysDicDao.getByKey("secKill"));
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

}
