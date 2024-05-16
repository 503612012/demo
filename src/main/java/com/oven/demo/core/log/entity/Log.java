package com.oven.demo.core.log.entity;

import com.oven.basic.base.entity.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_log")
@EqualsAndHashCode(callSuper = true)
public class Log extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "title")
    private String title; // 标题

    @Column(name = "request")
    private String request; // 内容

    @Column(name = "response")
    private String response; // 请求返回

    @Column(name = "request_uri")
    private String requestUri; // 请求uri

    @Column(name = "request_method")
    private String requestMethod; // 请求方法

    @Column(name = "operator_id")
    private Integer operatorId; // 操作人id

    @Column(name = "operator_name")
    private String operatorName; // 操作人姓名

    @Column(name = "operator_time")
    private String operatorTime; // 操作时间

    @Column(name = "operator_ip")
    private String operatorIp; // 操作人ip地址

}
