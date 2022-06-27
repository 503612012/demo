package com.oven.demo.common.util;

import com.oven.demo.common.enumerate.ResultEnum;
import lombok.Data;

/**
 * 自定义返回类
 *
 * @author Oven
 */
@Data
public class ResultInfo<T> {

    private Integer code; // 返回代码(200:成功)
    private T data; // 返回的数据,正确的信息或错误描述信息

    public static ResultInfo<Object> build(Integer code, Object data) {
        ResultInfo<Object> resultInfo = new ResultInfo<>();
        resultInfo.setCode(ResultEnum.SUCCESS.getCode());
        resultInfo.setData(data);
        return resultInfo;
    }

    @Override
    public String toString() {
        return "{code=" + code + ", data=" + data + "}";
    }

}
