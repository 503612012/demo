package com.oven.demo.framework.aop;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSON;
import com.oven.basic.common.util.DateUtils;
import com.oven.demo.common.util.CommonUtils;
import com.oven.demo.common.util.LogQueueUtils;
import com.oven.demo.core.log.entity.Log;
import com.oven.demo.core.user.entity.User;
import com.oven.demo.framework.annotation.AspectLog;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 接口调用日志记录处理
 *
 * @author Oven
 */
@Aspect
@Component
public class ApiLogAspect {

    /**
     * 处理完请求后执行
     */
    @AfterReturning(pointcut = "@annotation(aspectLog)", returning = "result")
    public void doAfterReturning(AspectLog aspectLog, Object result) {
        handle(aspectLog, result);
    }

    /**
     * 拦截异常操作
     */
    @AfterThrowing(value = "@annotation(aspectLog)")
    public void doAfterThrowing(AspectLog aspectLog) {
        handle(aspectLog, null);
    }

    protected void handle(AspectLog aspectLog, Object result) {
        String title = aspectLog.title();
        String response = StringUtils.substring(JSON.toJSONString(result), 0, 5000);

        String request = "";
        String requestUri = "";
        String requestMethod = "";
        Integer operatorId = -1;
        String operatorName = "";
        String operatorIp = "";
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest req = requestAttributes.getRequest();
            request = StringUtils.substring(JSON.toJSONString(ServletUtil.getParams(req)), 0, 5000);
            requestUri = req.getRequestURI();
            requestMethod = req.getMethod();
            User currentUser = CommonUtils.getCurrentUser();
            if (currentUser != null) {
                operatorId = currentUser.getId();
                operatorName = currentUser.getNickName();
            }
            operatorIp = CommonUtils.getCurrentUserIp();
        }
        Log log = Log.builder()
                .title(title)
                .request(request)
                .response(response)
                .requestUri(requestUri)
                .requestMethod(requestMethod)
                .operatorId(operatorId)
                .operatorName(operatorName)
                .operatorTime(DateUtils.getCurrentTime())
                .operatorIp(operatorIp)
                .build();
        LogQueueUtils.getInstance().offer(log);
    }

}
