package com.oven.demo.core.base.controller;

import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.common.util.ResultInfo;

/**
 * 基类Controller
 *
 * @author Oven
 */
public abstract class BaseController {

    /**
     * 请求成功
     *
     * @param data 请求成功返回的内容
     */
    protected Object success(Object data) {
        return ResultInfo.build(ResultEnum.SUCCESS.getCode(), data);
    }

    /**
     * 请求失败
     *
     * @param code 失败编码
     * @param msg  失败信息
     */
    protected Object fail(Integer code, String msg) {
        return ResultInfo.build(code, msg);
    }

}
