package com.oven.service;

import com.oven.constant.RedisCacheKey;
import com.oven.dao.PayDao;
import com.oven.vo.Workhour;
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
@Transactional
public class PayService extends BaseService {

    @Resource
    private PayDao payDao;

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
    public void doPay(String workhourIds) {
        payDao.doPay(workhourIds);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKHOUR_PREFIX);
    }

}
