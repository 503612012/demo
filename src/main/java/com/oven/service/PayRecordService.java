package com.oven.service;

import com.oven.constant.RedisCacheKey;
import com.oven.dao.PayRecordDao;
import com.oven.vo.PayRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 薪资发放记录服务层
 *
 * @author Oven
 */
@Service
@Transactional
public class PayRecordService extends BaseService {

    @Resource
    private PayRecordDao payRecordDao;

    /**
     * 添加
     */
    public void add(PayRecord payRecord) {
        payRecordDao.add(payRecord);
        // 移除缓存
        super.batchRemove(RedisCacheKey.PAYRECORD_PREFIX);
    }

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

}
