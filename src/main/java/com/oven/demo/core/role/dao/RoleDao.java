package com.oven.demo.core.role.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.role.vo.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色dao层
 *
 * @author Oven
 */
@Repository
public class RoleDao extends BaseDao<Role> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过id获取
     *
     * @param id 角色ID
     */
    public Role getById(Integer id) {
        String sql = "select * from t_role where dbid = ?";
        List<Role> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Role.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页获取角色
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<Role> getByPage(Integer pageNum, Integer pageSize, Role role) {
        StringBuilder sql = new StringBuilder("select * from t_role");
        List<Object> params = addCondition(sql, role);
        return super.getByPage(sql, params, Role.class, pageNum, pageSize, jdbcTemplate);
    }

    /**
     * 获取角色总数量
     */
    public Integer getTotalNum(Role role) {
        StringBuilder sql = new StringBuilder("select count(*) from t_role");
        List<Object> params = addCondition(sql, role);
        return super.getTotalNum(sql, params, jdbcTemplate);
    }

    /**
     * 添加
     */
    public int add(Role role) throws Exception {
        return super.add(jdbcTemplate, role);
    }

    /**
     * 修改
     */
    public int update(Role role) throws Exception {
        return super.update(jdbcTemplate, role);
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_role where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 查询所有
     */
    public List<Role> getAll() {
        String sql = "select * from t_role where `status` = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(Role.class));
    }

    /**
     * 搜索条件
     */
    private List<Object> addCondition(StringBuilder sql, Role role) {
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(role.getRoleName())) {
            sql.append(" and role_name like ?");
            params.add("%" + role.getRoleName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        return params;
    }

}
