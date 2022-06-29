package com.oven.demo.core.user.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author Oven
 */
@Data
@Table(name = "t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "status")
    private Integer status; // 状态，0-正常、1-删除

    @Column(name = "gender")
    private Integer gender; // 0-女、1-男

    @Column(name = "create_time", updatable = false)
    private String createTime;

    @Column(name = "create_id", updatable = false)
    private Integer createId;

    @Column(name = "last_modify_time")
    private String lastModifyTime;

    @Column(name = "last_modify_id")
    private Integer lastModifyId;

    @Column(name = "last_login_time")
    private String lastLoginTime;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "err_num")
    private Integer errNum;

    @Column(name = "config")
    private String config;

    // 非数据库属性
    private String createName;
    private String lastModifyName;
    private boolean isOnline;

}
