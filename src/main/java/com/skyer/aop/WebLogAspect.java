package com.skyer.aop;

import com.skyer.util.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 全局请求参数记录
 *
 * @author SKYER
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    @Pointcut("execution(public * com.skyer.controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore() {
        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        @SuppressWarnings("ConstantConditions") HttpServletRequest request = attributes.getRequest();
        // 记录请求内容
        log.info("请求地址：" + request.getRequestURL().toString());
        log.info("请求方法：" + request.getMethod());
        log.info("请求者IP：" + request.getRemoteAddr());
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            log.info("name: {}, value: {}", name, request.getParameter(name));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 请求返回的内容
        if (ret instanceof ResultInfo) {
            log.info(((ResultInfo) ret).getCode().toString());
        }
    }

}
