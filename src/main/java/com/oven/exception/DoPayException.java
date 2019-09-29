package com.oven.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DoPayException extends Exception {

    private Integer code;
    private String msg;
    private Exception e;

    public DoPayException(Integer code, String msg, Exception e) {
        this.code = code;
        this.msg = msg;
        this.e = e;
    }

}
