package com.oven.demo.core.user.entity;

import com.oven.basic.base.entity.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = true)
public class User extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "user_name")
    private String userName; // 用户名

    @Column(name = "password")
    private String password; // 密码

    @Column(name = "nick_name")
    private String nickName; // 昵称

    @Column(name = "age")
    private Integer age; // 年龄

    @Column(name = "email")
    private String email; // 邮箱

    @Column(name = "phone")
    private String phone; // 手机号

    @Column(name = "status")
    private Integer status; // 状态：0-正常、1-删除

    @Column(name = "gender")
    private Integer gender; // 性别：0-女、1-男

    @Column(name = "create_time", updatable = false)
    private String createTime; // 创建时间

    @Column(name = "create_id", updatable = false)
    private Integer createId; // 创建人id

    @Column(name = "last_modify_time")
    private String lastModifyTime; // 最后修改时间

    @Column(name = "last_modify_id")
    private Integer lastModifyId; // 最后修改人id

    @Column(name = "last_login_time")
    private String lastLoginTime; // 最后登录时间

    @Column(name = "avatar")
    private String avatar; // 头像地址

    @Column(name = "err_num")
    private Integer errNum; // 密码错误次数

    @Column(name = "config")
    private String config; // 个性化配置

    // 非数据库属性
    private String createName;
    private String lastModifyName;
    private boolean isOnline;

}
