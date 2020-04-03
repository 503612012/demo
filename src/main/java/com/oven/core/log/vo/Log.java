package com.oven.core.log.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 日志实体类
 *
 * @author Oven
 */
@Data
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "operator_id")
    private Integer operatorId;
    @Column(name = "operator_name")
    private String operatorName;
    @Column(name = "operator_time")
    private String operatorTime;
    @Column(name = "operator_ip")
    private String operatorIp;

}
