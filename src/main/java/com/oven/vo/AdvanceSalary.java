package com.oven.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 预支薪资实体类
 *
 * @author Oven
 */
@Data
public class AdvanceSalary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "employee_id")
    private Integer employeeId;
    @Column(name = "has_repay")
    private Integer hasRepay; // 是否归还，0是、1否
    @Column(name = "advance_time")
    private String advanceTime;
    @Column(name = "money")
    private Double money;
    @Column(name = "create_id")
    private Integer createId;
    @Column(name = "remark")
    private String remark;

    // 非数据库属性
    @Column(name = "create_name")
    private String createName;
    @Column(name = "employee_name")
    private String employeeName;

}
