package com.oven.demo.core.wechatFund.service;

import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.core.base.service.BaseService;
import com.oven.demo.core.wechatFund.dao.WechatFundDao;
import com.oven.demo.core.wechatFund.vo.WechatFund;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Map;

/**
 * 微信基金服务层
 *
 * @author Oven
 */
@Service
public class WechatFundService extends BaseService {

    @Resource
    private WechatFundDao wechatFundDao;

    /**
     * 添加基金收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(WechatFund wechatFund) {
        wechatFundDao.add(wechatFund);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WECHAT_FUND_PREFIX);
        // 记录日志
        super.addLog("添加微信基金", wechatFund.toString());
    }

    /**
     * 获取微信基金报表
     */
    public Map<String, Object> getChartsData(String date) {
        Map<String, Object> result = super.get(MessageFormat.format(RedisCacheKey.WECHAT_FUND_GET_CHARTS_DATA, date)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.WECHAT_FUND_GET_CHARTS_DATA, date)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = wechatFundDao.getChartsData(date);
                    super.set(MessageFormat.format(RedisCacheKey.WECHAT_FUND_GET_CHARTS_DATA, date), result);
                }
            }
        }
        return result;
    }

    /**
     * 获取微信累计基金报表
     */
    public Map<String, Object> getTotalChartsData(String date) {
        Map<String, Object> result = super.get(MessageFormat.format(RedisCacheKey.WECHAT_FUND_GET_TOTAL_CHARTS_DATA, date)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.WECHAT_FUND_GET_TOTAL_CHARTS_DATA, date)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = wechatFundDao.getTotalChartsData(date);
                    super.set(MessageFormat.format(RedisCacheKey.WECHAT_FUND_GET_TOTAL_CHARTS_DATA, date), result);
                }
            }
        }
        return result;
    }

}
