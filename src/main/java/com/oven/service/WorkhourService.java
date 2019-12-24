package com.oven.service;

import com.oven.constant.RedisCacheKey;
import com.oven.dao.WorkhourDao;
import com.oven.vo.Workhour;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 工时服务层
 *
 * @author Oven
 */
@Service
public class WorkhourService extends BaseService {

    @Resource
    private WorkhourDao workhourDao;

    /**
     * 分页查询工时
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Workhour> getByPage(Integer pageNum, Integer pageSize, Workhour workhour) {
        List<Workhour> list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_PAGE, pageNum, pageSize, workhour.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_PAGE, pageNum, pageSize, workhour.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = workhourDao.getByPage(pageNum, pageSize, workhour);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_PAGE, pageNum, pageSize, workhour.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取工时总数量
     */
    public Integer getTotalNum(Workhour workhour) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_TOTAL_NUM, workhour.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_TOTAL_NUM, workhour.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = workhourDao.getTotalNum(workhour);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_TOTAL_NUM, workhour.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Workhour workhour) {
        workhour.setCreateId(super.getCurrentUser().getId());
        workhour.setCreateTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        workhourDao.add(workhour);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKHOUR_PREFIX, RedisCacheKey.PAYRECORD_PREFIX);
        // 记录日志
        super.addLog("录入工时", workhour.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

    /**
     * 通过主键查询
     */
    public Workhour getById(Integer id) {
        Workhour workhour = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_ID, id)); // 先读取缓存
        if (workhour == null) { // double check
            synchronized (this) {
                workhour = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (workhour == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    workhour = workhourDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_ID, id), workhour);
                }
            }
        }
        return workhour;
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Workhour workhour = this.getById(id);
        workhourDao.delete(id);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKHOUR_PREFIX);
        // 记录日志
        super.addLog("删除工时", workhour.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
    }

    /**
     * 判断该员工该日期是否有录入过
     */
    public Workhour isInputed(Integer employeeId, String workDate, Integer worksiteId) {
        Workhour workhour = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_EMPLOYEEID_AND_WORKDATE_AND_WORKSITEID, employeeId, workDate, worksiteId)); // 先读取缓存
        if (workhour == null) { // double check
            synchronized (this) {
                workhour = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_EMPLOYEEID_AND_WORKDATE_AND_WORKSITEID, employeeId, workDate, worksiteId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (workhour == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    workhour = workhourDao.isInputed(employeeId, workDate, worksiteId);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_BY_EMPLOYEEID_AND_WORKDATE_AND_WORKSITEID, employeeId, workDate, worksiteId), workhour);
                }
            }
        }
        return workhour;
    }

    /**
     * 获取总工时
     */
    public Double getTotalWorkhour() {
        Double totalWorkhour = super.get(RedisCacheKey.WORKHOUR_GET_TOTAL_WORKHOUR); // 先读取缓存
        if (totalWorkhour == null) { // double check
            synchronized (this) {
                totalWorkhour = super.get(RedisCacheKey.WORKHOUR_GET_TOTAL_WORKHOUR); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalWorkhour == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalWorkhour = workhourDao.getTotalWorkhour();
                    super.set(RedisCacheKey.WORKHOUR_GET_TOTAL_WORKHOUR, totalWorkhour);
                }
            }
        }
        return totalWorkhour;
    }

    /**
     * 获取已发薪资工时占比
     */
    public Double getWorkhourProportion() {
        Double proportion = super.get(RedisCacheKey.WORKHOUR_GET_WORKHOUR_PROPORTION); // 先读取缓存
        if (proportion == null) { // double check
            synchronized (this) {
                proportion = super.get(RedisCacheKey.WORKHOUR_GET_WORKHOUR_PROPORTION); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (proportion == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    List<String> list = workhourDao.getWorkhourProportion();
                    if (CollectionUtils.isEmpty(list) || list.size() < 2) {
                        return 100d;
                    }
                    Double payedWorkhour = list.get(0) == null ? 0d : Double.parseDouble(list.get(0));
                    Double totalWorkhour = list.get(1) == null ? 0d : Double.parseDouble(list.get(1));
                    if (totalWorkhour == 0) {
                        return 100d;
                    }
                    proportion = payedWorkhour / totalWorkhour * 100;
                    super.set(RedisCacheKey.WORKHOUR_GET_WORKHOUR_PROPORTION, proportion);
                }
            }
        }
        return proportion;
    }

    /**
     * 获取一个员工未发薪资的所有工时
     */
    public List<Workhour> getUnPayByEmployeeId(Integer employeeId) {
        List<Workhour> list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_UN_PAY_BY_EMPLOYEEID, employeeId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_UN_PAY_BY_EMPLOYEEID, employeeId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = workhourDao.getUnPayByEmployeeId(employeeId);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_UN_PAY_BY_EMPLOYEEID, employeeId), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取同比增长数据
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月，3传入的是年月日
     * @param dataType 获取数据类型，in获取录入薪资，out获取发放薪资
     */
    public Double getSalaryCompareProportion(String date, Integer dateType, String dataType) {
        Double proportion = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_SALARY_COMPARE_PROPORTION, date, dateType, dataType)); // 先读取缓存
        if (proportion == null) { // double check
            synchronized (this) {
                proportion = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_SALARY_COMPARE_PROPORTION, date, dateType, dataType)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (proportion == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    Double thisSalary = workhourDao.getSalaryByDateAndDateType(date, dateType, dataType); // 本期薪资
                    if (dateType == 1) {
                        date = String.valueOf((Integer.parseInt(date) - 1));
                    } else if (dateType == 2) {
                        date = DateTime.parse(date).plusMonths(-1).toString("yyyy-MM");
                    }
                    Double preSalary = workhourDao.getSalaryByDateAndDateType(date, dateType, dataType); // 上期薪资
                    thisSalary = (thisSalary == null ? 0d : thisSalary);
                    preSalary = (preSalary == null ? 0d : preSalary);
                    if (preSalary == 0) {
                        proportion = 0d;
                    } else {
                        proportion = ((thisSalary - preSalary) / preSalary) * 100;
                    }
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_SALARY_COMPARE_PROPORTION, date, dateType, dataType), proportion);
                }
            }
        }
        return proportion;
    }

    /**
     * 获取薪资统计图表X轴数据
     */
    public List<String> getCategories(String date, Integer dateType) {
        return workhourDao.getCategories(date, dateType);
    }

    /**
     * 获取薪资信息
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月，3传入的是年月日
     * @param dataType 获取数据类型，in获取录入薪资，out获取发放薪资
     */
    public Double getSalaryByDateAndDateType(String date, Integer dateType, String dataType) {
        Double salary = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_SALARY_BY_DATE_AND_DATETYPE, date, dateType, dataType)); // 先读取缓存
        if (salary == null) { // double check
            synchronized (this) {
                salary = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_SALARY_BY_DATE_AND_DATETYPE, date, dateType, dataType)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (salary == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    salary = workhourDao.getSalaryByDateAndDateType(date, dateType, dataType);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_SALARY_BY_DATE_AND_DATETYPE, date, dateType, dataType), salary);
                }
            }
        }
        return salary;
    }

    /**
     * 获取薪资统计图表X轴数据-工时报表
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年，2传入的是年月
     */
    public List<String> getCategoriesForWorkhour(String date, Integer dateType, Integer employeeId) {
        return workhourDao.getCategoriesForWorkhour(date, dateType, employeeId);
    }

    /**
     * 获取薪资信息
     *
     * @param date     查询数据日期
     * @param dateType 日期类型，1传入的年月，2传入的是年月日
     */
    public Double getWorkhourByDateAndDateType(String date, Integer dateType, Integer employeeId) {
        Double workhour = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKHOUR_BY_DATE_AND_DATETYPE, date, dateType, employeeId)); // 先读取缓存
        if (workhour == null) { // double check
            synchronized (this) {
                workhour = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKHOUR_BY_DATE_AND_DATETYPE, date, dateType, employeeId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (workhour == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    workhour = workhourDao.getWorkhourByDateAndDateType(date, dateType, employeeId);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKHOUR_BY_DATE_AND_DATETYPE, date, dateType, employeeId), workhour);
                }
            }
        }
        return workhour;
    }

    /**
     * 通过工地获取
     */
    public List<Workhour> getByWorksiteId(Integer worksiteId) {
        List<Workhour> list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKSITEID, worksiteId)); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKSITEID, worksiteId)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = workhourDao.getByWorksiteId(worksiteId);
                    super.set(MessageFormat.format(RedisCacheKey.WORKHOUR_GET_WORKSITEID, worksiteId), list);
                }
            }
        }
        return list;
    }

}
