package com.oven.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户-角色关系实体类
 *
 * @author Oven
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer roleId;

}
