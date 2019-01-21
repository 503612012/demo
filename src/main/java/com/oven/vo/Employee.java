package com.oven.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工实体类
 *
 * @author Oven
 */
@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; // 员工表
    private String name; // 姓名
    private Integer age; // 年龄
    private Integer status; // 状态，0-正常、1-删除
    private Integer gender; // 0-女、1-男
    private String address; // 住址
    private String contact; // 联系方式
    private Double daySalary; // 日新
    private Double monthSalary; // 月薪
    private String createTime; // 创建时间
    private Integer createId; // 创建人ID
    private String lastModifyTime; // 最后修改时间
    private Integer lastModifyId; // 最后修改人ID

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
