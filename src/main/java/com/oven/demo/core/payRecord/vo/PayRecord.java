package com.oven.demo.core.payRecord.vo;

import lombok.Data;

import javax.persistence.Column;

/**
 * 发薪记录实体类
 *
 * @author Oven
 */
@Data
public class PayRecord {

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "payer_id")
    private Integer payerId;
    @Column(name = "employee_id")
    private Integer employeeId;
    @Column(name = "pay_date")
    private String payDate;
    @Column(name = "total_money")
    private Double totalMoney; // 总薪资
    @Column(name = "total_hour")
    private Integer totalHour; // 总工时
    @Column(name = "workhour_ids")
    private String workhourIds; // 工时ID列表
    @Column(name = "remark")
    private String remark;
    @Column(name = "has_modify_money")
    private Integer hasModifyMoney; // 是否修改了金额，1是，0否
    @Column(name = "change_money")
    private Double changeMoney;

    // 非数据库属性
    @Column(name = "employee_name")
    private String employeeName;
    @Column(name = "payer_name")
    private String payerName;

}
