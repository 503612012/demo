package com.oven.service;

import com.oven.constant.RedisCacheKey;
import com.oven.dao.AdvanceSalaryDao;
import com.oven.dao.FinanceDao;
import com.oven.dao.PayDao;
import com.oven.dao.PayRecordDao;
import com.oven.enumerate.ResultEnum;
import com.oven.exception.DoPayException;
import com.oven.vo.AdvanceSalary;
import com.oven.vo.Finance;
import com.oven.vo.PayRecord;
import com.oven.vo.Workhour;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 薪资发放服务层
 *
 * @author Oven
 */
@Service
public class PayService extends BaseService {

    @Resource
    private PayDao payDao;
    @Resource
    private FinanceDao financeDao;
    @Resource
    private PayRecordDao payRecordDao;
    @Resource
    private AdvanceSalaryDao advanceSalaryDao;

    /**
     * 获取员工未发放的薪资的工时
     */
    public List<Workhour> getWorkhourData(Integer employeeId, Integer worksiteId) {
        List<Workhour> list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKHOUR_BY_EMPLOYEEID_AND_WORKSITEID, employeeId, worksiteId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKHOUR_BY_EMPLOYEEID_AND_WORKSITEID, employeeId, worksiteId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = payDao.getWorkhourData(employeeId, worksiteId);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKHOUR_BY_EMPLOYEEID_AND_WORKSITEID, employeeId, worksiteId), list);
                }
            }
        }
        return list;
    }

    /**
     * 下发薪资
     */
    @Transactional(rollbackFor = DoPayException.class)
    public String doPay(String workhourIds, Integer employeeId, Integer totalHour, Double totalMoney, String remark, Integer worksiteId, Integer hasModifyMoney, Double changeMoney) throws DoPayException {
        payDao.doPay(workhourIds);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKHOUR_PREFIX);
        // 记录日志
        super.addLog("工时标记为已发薪", workhourIds, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());

        // 发放金额中扣除预支薪资
        List<AdvanceSalary> advanceSalaries = advanceSalaryDao.getByEmployeeId(employeeId);
        Double totalAdvanceSalary = 0d; // 总预支金额
        if (advanceSalaries != null && advanceSalaries.size() > 0) {
            for (AdvanceSalary advanceSalary : advanceSalaries) {
                if (advanceSalary.getHasRepay() == 1) { // 没有归还
                    totalAdvanceSalary += advanceSalary.getMoney();
                }
            }
        }
        if (totalAdvanceSalary > totalMoney) {
            throw new DoPayException(ResultEnum.DOPAY_ADVANCE_SALARY_OVER_PAY_SALARY.getCode(), ResultEnum.DOPAY_ADVANCE_SALARY_OVER_PAY_SALARY.getValue(), new Exception());
        }
        totalMoney = totalMoney - totalAdvanceSalary;
        advanceSalaryDao.backAdvanceSalaryByEmployeeId(employeeId);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ADVANCESALARY_PREFIX);
        // 记录日志
        super.addLog("预支薪资标记为已归还", "员工ID" + employeeId, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());

        // 从工地资金中扣除
        Finance finance = null;
        List<Finance> list = financeDao.getByWorksiteId(worksiteId);
        if (list != null && list.size() > 0) {
            for (Finance item : list) {
                if (item.getDelFlag() == 0) {
                    finance = item;
                }
            }
        }
        if (finance == null) {
            throw new DoPayException(ResultEnum.DOPAY_NO_WORKSITE_SALARY.getCode(), ResultEnum.DOPAY_NO_WORKSITE_SALARY.getValue(), new Exception());
        }
        if (finance.getMoney() < (totalMoney + finance.getOutMoney())) {
            throw new DoPayException(ResultEnum.DOPAY_TOTAL_SALARY_OVER_PAY_SALARY.getCode(), MessageFormat.format(ResultEnum.DOPAY_TOTAL_SALARY_OVER_PAY_SALARY.getValue(), (finance.getMoney() - finance.getOutMoney())), new Exception());
        }
        Double oldOutMoney = finance.getOutMoney();
        finance.setOutMoney((finance.getMoney() == null ? 0d : finance.getOutMoney()) + totalMoney);
        financeDao.updateOutMoney(finance);
        // 移除缓存
        super.batchRemove(RedisCacheKey.FINANCE_PREFIX);
        // 记录日志
        super.addLog("更新财务支出金额", "【" + oldOutMoney + "】改为【" + finance.getOutMoney() + "】", super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());

        // 保存发薪记录
        PayRecord payRecord = new PayRecord();
        payRecord.setPayerId(super.getCurrentUser().getId());
        payRecord.setEmployeeId(employeeId);
        payRecord.setPayDate(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        payRecord.setTotalHour(totalHour);
        payRecord.setTotalMoney(totalMoney);
        payRecord.setWorkhourIds(workhourIds);
        payRecord.setRemark(remark);
        payRecord.setHasModifyMoney(hasModifyMoney);
        payRecord.setChangeMoney(changeMoney);
        payRecordDao.add(payRecord);
        // 移除缓存
        super.batchRemove(RedisCacheKey.PAYRECORD_PREFIX);
        // 记录日志
        super.addLog("添加发薪记录", payRecord.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        return "";
    }

}
