package com.skyer.aop;

import com.skyer.util.ResultInfo;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Aspect
@Component
public class WebLogAspect {

    private static final Logger LOG = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.skyer.controller.*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore() {
        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // FIXME: 2018/7/14 此处的日志应该放到redis中，并设置有效时间
        // 记录请求内容
        LOG.info("请求地址：" + request.getRequestURL().toString());
        LOG.info("请求方法：" + request.getMethod());
        LOG.info("请求者IP：" + request.getRemoteAddr());
        Enumeration<String> enums = request.getParameterNames();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            LOG.info("name: {}, value: {}", name, request.getParameter(name));
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 请求返回的内容
        if (ret instanceof ResultInfo) {
            LOG.info(((ResultInfo) ret).getCode().toString());
        }
    }

}
