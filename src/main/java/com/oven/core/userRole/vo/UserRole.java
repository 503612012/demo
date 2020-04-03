package com.oven.core.userRole.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 用户-角色关系实体类
 *
 * @author Oven
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "role_id")
    private Integer roleId;

}
