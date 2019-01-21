package com.oven.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色-菜单关系实体类
 *
 * @author Oven
 */
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer roleId;
    private Integer menuId;

}
