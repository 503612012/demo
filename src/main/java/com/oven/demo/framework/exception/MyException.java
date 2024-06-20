package com.oven.demo.framework.exception;

import com.oven.basic.common.util.IException;
import com.oven.basic.common.util.IResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @author Oven
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyException extends Exception implements IException {

    private static final long serialVersionUID = -2511799915640286981L;

    private Integer code;
    private String msg;
    private String log;
    private Exception e;

    public static MyException build(IResultCode resultCode, String log, Exception e) {
        return new MyException(resultCode.code(), resultCode.message(), log, e);
    }

    private MyException(Integer code, String msg, String log, Exception e) {
        this.code = code;
        this.msg = msg;
        this.log = log;
        this.e = e;
    }

    @Override
    public Integer code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }

}
