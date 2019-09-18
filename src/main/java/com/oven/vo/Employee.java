package com.oven.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 员工实体类
 *
 * @author Oven
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id; // 员工表
    @Column(name = "name")
    private String name; // 姓名
    @Column(name = "age")
    private Integer age; // 年龄
    @Column(name = "status")
    private Integer status; // 状态，0-正常、1-删除
    @Column(name = "gender")
    private Integer gender; // 0-女、1-男
    @Column(name = "address")
    private String address; // 住址
    @Column(name = "contact")
    private String contact; // 联系方式
    @Column(name = "hour_salary")
    private Double hourSalary; // 时薪
    @Column(name = "create_time")
    private String createTime; // 创建时间
    @Column(name = "create_id")
    private Integer createId; // 创建人ID
    @Column(name = "last_modify_time")
    private String lastModifyTime; // 最后修改时间
    @Column(name = "last_modify_id")
    private Integer lastModifyId; // 最后修改人ID

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
