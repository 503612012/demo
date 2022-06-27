package com.oven.demo.core.log.vo;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 日志实体类
 *
 * @author Oven
 */
@Data
@Builder
@Table(name = "t_log")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "request")
    private String request;

    @Column(name = "response")
    private String response;

    @Column(name = "request_uri")
    private String requestUri;

    @Column(name = "request_method")
    private String requestMethod;

    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "operator_name")
    private String operatorName;

    @Column(name = "operator_time")
    private String operatorTime;

    @Column(name = "operator_ip")
    private String operatorIp;

}
