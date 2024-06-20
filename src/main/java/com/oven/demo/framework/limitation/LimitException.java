package com.oven.demo.framework.limitation;

import com.oven.basic.common.util.IException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义限流异常类
 *
 * @author Oven
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LimitException extends RuntimeException implements IException {

    private static final long serialVersionUID = -1920795727304163167L;

    private Integer code;
    private String msg;

    LimitException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
