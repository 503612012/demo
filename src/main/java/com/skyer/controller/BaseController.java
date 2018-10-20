package com.skyer.controller;

import com.skyer.enumerate.ResultEnum;
import com.skyer.util.ResultInfo;

/**
 * 基类Controller
 *
 * @author SKYER
 */
public abstract class BaseController {

    /**
     * 请求成功
     *
     * @param data 请求成功返回的内容
     */
    Object success(Object data) {
        ResultInfo<Object> resultInfo = new ResultInfo<>();
        resultInfo.setCode(ResultEnum.SUCCESS.getCode());
        resultInfo.setData(data);
        return resultInfo;
    }

    /**
     * 请求失败
     *
     * @param code 失败编码
     * @param msg  失败信息
     */
    Object fail(Integer code, String msg) {
        ResultInfo<Object> resultInfo = new ResultInfo<>();
        resultInfo.setCode(code);
        resultInfo.setData(msg);
        return resultInfo;
    }

}
