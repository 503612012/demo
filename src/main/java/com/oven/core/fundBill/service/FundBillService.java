package com.oven.core.fundBill.service;

import com.oven.common.constant.RedisCacheKey;
import com.oven.core.base.service.BaseService;
import com.oven.core.fundBill.dao.FundBillDao;
import com.oven.core.fundBill.vo.FundBill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 基金账单服务层
 *
 * @author Oven
 */
@Service
public class FundBillService extends BaseService {

    @Resource
    private FundBillDao fundBillDao;

    /**
     * 通过主键查询
     */
    public FundBill getById(Integer id) {
        FundBill fundBill = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_BY_ID, id)); // 先读取缓存
        if (fundBill == null) { // double check
            synchronized (this) {
                fundBill = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (fundBill == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    fundBill = fundBillDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_BY_ID, id), fundBill);
                }
            }
        }
        return fundBill;
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(FundBill fundBill) {
        FundBill fundBillInDb = this.getById(fundBill.getId());
        String fundName = fundBillInDb.getFundName();
        StringBuilder content = new StringBuilder();
        if (!fundBillInDb.getFundName().equals(fundBill.getFundName())) {
            content.append("基金名称由[").append(fundBillInDb.getFundName()).append("]改为[").append(fundBill.getFundName()).append("]，");
            fundBillInDb.setFundId(fundBill.getFundId());
            fundBillInDb.setFundName(fundBill.getFundName());
        }
        if (!fundBillInDb.getDataDate().equals(fundBill.getDataDate())) {
            content.append("收益日期由[").append(fundBillInDb.getDataDate()).append("]改为[").append(fundBill.getDataDate()).append("]，");
            fundBillInDb.setDataDate(fundBill.getDataDate());
        }
        if (!fundBillInDb.getMoney().equals(fundBill.getMoney())) {
            content.append("收益金额由[").append(fundBillInDb.getMoney()).append("]改为[").append(fundBill.getMoney()).append("]，");
            fundBillInDb.setMoney(fundBill.getMoney());
        }
        String str = content.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            fundBillDao.update(fundBillInDb);
            // 移除缓存
            super.batchRemove(RedisCacheKey.FUNDBILL_PREFIX);
            // 记录日志
            super.addLog("修改基金", "[" + fundName + "]" + str, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
    }

    /**
     * 分页查询基金收益
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<FundBill> getByPage(Integer pageNum, Integer pageSize, String fundName, String date) {
        List<FundBill> list = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_BY_PAGE, pageNum, pageSize, fundName, date)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_BY_PAGE, pageNum, pageSize, fundName, date)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = fundBillDao.getByPage(pageNum, pageSize, fundName, date);
                    super.set(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_BY_PAGE, pageNum, pageSize, fundName, date), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取基金总数量
     */
    public Integer getTotalNum(String fundName, String date) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_TOTAL_NUM, fundName, date)); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_TOTAL_NUM, fundName, date)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = fundBillDao.getTotalNum(fundName, date);
                    super.set(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_TOTAL_NUM, fundName, date), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 删除基金收益
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id) {
        FundBill fundBill = this.getById(id);
        boolean flag = fundBillDao.delete(id) > 0;
        if (flag) {
            // 移除缓存
            super.batchRemove(RedisCacheKey.FUNDBILL_PREFIX);
            // 记录日志
            super.addLog("删除基金收益", fundBill.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
        return flag;
    }

    /**
     * 添加基金收益
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(FundBill fundBill) {
        fundBillDao.add(fundBill);
        // 移除缓存
        super.batchRemove(RedisCacheKey.FUNDBILL_PREFIX);
        // 记录日志
        super.addLog("添加基金账单", fundBill.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

    /**
     * 获取基金收益
     *
     * @param dateType 日期类型：1按月、2按天
     */
    public Map<String, Object> getChartsData(String date, Integer dateType, Integer fundId) {
        Map<String, Object> result = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_CHARTS_DATA, date, dateType, fundId)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_CHARTS_DATA, date, dateType, fundId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = fundBillDao.getChartsData(date, dateType, fundId);
                    super.set(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_CHARTS_DATA, date, dateType, fundId), result);
                }
            }
        }
        return result;
    }

    /**
     * 获取累计收益
     *
     * @param dateType 日期类型：1按月、2按天
     */
    public Double getCurrentDayTotalByDate(String date, Integer dateType) {
        Double result = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_CURRENT_DAY_TOTAL_BY_DATE, date, dateType)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_CURRENT_DAY_TOTAL_BY_DATE, date, dateType)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = fundBillDao.getCurrentDayTotalByDate(date, dateType);
                    super.set(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_CURRENT_DAY_TOTAL_BY_DATE, date, dateType), result);
                }
            }
        }
        return result;
    }

    /**
     * 获取全部累计
     */
    public Double getTotal() {
        Double result = super.get(RedisCacheKey.FUNDBILL_GET_TOTAL); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(RedisCacheKey.FUNDBILL_GET_TOTAL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = fundBillDao.getTotal();
                    super.set(RedisCacheKey.FUNDBILL_GET_TOTAL, result);
                }
            }
        }
        return result;
    }

    /**
     * 获取持有收益
     */
    public Double getTotalByDate(String date, Integer dateType) {
        Double result = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_TOTAL_BY_DATE, date, dateType)); // 先读取缓存
        if (result == null) { // double check
            synchronized (this) {
                result = super.get(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_TOTAL_BY_DATE, date, dateType)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (result == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    result = fundBillDao.getTotalByDate(date, dateType);
                    super.set(MessageFormat.format(RedisCacheKey.FUNDBILL_GET_TOTAL_BY_DATE, date, dateType), result);
                }
            }
        }
        return result;
    }

}
