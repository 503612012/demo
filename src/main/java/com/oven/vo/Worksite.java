package com.oven.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 工地实体类
 *
 * @author Oven
 */
@Data
public class Worksite implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; // 工地
    private String name; // 名称
    private String desc; // 描述
    private Integer status; // 状态，0-正常、1-删除
    private Integer createId; // 创建人ID
    private String createTime; // 创建时间
    private Integer lastModifyId; // 最后修改人ID
    private String lastModifyTime; // 最后修改时间

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
