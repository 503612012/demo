package com.oven.core.roleMenu.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 角色-菜单关系实体类
 *
 * @author Oven
 */
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "menu_id")
    private Integer menuId;

}
