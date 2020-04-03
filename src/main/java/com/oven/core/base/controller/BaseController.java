package com.oven.core.base.controller;

import com.oven.constant.AppConst;
import com.oven.core.user.vo.User;
import com.oven.enumerate.ResultEnum;
import com.oven.util.ResultInfo;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
    protected Object fail(Integer code, String msg) {
        ResultInfo<Object> resultInfo = new ResultInfo<>();
        resultInfo.setCode(code);
        resultInfo.setData(msg);
        return resultInfo;
    }

    /**
     * 获取当前登录人信息
     */
    protected User getCurrentUser() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest req = attributes.getRequest();
        return (User) req.getSession().getAttribute(AppConst.CURRENT_USER);
    }

}
