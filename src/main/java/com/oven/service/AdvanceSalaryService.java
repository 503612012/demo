package com.oven.service;

import com.oven.constant.RedisCacheKey;
import com.oven.dao.AdvanceSalaryDao;
import com.oven.vo.AdvanceSalary;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 预支薪资服务层
 *
 * @author Oven
 */
@Service
public class AdvanceSalaryService extends BaseService {

    @Resource
    private AdvanceSalaryDao advanceSalaryDao;

    /**
     * 添加预支薪资
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(AdvanceSalary advanceSalary) {
        advanceSalary.setCreateId(super.getCurrentUser().getId());
        advanceSalaryDao.add(advanceSalary);
        // 移除缓存
        super.batchRemove(RedisCacheKey.ADVANCESALARY_PREFIX);
        // 记录日志
        super.addLog("添加预支薪资", advanceSalary.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

    /**
     * 分页查询预支薪资
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<AdvanceSalary> getByPage(Integer pageNum, Integer pageSize, AdvanceSalary advanceSalary) {
        List<AdvanceSalary> list = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_BY_PAGE, pageNum, pageSize, advanceSalary.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_BY_PAGE, pageNum, pageSize, advanceSalary.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = advanceSalaryDao.getByPage(pageNum, pageSize, advanceSalary);
                    super.set(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_BY_PAGE, pageNum, pageSize, advanceSalary.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取预支薪资总数量
     */
    public Integer getTotalNum(AdvanceSalary advanceSalary) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_TOTAL_NUM, advanceSalary.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_TOTAL_NUM, advanceSalary.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = advanceSalaryDao.getTotalNum(advanceSalary);
                    super.set(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_TOTAL_NUM, advanceSalary.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 删除预支薪资
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Integer id) {
        AdvanceSalary advanceSalary = advanceSalaryDao.getById(id);
        boolean flag = advanceSalaryDao.delete(id) > 0;
        if (flag) {
            // 移除缓存
            super.batchRemove(RedisCacheKey.ADVANCESALARY_PREFIX);
            // 记录日志
            super.addLog("删除预支薪资", advanceSalary.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        }
        return flag;
    }

    /**
     * 通过主键查询
     */
    public AdvanceSalary getById(Integer id) {
        AdvanceSalary advanceSalary = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_BY_ID, id)); // 先读取缓存
        if (advanceSalary == null) { // double check
            synchronized (this) {
                advanceSalary = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (advanceSalary == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    advanceSalary = advanceSalaryDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_BY_ID, id), advanceSalary);
                }
            }
        }
        return advanceSalary;
    }

    /**
     * 获取同比增长数据
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月，3传入的是年月日
     */
    public Double getAdvanceSalaryCompareProportion(String date, Integer dateType) {
        Double proportion = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_ADVANCE_SALARY_COMPARE_PROPORTION, date, dateType)); // 先读取缓存
        if (proportion == null) { // double check
            synchronized (this) {
                proportion = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_ADVANCE_SALARY_COMPARE_PROPORTION, date, dateType)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (proportion == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    Double thisAdvanceSalary = advanceSalaryDao.getAdvanceSalaryByDateAndDateType(date, dateType); // 本期薪资
                    if (dateType == 1) {
                        date = String.valueOf((Integer.parseInt(date) - 1));
                    } else if (dateType == 2) {
                        date = DateTime.parse(date).plusMonths(-1).toString("yyyy-MM");
                    }
                    Double preAdvanceSalary = advanceSalaryDao.getAdvanceSalaryByDateAndDateType(date, dateType); // 上期薪资
                    thisAdvanceSalary = (thisAdvanceSalary == null ? 0d : thisAdvanceSalary);
                    preAdvanceSalary = (preAdvanceSalary == null ? 0d : preAdvanceSalary);
                    if (preAdvanceSalary == 0) {
                        proportion = 0d;
                    } else {
                        proportion = ((thisAdvanceSalary - preAdvanceSalary) / preAdvanceSalary) * 100;
                    }
                    super.set(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_ADVANCE_SALARY_COMPARE_PROPORTION, date, dateType), proportion);
                }
            }
        }
        return proportion;
    }

    /**
     * 获取薪资信息
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月，3传入的是年月日
     */
    public Double getAdvanceSalaryByDateAndDateType(String date, Integer dateType) {
        Double advanceSalary = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_ADVANCE_SALARY_BY_DATE_DATETYPE, date, dateType)); // 先读取缓存
        if (advanceSalary == null) { // double check
            synchronized (this) {
                advanceSalary = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_ADVANCE_SALARY_BY_DATE_DATETYPE, date, dateType)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (advanceSalary == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    advanceSalary = advanceSalaryDao.getAdvanceSalaryByDateAndDateType(date, dateType); // 本期薪资
                    super.set(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_ADVANCE_SALARY_BY_DATE_DATETYPE, date, dateType), advanceSalary);
                }
            }
        }
        return advanceSalary;
    }

    /**
     * 获取员工未归还薪资总额
     */
    public Double getTotalAdvanceSalaryByEmployeeId(Integer employeeId) {
        Double totalAdvanceSalary = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_TOTAL_ADVANCE_SALARY_BY_EMPLOYEEID, employeeId)); // 先读取缓存
        if (totalAdvanceSalary == null) { // double check
            synchronized (this) {
                totalAdvanceSalary = super.get(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_TOTAL_ADVANCE_SALARY_BY_EMPLOYEEID, employeeId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalAdvanceSalary == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalAdvanceSalary = advanceSalaryDao.getTotalAdvanceSalaryByEmployeeId(employeeId); // 本期薪资
                    super.set(MessageFormat.format(RedisCacheKey.ADVANCESALARY_GET_TOTAL_ADVANCE_SALARY_BY_EMPLOYEEID, employeeId), totalAdvanceSalary);
                }
            }
        }
        return totalAdvanceSalary;
    }

}
