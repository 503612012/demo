package com.skyer.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志实体类
 *
 * @author SKYER
 */
@Data
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String title;
    private String content;
    private Integer operatorId;
    private String operatorTime;
    private String operatorIp;

    // 非数据库属性
    private String operatorName;

}
