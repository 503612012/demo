package com.oven.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色实体类
 *
 * @author Oven
 */
@Data
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String roleName;
    private String createTime;
    private Integer createId;
    private Integer status; // 状态，0-正常、1-删除
    private String lastModifyTime;
    private Integer lastModifyId;

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
