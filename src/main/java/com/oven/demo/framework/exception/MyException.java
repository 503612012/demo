package com.oven.demo.framework.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author Oven
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyException extends Exception {

    private static final long serialVersionUID = -2511799915640286981L;

    private Integer code;
    private String msg;
    private String log;
    private Exception e;

    public MyException(Integer code, String msg, String log, Exception e) {
        this.code = code;
        this.msg = msg;
        this.log = log;
        this.e = e;
    }

}
