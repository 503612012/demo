package com.oven.core.user.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author Oven
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "create_id")
    private Integer createId;
    @Column(name = "last_modify_time")
    private String lastModifyTime;
    @Column(name = "last_modify_id")
    private Integer lastModifyId;
    @Column(name = "last_login_time")
    private String lastLoginTime;

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
