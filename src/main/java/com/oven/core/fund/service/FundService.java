package com.oven.core.fund.service;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.RedisCacheKey;
import com.oven.core.base.service.BaseService;
import com.oven.core.fund.dao.FundDao;
import com.oven.core.fund.vo.Fund;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 基金服务层
 *
 * @author Oven
 */
@Service
public class FundService extends BaseService {

    @Resource
    private FundDao fundDao;

    /**
     * 添加基金
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Fund fund) {
        fund.setCreateId(super.getCurrentUser().getId());
        fund.setCreateTime(new DateTime().toString(AppConst.TIME_PATTERN));
        fundDao.add(fund);
        // 移除缓存
        super.batchRemove(RedisCacheKey.FUND_PREFIX);
        // 记录日志
        super.addLog("添加基金", fund.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Fund fund) {
        Fund fundInDb = this.getById(fund.getId());
        String fundName = fundInDb.getFundName();
        StringBuilder content = new StringBuilder();
        if (!fundInDb.getFundName().equals(fund.getFundName())) {
            content.append("名称由[").append(fundInDb.getFundName()).append("]改为[").append(fund.getFundName()).append("]，");
            fundInDb.setFundName(fund.getFundName());
        }
        if (!StringUtils.isEmpty(fund.getStatus())) {
            if (!fundInDb.getStatus().equals(fund.getStatus())) {
                content.append("状态由[").append(fundInDb.getStatus() == 0 ? "正常" : "锁定").append("]改为[").append(fund.getStatus() == 0 ? "正常" : "锁定").append("]，");
                fundInDb.setStatus(fund.getStatus());
            }
        }
        String str = content.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            fundDao.update(fundInDb);
            // 移除缓存
            super.batchRemove(RedisCacheKey.FUND_PREFIX);
            // 记录日志
            super.addLog("修改基金", "[" + fundName + "]" + str, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
    }

    /**
     * 通过主键查询
     */
    public Fund getById(Integer id) {
        Fund fund = super.get(MessageFormat.format(RedisCacheKey.FUND_GET_BY_ID, id)); // 先读取缓存
        if (fund == null) { // double check
            synchronized (this) {
                fund = super.get(MessageFormat.format(RedisCacheKey.FUND_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (fund == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    fund = fundDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.FUND_GET_BY_ID, id), fund);
                }
            }
        }
        return fund;
    }

    /**
     * 分页查询基金
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Fund> getByPage(Integer pageNum, Integer pageSize, String fundName) {
        List<Fund> list = super.get(MessageFormat.format(RedisCacheKey.FUND_GET_BY_PAGE, pageNum, pageSize, fundName)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.FUND_GET_BY_PAGE, pageNum, pageSize, fundName)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = fundDao.getByPage(pageNum, pageSize, fundName);
                    super.set(MessageFormat.format(RedisCacheKey.FUND_GET_BY_PAGE, pageNum, pageSize, fundName), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取基金总数量
     */
    public Integer getTotalNum(String fundName) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.FUND_GET_TOTAL_NUM, fundName)); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.FUND_GET_TOTAL_NUM, fundName)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = fundDao.getTotalNum(fundName);
                    super.set(MessageFormat.format(RedisCacheKey.FUND_GET_TOTAL_NUM, fundName), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 删除基金
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id) {
        Fund fund = this.getById(id);
        boolean flag = fundDao.delete(id) > 0;
        if (flag) {
            // 移除缓存
            super.batchRemove(RedisCacheKey.FUND_PREFIX);
            // 记录日志
            super.addLog("删除基金", fund.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
        return flag;
    }

    /**
     * 获取所有基金
     */
    public List<Fund> getAll() {
        List<Fund> list = super.get(RedisCacheKey.FUND_GET_ALL); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.FUND_GET_ALL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = fundDao.getAll();
                    super.set(RedisCacheKey.FUND_GET_ALL, list);
                }
            }
        }
        return list;
    }

    /**
     * 修改基金排序
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrder(Integer fundId, Integer order) {
        Fund fund = this.getById(fundId);
        fundDao.updateOrder(fundId, order);
        // 移除缓存
        super.batchRemove(RedisCacheKey.FUND_PREFIX);
        // 记录日志
        super.addLog("修改基金排序", fund.getFundName() + "的排序值由【" + fund.getOrder() + "】改为【" + order + "】", super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

}
