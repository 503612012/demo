package com.skyer.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.RedisCacheKey;
import com.skyer.mapper.WorksiteMapper;
import com.skyer.vo.Worksite;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 工地服务层
 *
 * @author SKYER
 */
@Service
@Transactional
public class WorksiteService extends BaseService {

    @Resource
    private WorksiteMapper worksiteMapper;

    /**
     * 分页查询工地
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Worksite> getByPage(Integer pageNum, Integer pageSize, Worksite worksite) {
        List<Worksite> list = super.get(RedisCacheKey.WORKSITE_GET_BY_PAGE + pageNum + "_" + pageSize + "_" + worksite.toString()); // 先读取缓存
        if (list == null) { // double check
            synchronized (this) {
                list = super.get(RedisCacheKey.WORKSITE_GET_BY_PAGE + pageNum + "_" + pageSize + "_" + worksite.toString()); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (list == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    list = worksiteMapper.getByPage((pageNum - 1) * pageSize, pageSize, worksite);
                    super.set(RedisCacheKey.WORKSITE_GET_BY_PAGE + pageNum + "_" + pageSize + "_" + worksite.toString(), list);
                }
            }
        }
        return list;
    }

    /**
     * 获取工地总数量
     */
    public Long getTotalNum(Worksite worksite) {
        Long totalNum = super.get(RedisCacheKey.WORKSITE_GET_TOTAL_NUM + worksite.toString()); // 先读取缓存
        if (totalNum == null) { // double check
            synchronized (this) {
                totalNum = super.get(RedisCacheKey.WORKSITE_GET_TOTAL_NUM + worksite.toString()); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (totalNum == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    totalNum = worksiteMapper.getTotalNum(worksite);
                    super.set(RedisCacheKey.WORKSITE_GET_TOTAL_NUM + worksite.toString(), totalNum);
                }
            }
        }
        return totalNum;
    }

    /**
     * 添加
     */
    public void add(Worksite worksite) {
        worksite.setCreateId(super.getCurrentUser().getId());
        worksite.setCreateTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        worksite.setLastModifyId(super.getCurrentUser().getId());
        worksite.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKSITE_PREFIX);
        // 记录日志
        super.addLog("添加工地", worksite.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        worksiteMapper.add(worksite);
    }

    /**
     * 通过主键查询
     */
    public Worksite getById(Integer id) {
        Worksite worksite = super.get(RedisCacheKey.WORKSITE_GET_BY_ID + id); // 先读取缓存
        if (worksite == null) { // double check
            synchronized (this) {
                worksite = super.get(RedisCacheKey.WORKSITE_GET_BY_ID + id); // 再次从缓存中读取，防止高并发情况下缓存穿透问题
                if (worksite == null) { // 缓存中没有，再从数据库中读取，并写入缓存
                    worksite = worksiteMapper.getById(id);
                    super.set(RedisCacheKey.WORKSITE_GET_BY_ID + id, worksite);
                }
            }
        }
        return worksite;
    }

    /**
     * 更新
     */
    public void update(Worksite worksite) {
        Worksite worksiteInDb = worksiteMapper.getById(worksite.getId());
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
            worksiteInDb.setLastModifyTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            worksiteInDb.setLastModifyId(super.getCurrentUser().getId());
            // 移除缓存
            super.batchRemove(RedisCacheKey.WORKSITE_PREFIX);
            // 记录日志
            super.addLog("修改工地", "[" + name + "]" + str, super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
            worksiteMapper.update(worksiteInDb);
        }
    }

    /**
     * 删除
     */
    public void delete(Integer id) {
        Worksite worksite = this.getById(id);
        // 移除缓存
        super.batchRemove(RedisCacheKey.WORKSITE_PREFIX);
        // 记录日志
        super.addLog("删除工地", worksite.toString(), super.getCurrentUser().getId(), super.getCurrentUser().getNickName(), super.getCurrentUserIp());
        worksiteMapper.delete(id);
    }

    /**
     * 查询所有工地
     */
    public JSONArray findAll() {
        List<Worksite> list = worksiteMapper.getAll();
        JSONArray result = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            JSONObject obj = new JSONObject();
            obj.put("id", list.get(i).getId());
            obj.put("text", list.get(i).getName());
            if (i == 0) {
                obj.put("selected", true);
            }
            result.add(obj);
        }
        return result;
    }

}
