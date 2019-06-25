package com.oven.limitation;

import lombok.Data;

/**
 * 自定义限流异常类
 *
 * @author Oven
 */
@Data
public class LimitException extends RuntimeException {

    private Integer code;
    private String msg;

    public LimitException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
