package com.oven.core.worksite.service;

import com.oven.common.constant.AppConst;
import com.oven.common.constant.RedisCacheKey;
import com.oven.common.util.CommonUtils;
import com.oven.core.base.service.BaseService;
import com.oven.core.worksite.dao.WorksiteDao;
import com.oven.core.worksite.vo.Worksite;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.List;

/**
 * 工地服务层
 *
 * @author Oven
 */
@Service
public class WorksiteService extends BaseService {

    @Resource
    private WorksiteDao worksiteDao;

    /**
     * 分页查询工地
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Worksite> getByPage(Integer pageNum, Integer pageSize, Worksite worksite) {
        List<Worksite> list = super.get(MessageFormat.format(RedisCacheKey.WORKSITE_GET_BY_PAGE, pageNum, pageSize, worksite.toString())); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(MessageFormat.format(RedisCacheKey.WORKSITE_GET_BY_PAGE, pageNum, pageSize, worksite.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = worksiteDao.getByPage(pageNum, pageSize, worksite);
                    super.set(MessageFormat.format(RedisCacheKey.WORKSITE_GET_BY_PAGE, pageNum, pageSize, worksite.toString()), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取工地总数量
     */
    public Integer getTotalNum(Worksite worksite) {
        Integer totalNum = super.get(MessageFormat.format(RedisCacheKey.WORKSITE_GET_TOTAL_NUM, worksite.toString())); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(MessageFormat.format(RedisCacheKey.WORKSITE_GET_TOTAL_NUM, worksite.toString())); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = worksiteDao.getTotalNum(worksite);
                    super.set(MessageFormat.format(RedisCacheKey.WORKSITE_GET_TOTAL_NUM, worksite.toString()), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 获取所有工地
     */
    public Object getAll() {
        List<Worksite> list = super.get(RedisCacheKey.WORKSITE_GET_ALL); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.WORKSITE_GET_ALL); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = worksiteDao.getAll();
                    super.set(RedisCacheKey.WORKSITE_GET_ALL, list);
                }
            }
        }
        return list;
    }

    /**
     * 添加
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Worksite worksite) {
        worksite.setCreateId(CommonUtils.getCurrentUser().getId());
        worksite.setCreateTime(new DateTime().toString(AppConst.TIME_PATTERN));
        worksite.setLastModifyId(CommonUtils.getCurrentUser().getId());
        worksite.setLastModifyTime(new DateTime().toString(AppConst.TIME_PATTERN));
        worksiteDao.add(worksite);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKSITE_PREFIX);
        // 记录日志
        super.addLog("添加工地", worksite.toString());
    }

    /**
     * 通过主键查询
     */
    public Worksite getById(Integer id) {
        Worksite worksite = super.get(MessageFormat.format(RedisCacheKey.WORKSITE_GET_BY_ID, id)); // 先读取缓存
        if (worksite == null) { // double check
            synchronized (this) {
                worksite = super.get(MessageFormat.format(RedisCacheKey.WORKSITE_GET_BY_ID, id)); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (worksite == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    worksite = worksiteDao.getById(id);
                    super.set(MessageFormat.format(RedisCacheKey.WORKSITE_GET_BY_ID, id), worksite);
                }
            }
        }
        return worksite;
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(Worksite worksite) {
        Worksite worksiteInDb = worksiteDao.getById(worksite.getId());
        String name = worksiteInDb.getName();
        StringBuilder content = new StringBuilder();
        if (!worksiteInDb.getName().equals(worksite.getName())) {
            content.append("工地名称由[").append(worksiteInDb.getName()).append("]改为[").append(worksite.getName()).append("]；");
            worksiteInDb.setName(worksite.getName());
        }
        if (worksite.getStatus() == null) {
            worksite.setStatus(0);
        }
        if (!worksiteInDb.getStatus().equals(worksite.getStatus())) {
            content.append("状态由[").append(worksiteInDb.getStatus() == 0 ? "正常" : "锁定").append("]改为[").append(worksite.getStatus() == 0 ? "正常" : "锁定").append("]，");
            worksiteInDb.setStatus(worksite.getStatus());
        }
        if (!worksiteInDb.getDesc().equals(worksite.getDesc())) {
            content.append("工地描述由[").append(worksiteInDb.getDesc()).append("]改为[").append(worksite.getDesc()).append("]；");
            worksiteInDb.setDesc(worksite.getDesc());
        }
        String str = content.toString();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
            worksiteInDb.setLastModifyTime(new DateTime().toString(AppConst.TIME_PATTERN));
            worksiteInDb.setLastModifyId(CommonUtils.getCurrentUser().getId());
            worksiteDao.update(worksiteInDb);
            // 移除缓存
            super.batchRemove(RedisCacheKey.WORKSITE_PREFIX);
            // 记录日志
            super.addLog("修改工地", "[" + name + "]" + str);
        }
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        Worksite worksite = this.getById(id);
        worksiteDao.delete(id);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKSITE_PREFIX);
        // 记录日志
        super.addLog("删除工地", worksite.toString());
    }

}
