package com.skyer.controller;

import com.skyer.contants.AppConst;
import com.skyer.enumerate.ResultEnum;
import com.skyer.util.ResultInfo;
import com.skyer.vo.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取当前登录人信息
     */
    @SuppressWarnings("all")
    User getCurrentUser() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return (User) req.getSession().getAttribute(AppConst.CURRENT_USER);
    }

}
