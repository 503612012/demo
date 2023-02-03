package com.oven.demo.core.employee.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 员工实体类
 *
 * @author Oven
 */
@Data
@Table(name = "t_employee")
@ApiModel(value = "员工实体类", description = "员工实体类")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    @ApiModelProperty(value = "员工主键", dataType = "int")
    private Integer id; // 员工表

    @Column(name = "name")
    @ApiModelProperty(value = "姓名", dataType = "string")
    private String name; // 姓名

    @Column(name = "age")
    @ApiModelProperty(value = "年龄", dataType = "int")
    private Integer age; // 年龄

    @Column(name = "status")
    @ApiModelProperty(value = "状态：0-正常、1-删除", dataType = "int")
    private Integer status; // 状态：0-正常、1-删除

    @Column(name = "gender")
    @ApiModelProperty(value = "性别：0-女、1-男", dataType = "int")
    private Integer gender; // 0-女、1-男

    @Column(name = "address")
    @ApiModelProperty(value = "住址", dataType = "string")
    private String address; // 住址

    @Column(name = "contact")
    @ApiModelProperty(value = "联系方式", dataType = "string")
    private String contact; // 联系方式

    @Column(name = "hour_salary")
    @ApiModelProperty(value = "时薪", dataType = "double")
    private Double hourSalary; // 时薪

    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(value = "创建时间", dataType = "string")
    private String createTime; // 创建时间

    @Column(name = "create_id", updatable = false)
    @ApiModelProperty(value = "创建人ID", dataType = "int")
    private Integer createId; // 创建人ID

    @Column(name = "last_modify_time")
    @ApiModelProperty(value = "最后修改时间", dataType = "string")
    private String lastModifyTime; // 最后修改时间

    @Column(name = "last_modify_id")
    @ApiModelProperty(value = "最后修改人ID", dataType = "int")
    private Integer lastModifyId; // 最后修改人ID

    // 非数据库属性
    @ApiModelProperty(value = "创建人", dataType = "string")
    private String createName;
    @ApiModelProperty(value = "最后修改人", dataType = "string")
    private String lastModifyName;

}
