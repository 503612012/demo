package com.oven.exception;

import com.alibaba.fastjson.JSONObject;
import com.oven.contants.AppConst;
import com.oven.enumerate.ResultEnum;
import com.oven.util.ParametersUtils;
import com.oven.util.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常捕获
 *
 * @author Oven
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandle {

    /**
     * 处理捕获的异常
     */
    @ExceptionHandler(value = Exception.class)
    public Object handleException(Exception e, HttpServletRequest request, HttpServletResponse resp) throws IOException {
        log.error(AppConst.ERROR_LOG_PREFIX + "请求地址：" + request.getRequestURL().toString());
        log.error(AppConst.ERROR_LOG_PREFIX + "请求方法：" + request.getMethod());
        log.error(AppConst.ERROR_LOG_PREFIX + "请求者IP：" + request.getRemoteAddr());
        log.error(AppConst.ERROR_LOG_PREFIX + "请求参数：" + ParametersUtils.getParameters(request));
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            log.error(AppConst.ERROR_LOG_PREFIX + "错误信息：", myException.getE());
            if (myException.getCode().equals(ResultEnum.SEARCH_PAGE_ERROR.getCode())) {
                JSONObject result = new JSONObject();
                result.put("code", myException.getCode());
                result.put("msg", myException.getMsg());
                return result;
            } else if (myException.getCode().equals(ResultEnum.ERROR_PAGE.getCode())) {
                resp.sendRedirect("/err");
                return "";
            } else {
                return new ResultInfo<>(myException.getCode(), myException.getMsg());
            }
        } else if (e instanceof UnauthorizedException) {
            resp.sendRedirect("/noauth");
            return "";
        } else {
            log.error(AppConst.ERROR_LOG_PREFIX + "错误信息：", e);
        }
        resp.sendRedirect("/err");
        return "";
    }

}
