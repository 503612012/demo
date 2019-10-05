package com.oven.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 财务管理实体类
 */
@Data
public class Finance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "worksite_id")
    private Integer worksiteId;
    @Column(name = "create_id")
    private Integer createId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "last_modify_id")
    private Integer lastModifyId;
    @Column(name = "last_modify_time")
    private String lastModifyTime;
    @Column(name = "money")
    private Double money;
    @Column(name = "out_money")
    private Double outMoney;
    @Column(name = "del_flag")
    private Integer delFlag; // 0正常，1删除
    @Column(name = "del_id")
    private Integer delId;
    @Column(name = "del_time")
    private String delTime;
    @Column(name = "finish_flag")
    private Integer finishFlag; // 是否清账，0是，1否
    @Column(name = "finish_id")
    private Integer finishId;
    @Column(name = "finish_time")
    private String finishTime;
    @Column(name = "remark")
    private String remark;

    // 非数据库属性
    @Column(name = "worksite_name")
    private String worksiteName;
    @Column(name = "create_name")
    private String createName;
    @Column(name = "last_modify_name")
    private String lastModifyName;
    @Column(name = "del_name")
    private String delName;
    @Column(name = "finish_name")
    private String finishName;

}
