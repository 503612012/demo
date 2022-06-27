package com.oven.demo.framework.exception;

import com.alibaba.fastjson.JSONObject;
import com.oven.demo.common.enumerate.ResultEnum;
import com.oven.demo.common.util.ParametersUtils;
import com.oven.demo.common.util.ResultInfo;
import com.oven.demo.framework.limitation.LimitException;
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
        log.error("请求地址：{}", request.getRequestURL().toString());
        log.error("请求方法：{}", request.getMethod());
        log.error("请求者IP：{}", request.getRemoteAddr());
        log.error("请求参数：{}", ParametersUtils.getParameters(request));
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            log.error(myException.getLog(), myException.getE());
            if (myException.getCode().equals(ResultEnum.SEARCH_PAGE_ERROR.getCode())) {
                JSONObject result = new JSONObject();
                result.put("code", myException.getCode());
                result.put("data", myException.getMsg());
                return result;
            } else if (myException.getCode().equals(ResultEnum.ERROR_PAGE.getCode())) {
                resp.sendRedirect("/err");
                return "";
            } else {
                return ResultInfo.build(myException.getCode(), myException.getMsg());
            }
        } else if (e instanceof UnauthorizedException) {
            resp.sendRedirect("/noauth");
            return "";
        } else if (e instanceof LimitException) {
            LimitException limitException = (LimitException) e;
            return ResultInfo.build(limitException.getCode(), limitException.getMsg());
        } else {
            log.error("错误信息：", e);
        }
        resp.sendRedirect("/err");
        return "";
    }

}
