package com.skyer.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户-角色关系实体类
 *
 * @author SKYER
 */
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer roleId;

}
