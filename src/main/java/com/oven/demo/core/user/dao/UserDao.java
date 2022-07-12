package com.oven.demo.core.user.dao;

import com.oven.demo.common.constant.AppConst;
import com.oven.demo.common.util.VoPropertyRowMapper;
import com.oven.demo.core.base.dao.BaseDao;
import com.oven.demo.core.base.entity.SqlAndParams;
import com.oven.demo.core.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户dao层
 *
 * @author Oven
 */
@Repository
public class UserDao extends BaseDao<User> {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 分页获取用户
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<User> getByPage(Integer pageNum, Integer pageSize, User user) {
        return super.getByPage(addCondition(user), pageNum, pageSize);
    }

    /**
     * 获取用户总数量
     */
    public Integer getTotalNum(User user) {
        return super.getTotalNum(addCondition(user));
    }

    /**
     * 通过用户名查询
     *
     * @param userName 用户名
     */
    public User getByUserName(String userName) {
        String sql = "select * from t_user where user_name = ?";
        List<User> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(User.class), userName);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 搜索条件
     */
    private SqlAndParams addCondition(User user) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        if (!StringUtils.isEmpty(user.getUserName())) {
            sql.append(" and user_name like ?");
            params.add("%" + user.getUserName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(user.getNickName())) {
            sql.append(" and nick_name like ?");
            params.add("%" + user.getNickName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(user.getPhone())) {
            sql.append(" and phone like ?");
            params.add("%" + user.getPhone().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        return SqlAndParams.build(sql.toString(), params.toArray());
    }

    public void updateLastLoginTime(String time, Integer userId) {
        String sql = "update t_user set last_login_time = ?, err_num = 0 where dbid = ?";
        this.jdbcTemplate.update(sql, time, userId);
    }

    /**
     * 登录密码错误次数加一
     */
    public void logPasswordWrong(Integer userId) {
        String sql = "update t_user set err_num = err_num + 1 where dbid = ?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * 更新头像
     */
    public void updateAvatar(Integer id, String avatarFileName) {
        String sql = "update t_user set avatar = ? where dbid = ?";
        jdbcTemplate.update(sql, avatarFileName, id);
    }

    /**
     * 重置错误次数
     */
    public void resetErrNum(Integer userId) {
        String sql = "update t_user set err_num = 0 where dbid = ?";
        jdbcTemplate.update(sql, userId);
    }

    /**
     * 修改用户个性化配置
     */
    public void updateConfig(Integer id, String config) {
        String sql = "update t_user set `config` = ? where dbid = ?";
        jdbcTemplate.update(sql, config, id);
    }

}
