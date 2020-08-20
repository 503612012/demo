package com.oven.common.requestLog;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * 请求日志记录实体类
 *
 * @author Oven
 */
@Data
public class RequestLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "dbid")
    private Integer id;
    @Column(name = "request_time")
    private String requestTime;
    @Column(name = "request_url")
    private String requestUrl;
    @Column(name = "request_method")
    private String requestMethod;
    @Column(name = "request_ip")
    private String requestIp;
    @Column(name = "request_param")
    private String requestParam;
    @Column(name = "user_id")
    private Integer userId;

}
