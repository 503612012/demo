package com.oven.core.finance.service;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.RedisCacheKey;
import com.oven.core.base.service.BaseService;
import com.oven.core.finance.dao.FinanceDao;
import com.oven.core.finance.vo.Finance;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 财务登记服务层
 *
 * @author Oven
 */
@Service
public class FinanceService extends BaseService {

    @Resource
    private FinanceDao financeDao;

    /**
     * 添加财务登记
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Finance finance) {
        finance.setCreateId(super.getCurrentUser().getId());
        finance.setLastModifyId(super.getCurrentUser().getId());
        finance.setCreateTime(new DateTime().toString(AppConst.TIME_PATTERN));
        finance.setLastModifyTime(new DateTime().toString(AppConst.TIME_PATTERN));
        finance.setOutMoney(0d);
        finance.setDelFlag(0);
        finance.setFinishFlag(1);
        financeDao.add(finance);
        // 移除缓存
        super.batchRemove(RedisCacheKey.FINANCE_PREFIX);
        // 记录日志
        super.addLog("添加财务登记", finance.toString());
    }

    /**
     * 分页查询财务登记
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Finance> getByPage(Integer pageNum, Integer pageSize, Finance finance) {
        List<Finance> list = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_PAGE, pageNum, pageSize, finance.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_PAGE, pageNum, pageSize, finance.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = financeDao.getByPage(pageNum, pageSize, finance);
                    super.set(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_PAGE, pageNum, pageSize, finance.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取财务登记总数量
     */
    public Integer getTotalNum(Finance finance) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_TOTAL_NUM, finance.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_TOTAL_NUM, finance.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = financeDao.getTotalNum(finance);
                    super.set(MessageFormat.format(RedisCacheKey.FINANCE_GET_TOTAL_NUM, finance.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 删除财务登记
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id) {
        Finance finance = financeDao.getById(id);
        boolean flag = financeDao.delete(id, 1, super.getCurrentUser().getId(), new DateTime().toString(AppConst.TIME_PATTERN)) > 0;
        if (flag) {
            // 移除缓存
            super.batchRemove(RedisCacheKey.FINANCE_PREFIX);
            // 记录日志
            super.addLog("删除财务登记", finance.toString());
        }
        return flag;
    }

    /**
     * 通过主键查询
     */
    public Finance getById(Integer id) {
        Finance finance = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_ID, id)); // 先读取缓存
        if (finance == null) { // double check
            synchronized (this) {
                finance = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (finance == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    finance = financeDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_ID, id), finance);
                }
            }
        }
        return finance;
    }

    /**
     * 通过工地获取
     */
    public Finance getByWorksiteId(Integer worksiteId) {
        Finance finance = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_WORKSITEID, worksiteId)); // 先读取缓存
        if (finance == null) { // double check
            synchronized (this) {
                finance = super.get(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_WORKSITEID, worksiteId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (finance == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    finance = financeDao.getByWorksiteId(worksiteId);
                    super.set(MessageFormat.format(RedisCacheKey.FINANCE_GET_BY_WORKSITEID, worksiteId), finance);
                }
            }
        }
        return finance;
    }

}
