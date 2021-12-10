package com.oven.demo.core.fundBill.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * 基金账单实体类
 *
 * @author Oven
 */
@Data
public class FundBill {

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "fund_id")
    private Integer fundId;
    @Column(name = "data_date")
    private String dataDate;
    @Column(name = "money")
    private Double money;

    // 非数据库属性
    @Column(name = "fund_name")
    private String fundName;

}
