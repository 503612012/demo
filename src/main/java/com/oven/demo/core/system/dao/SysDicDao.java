package com.oven.demo.core.system.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.basic.base.entity.UpdateColumn;
import com.oven.demo.core.system.entity.SysDicEntity;
import org.springframework.stereotype.Repository;


/**
 * 系统级字典dao层
 *
 * @author Oven
 */
@Repository
public class SysDicDao extends BaseDao<SysDicEntity> {

    /**
     * 更新
     */
    public int update(SysDicEntity sysdic) {
        return super.update(UpdateColumn.update("_value", sysdic.getValue()).and("_desc", sysdic.getDesc()), ConditionAndParams.eq("dbid", sysdic.getId()));
    }

    /**
     * 修改状态
     */
    public void updateStatus(Integer id, Integer status) {
        super.update(UpdateColumn.update("_status", status), ConditionAndParams.eq("dbid", id));
    }

    /**
     * 根据key查询
     */
    public SysDicEntity getByKey(String key) {
        return super.getOne(ConditionAndParams.eq("_key", key));
    }

    public void reduceNum() {
        super.decrement("_value", ConditionAndParams.eq("_key", "secKill"));
    }

}
