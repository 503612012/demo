package com.oven.demo.core.user.dao;

import com.oven.basic.base.dao.BaseDao;
import com.oven.basic.base.entity.ConditionAndParams;
import com.oven.basic.common.util.PropertyRowMapper;
import com.oven.demo.core.user.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
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
     * 通过用户名查询
     *
     * @param userName 用户名
     */
    public User getByUserName(String userName) {
        return super.getOne(ConditionAndParams.build("and user_name = ?", userName));
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

    public List<User> getByIds(List<Integer> ids) {
        String sql = "select * from t_user where dbid in (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        return jdbcTemplate.query(sql, PropertyRowMapper.build(User.class));
    }

}
