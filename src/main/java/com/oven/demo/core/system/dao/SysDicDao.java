package com.oven.demo.core.system.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.demo.common.constant.AppConst;
import com.oven.demo.core.system.entity.SysDicEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统级字典dao层
 *
 * @author Oven
 */
@Repository
public class SysDicDao extends BaseDao<SysDicEntity> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 更新
     */
    public int update(SysDicEntity sysdic) {
        String sql = "update t_sys_dic set `_value` = ?, `_desc` = ? where `dbid` = ?";
        return this.jdbcTemplate.update(sql, sysdic.getValue(), sysdic.getDesc(), sysdic.getId());
    }

    /**
     * 分页查询数据字典
     */
    public List<SysDicEntity> getByPage(Integer pageNum, Integer pageSize, SysDicEntity sysdic) {
        return super.getByPage(pageNum, pageSize, addCondition(sysdic));
    }

    /**
     * 统计数据字典总数量
     */
    public Integer getTotalNum(SysDicEntity sysdic) {
        return super.getTotalNum(addCondition(sysdic));
    }

    /**
     * 搜索条件
     */
    private ConditionAndParams addCondition(SysDicEntity sysdic) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (sysdic != null && !StringUtils.isEmpty(sysdic.getKey())) {
            sql.append(" and (`_key` like ? or `_desc` like ?)");
            params.add("%" + sysdic.getKey().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
            params.add("%" + sysdic.getKey().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        return ConditionAndParams.build(sql.toString(), params.toArray());
    }

    /**
     * 修改状态
     */
    public void updateStatus(Integer id, Integer status) {
        this.jdbcTemplate.update("update t_sys_dic set `_status` = ? where dbid = ?", status, id);
    }

    /**
     * 根据key查询
     */
    public SysDicEntity getByKey(String key) {
        return super.getOne(ConditionAndParams.build("and _key = ?", key));
    }

    public void reduceNum() {
        String sql = "update t_sys_dic set _value = _value - 1 where _key = 'secKill'";
        this.jdbcTemplate.update(sql);
    }

}
