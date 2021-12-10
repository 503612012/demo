package com.oven.demo.core.payRecord.service;

import com.oven.demo.common.constant.RedisCacheKey;
import com.oven.demo.core.base.service.BaseService;
import com.oven.demo.core.payRecord.dao.PayRecordDao;
import com.oven.demo.core.payRecord.vo.PayRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * 薪资发放记录服务层
 *
 * @author Oven
 */
@Service
public class PayRecordService extends BaseService {

    @Resource
    private PayRecordDao payRecordDao;

    /**
     * 分页查询发薪记录
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<PayRecord> getByPage(Integer pageNum, Integer pageSize, String employeeName) {
        List<PayRecord> list = super.get(MessageFormat.format(RedisCacheKey.PAYRECORD_GET_BY_PAGE, pageNum, pageSize, employeeName)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.PAYRECORD_GET_BY_PAGE, pageNum, pageSize, employeeName)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = payRecordDao.getByPage(pageNum, pageSize, employeeName);
                    super.set(MessageFormat.format(RedisCacheKey.PAYRECORD_GET_BY_PAGE, pageNum, pageSize, employeeName), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取发薪记录总数量
     */
    public Integer getTotalNum(String employeeName) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.PAYRECORD_GET_TOTAL_NUM, employeeName)); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.PAYRECORD_GET_TOTAL_NUM, employeeName)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = payRecordDao.getTotalNum(employeeName);
                    super.set(MessageFormat.format(RedisCacheKey.PAYRECORD_GET_TOTAL_NUM, employeeName), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 获取总发薪金额
     */
    public Double getTotalPay() {
        Double totalPay = super.get(RedisCacheKey.PAYRECORD_GET_TOTAL_PAY); // 先读取缓存
        if (totalPay == null) { // double check
            synchronized (this) {
                totalPay = super.get(RedisCacheKey.PAYRECORD_GET_TOTAL_PAY); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalPay == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalPay = payRecordDao.getTotalPay();
                    super.set(RedisCacheKey.PAYRECORD_GET_TOTAL_PAY, totalPay);
                }
            }
        }
        return totalPay;
    }

    /**
     * 获取薪资排行前五
     */
    public List<Map<String, Object>> getSalaryTopFive() {
        List<Map<String, Object>> list = super.get(RedisCacheKey.PAYRECORD_GET_SALARY_TOP_FIVE); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.PAYRECORD_GET_SALARY_TOP_FIVE); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = payRecordDao.getSalaryTopFive();
                    super.set(RedisCacheKey.PAYRECORD_GET_SALARY_TOP_FIVE, list);
                }
            }
        }
        return list;
    }

    /**
     * 获取已发薪资占比
     */
    public Double getSalaryProportion() {
        Double proportion = super.get(RedisCacheKey.PAYRECORD_GET_SALARY_PROPORTION); // 先读取缓存
        if (proportion == null) { // double check
            synchronized (this) {
                proportion = super.get(RedisCacheKey.PAYRECORD_GET_SALARY_PROPORTION); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (proportion == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    List<String> list = payRecordDao.getSalaryProportion();
                    if (CollectionUtils.isEmpty(list) || list.size() < 2) {
                        return 100d;
                    }
                    double payedSalary = list.get(0) == null ? 0d : Double.parseDouble(list.get(0));
                    double totalSalary = list.get(1) == null ? 0d : Double.parseDouble(list.get(1));
                    if (totalSalary == 0) {
                        return 100d;
                    }
                    proportion = payedSalary / totalSalary * 100;
                    super.set(RedisCacheKey.PAYRECORD_GET_SALARY_PROPORTION, proportion);
                }
            }
        }
        return proportion;
    }

}
