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
@Transactional
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
    public void add(Workhour workhour) {
        workhour.setCreateId(super.getCurrentUser().getId());
        workhour.setCreateTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        workhourDao.add(workhour);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKHOUR_PREFIX);
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
                    Double payedWorkhour = Double.parseDouble(list.get(0));
                    Double totalWorkhour = Double.parseDouble(list.get(1));
                    proportion = payedWorkhour / totalWorkhour * 100;
                    super.set(RedisCacheKey.WORKHOUR_GET_WORKHOUR_PROPORTION, proportion);
                }
            }
        }
        return proportion;
    }

}
