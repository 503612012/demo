package com.oven.dao;

import com.oven.constant.AppConst;
import com.oven.util.VoPropertyRowMapper;
import com.oven.vo.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 用户dao层
 *
 * @author Oven
 */
@Repository
public class UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过id获取
     *
     * @param id 用户ID
     */
    public User getById(Integer id) {
        String sql = "select * from t_user where dbid = ?";
        List<User> list = this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(User.class), id);
        return list.size() == 0 ? null : list.get(0);
    }

    /**
     * 分页获取用户
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示数量
     */
    public List<User> getByPage(Integer pageNum, Integer pageSize, User user) {
        StringBuilder sb = new StringBuilder("select * from t_user");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, user);
        String sql = sb.append(" limit ?,?").toString().replaceFirst("and", "where");
        params.add((pageNum - 1) * pageSize);
        params.add(pageSize);
        return this.jdbcTemplate.query(sql, params.toArray(), new VoPropertyRowMapper<>(User.class));
    }

    /**
     * 获取用户总数量
     */
    public Integer getTotalNum(User user) {
        StringBuilder sb = new StringBuilder("select count(*) from t_user");
        List<Object> params = new ArrayList<>();
        addCondition(sb, params, user);
        String sql = sb.toString().replaceFirst("and", "where");
        return this.jdbcTemplate.queryForObject(sql, params.toArray(), Integer.class);
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
     * 添加
     */
    public int add(User user) {
        String sql = "insert into t_user (`dbid`," +
                "                         `user_name`," +
                "                         `password`," +
                "                         `nick_name`," +
                "                         `age`," +
                "                         `email`," +
                "                         `phone`," +
                "                         `status`," +
                "                         `gender`," +
                "                         `create_time`," +
                "                         `create_id`," +
                "                         `last_modify_time`," +
                "                         `last_modify_id`)" +
                "                   values (null, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?, ?, ?)";
        KeyHolder key = new GeneratedKeyHolder();
        PreparedStatementCreator creator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"dbid"});
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickName());
            ps.setInt(4, user.getAge());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());
            ps.setInt(7, user.getGender());
            ps.setString(8, user.getCreateTime());
            ps.setInt(9, user.getCreateId());
            ps.setString(10, user.getLastModifyTime());
            ps.setInt(11, user.getLastModifyId());
            return ps;
        };
        this.jdbcTemplate.update(creator, key);
        return Objects.requireNonNull(key.getKey()).intValue();
    }

    /**
     * 修改
     */
    public int update(User userInDb) {
        String sql = "update t_user set `user_name` = ?," +
                "                       `password` = ?," +
                "                       `nick_name` = ?," +
                "                       `age` = ?," +
                "                       `email` = ?," +
                "                       `phone` = ?," +
                "                       `status` = ?," +
                "                       `gender` = ?," +
                "                       `create_id` = ?," +
                "                       `create_time` = ?," +
                "                       `last_modify_id` = ?," +
                "                       `last_modify_time` = ?" +
                "                 where `dbid` = ?";
        return this.jdbcTemplate.update(sql, userInDb.getUserName(), userInDb.getPassword(), userInDb.getNickName(),
                userInDb.getAge(), userInDb.getEmail(), userInDb.getPhone(), userInDb.getStatus(), userInDb.getGender(),
                userInDb.getCreateId(), userInDb.getCreateTime(), userInDb.getLastModifyId(), userInDb.getLastModifyTime(),
                userInDb.getId());
    }

    /**
     * 删除
     */
    public int delete(Integer id) {
        String sql = "delete from t_user where dbid = ?";
        return this.jdbcTemplate.update(sql, id);
    }

    /**
     * 获取所有用户
     */
    public List<User> getAll() {
        String sql = "select * from t_user where `status` = 0";
        return this.jdbcTemplate.query(sql, new VoPropertyRowMapper<>(User.class));
    }

    /**
     * 搜索条件
     */
    private void addCondition(StringBuilder sb, List<Object> params, User user) {
        if (!StringUtils.isEmpty(user.getUserName())) {
            sb.append(" and user_name like ?");
            params.add("%" + user.getUserName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(user.getNickName())) {
            sb.append(" and nick_name like ?");
            params.add("%" + user.getNickName().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
        if (!StringUtils.isEmpty(user.getPhone())) {
            sb.append(" and phone like ?");
            params.add("%" + user.getPhone().replaceAll("%", AppConst.PERCENTAGE_MARK) + "%");
        }
    }

}
