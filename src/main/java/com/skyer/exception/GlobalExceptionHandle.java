package com.skyer.exception;

import com.skyer.contants.AppConst;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 全局异常捕获
 *
 * @author SKYER
 */
@ControllerAdvice
public class GlobalExceptionHandle {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    /**
     * 处理捕获的异常
     */
    @ExceptionHandler(value = Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse resp) throws IOException {
        e.printStackTrace();
        LOG.error(AppConst.ERROR_LOG_PREFIX + "请求地址：" + request.getRequestURL().toString());
        LOG.error(AppConst.ERROR_LOG_PREFIX + "请求方法：" + request.getMethod());
        LOG.error(AppConst.ERROR_LOG_PREFIX + "请求者IP：" + request.getRemoteAddr());
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            LOG.error(AppConst.ERROR_LOG_PREFIX + "name: {}, value: {}", name, request.getParameter(name));
        }
        if (e instanceof UnauthorizedException) {
            resp.sendRedirect("/noauth");
            return;
        }
        resp.sendRedirect("/err");
    }

}
