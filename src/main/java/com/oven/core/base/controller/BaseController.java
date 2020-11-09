package com.oven.core.base.controller;

import com.oven.common.constant.AppConst;
import com.oven.common.enumerate.ResultEnum;
import com.oven.common.util.ResultInfo;
import com.oven.core.user.vo.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        HttpServletRequest req = servletRequestAttributes.getRequest();
        if (req == null) {
            return null;
        }
        HttpSession session = req.getSession();
        if (session == null) {
            return null;
        }
        Object attribute = session.getAttribute(AppConst.CURRENT_USER);
        if (attribute == null) {
            return null;
        }
        return (User) attribute;
    }

}
