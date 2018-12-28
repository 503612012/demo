package com.skyer.exception;

import com.alibaba.fastjson.JSONObject;
import com.skyer.contants.AppConst;
import com.skyer.enumerate.ResultEnum;
import com.skyer.util.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 全局异常捕获
 *
 * @author SKYER
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
        e.printStackTrace();
        log.error(AppConst.ERROR_LOG_PREFIX + "请求地址：" + request.getRequestURL().toString());
        log.error(AppConst.ERROR_LOG_PREFIX + "请求方法：" + request.getMethod());
        log.error(AppConst.ERROR_LOG_PREFIX + "请求者IP：" + request.getRemoteAddr());
        Enumeration<String> enums = request.getParameterNames();
        StringBuilder content = new StringBuilder();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            content.append("\"").append(name).append("\"").append(": ").append("\"").append(request.getParameter(name)).append("\"").append(", ");
        }
        log.error(AppConst.ERROR_LOG_PREFIX + "请求参数：[{" + content.toString().substring(0, content.toString().length() - 2) + "}]");
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
        }
        if (e instanceof UnauthorizedException) {
            resp.sendRedirect("/noauth");
            return "";
        }
        resp.sendRedirect("/err");
        return "";
    }

}
