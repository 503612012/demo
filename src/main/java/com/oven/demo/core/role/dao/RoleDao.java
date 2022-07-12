package com.oven.demo.core.role.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.base.entity.SqlAndParams;
import com.oven.demo.core.role.entity.Role;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色dao层
 *
 * @author Oven
 */
@Repository
public class RoleDao extends BaseDao<Role> {

    /**
     * 分页获取角色
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Role> getByPage(Integer pageNum, Integer pageSize, Role role) {
        return super.getByPage(addCondition(role), pageNum, pageSize);
    }

    /**
     * 获取角色总数量
     */
    public Integer getTotalNum(Role role) {
        return super.getTotalNum(addCondition(role));
    }

    /**
     * 搜索条件
     */
    private SqlAndParams addCondition(Role role) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(role.getRoleName())) {
            sql.append(" and role_name like ?");
            params.add("%" + role.getRoleName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        return SqlAndParams.build(sql.toString(), params.toArray());
    }

}
