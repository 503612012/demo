package com.oven.demo.core.role.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 角色实体类
 *
 * @author Oven
 */
@Data
@Table(name = "t_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "create_time", updatable = false)
    private String createTime;

    @Column(name = "create_id", updatable = false)
    private Integer createId;

    @Column(name = "status")
    private Integer status; // 状态，0-正常、1-删除

    @Column(name = "last_modify_time")
    private String lastModifyTime;

    @Column(name = "last_modify_id")
    private Integer lastModifyId;

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}