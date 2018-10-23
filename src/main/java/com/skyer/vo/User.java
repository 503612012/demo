package com.skyer.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 *
 * @author SKYER
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String userName;
    private String password;
    private String nickName;
    private Integer age;
    private String email;
    private String phone;
    private Integer status; // 状态，0-正常、1-删除
    private Integer gender; // 0-女、1-男
    private String createTime;
    private Integer createId;
    private String lastModifyTime;
    private Integer lastModifyId;

    // 非数据库属性
    private String createName;
    private String lastModifyName;

}
