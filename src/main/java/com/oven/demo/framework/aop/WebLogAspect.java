package com.oven.demo.framework.aop;

import com.oven.basic.common.requestLog.RequestLog;
import com.oven.basic.common.util.ParametersUtils;
import com.oven.basic.common.util.RequestLogQueueUtils;
import com.oven.basic.common.util.ResultInfo;
import com.oven.basic.core.user.entity.User;
import com.oven.demo.common.constant.AppConst;
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

    @Pointcut("execution(public * com.oven.demo.core.*.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore() {
        // 获取请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        // 记录请求内容
        log.info("请求地址：" + request.getRequestURL().toString());
        log.info("请求方法：" + request.getMethod());
        log.info("请求者IP：" + request.getRemoteAddr());
        log.info("请求参数：" + ParametersUtils.getParameters(request));

        // 放入日志队列，保存到数据库
        RequestLog requestLog = new RequestLog();
        requestLog.setRequestIp(request.getRemoteAddr());
        requestLog.setRequestMethod(request.getMethod());
        requestLog.setRequestParam(ParametersUtils.getParameters(request));
        requestLog.setRequestTime(DateTime.now().toString(AppConst.TIME_PATTERN));
        requestLog.setRequestUrl(request.getRequestURL().toString());
        if (request.getSession().getAttribute(AppConst.CURRENT_USER) != null) {
            User currentUser = (User) request.getSession().getAttribute(AppConst.CURRENT_USER);
            requestLog.setUserId(currentUser.getId());
        } else {
            requestLog.setUserId(-1);
        }

        RequestLogQueueUtils.getInstance().offer(requestLog);
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 请求返回的内容
        if (ret instanceof ResultInfo) {
            log.info("返回结果：" + ((ResultInfo<?>) ret).getCode().toString());
        }
    }

}
