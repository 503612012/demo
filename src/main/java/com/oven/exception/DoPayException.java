package com.oven.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义发放薪资异常
 *
 * @author Oven
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DoPayException extends Exception {

    private static final long serialVersionUID = -6779146587014220590L;

    private Integer code;
    private String msg;
    private Exception e;

    public DoPayException(Integer code, String msg, Exception e) {
        this.code = code;
        this.msg = msg;
        this.e = e;
    }

}
