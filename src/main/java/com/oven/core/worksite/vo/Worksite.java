package com.oven.core.worksite.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 工地实体类
 *
 * @author Oven
 */
@Data
public class Worksite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id; // 工地
    @Column(name = "name")
    private String name; // 名称
    @Column(name = "desc")
    private String desc; // 描述
    @Column(name = "status")
    private Integer status; // 状态，0-正常、1-删除
    @Column(name = "create_id")
    private Integer createId; // 创建人ID
    @Column(name = "create_time")
    private String createTime; // 创建时间
    @Column(name = "last_modify_id")
    private Integer lastModifyId; // 最后修改人ID
    @Column(name = "last_modify_time")
    private String lastModifyTime; // 最后修改时间

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
