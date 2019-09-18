package com.oven.limitation;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义限流异常类
 *
 * @author Oven
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LimitException extends RuntimeException {

    private Integer code;
    private String msg;

    LimitException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
