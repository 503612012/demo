package com.oven.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * 基金实体类
 *
 * @author Oven
 */
@Data
public class Fund {

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "fund_name")
    private String fundName;
    @Column(name = "status")
    private Integer status; // 状态，0-正常、1-删除
    @Column(name = "create_id")
    private Integer createId;
    @Column(name = "create_time")
    private String createTime;

    // 非数据库属性
    @Column(name = "create_name")
    private String createName;

}
