package com.oven.aop;

import com.oven.common.RequestLog;
import com.oven.constant.AppConst;
import com.oven.util.ParametersUtils;
import com.oven.util.RequestLogQueueUtils;
import com.oven.util.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局请求参数记录
 *
 * @author Oven
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    @Pointcut("execution(public * com.oven.core..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore() {
        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.info(AppConst.ERROR_LOG_PREFIX + "全局请求参数记录类异常，获取attributes为null！");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        // 记录请求内容
        log.info(AppConst.INFO_LOG_PREFIX + "请求地址：" + request.getRequestURL().toString());
        log.info(AppConst.INFO_LOG_PREFIX + "请求方法：" + request.getMethod());
        log.info(AppConst.INFO_LOG_PREFIX + "请求者IP：" + request.getRemoteAddr());
        log.info(AppConst.INFO_LOG_PREFIX + "请求参数：" + ParametersUtils.getParameters(request));

        // 放入日志队列，保存到数据库
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestIp(request.getRemoteAddr());
        requestLog.setRequestMethod(request.getMethod());
        requestLog.setRequestParam(ParametersUtils.getParameters(request));
        requestLog.setRequestTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
        requestLog.setRequestUrl(request.getRequestURL().toString());
        RequestLogQueueUtils.getInstance().offer(requestLog);
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 请求返回的内容
        if (ret instanceof ResultInfo) {
            log.info(AppConst.INFO_LOG_PREFIX + "返回结果：" + ((ResultInfo) ret).getCode().toString());
        }
    }

}
