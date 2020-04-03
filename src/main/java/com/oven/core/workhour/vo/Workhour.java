package com.oven.core.workhour.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 工时实体类
 *
 * @author Oven
 */
@Data
public class Workhour implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "employee_id")
    private Integer employeeId;
    @Column(name = "worksite_id")
    private Integer worksiteId;
    @Column(name = "work_date")
    private String workDate;
    @Column(name = "work_hour")
    private Integer workhour;
    @Column(name = "hour_salary")
    private Double hourSalary;
    @Column(name = "create_id")
    private Integer createId;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "has_pay")
    private Integer hasPay; // 是否发薪，0否，1是
    @Column(name = "remark")
    private String remark; // 备注

    // 非数据库属性
    @Column(name = "employee_name")
    private String employeeName;
    @Column(name = "worksite_name")
    private String worksiteName;
    @Column(name = "create_name")
    private String createName;


}
