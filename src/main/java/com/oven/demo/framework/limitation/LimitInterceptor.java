package com.oven.demo.framework.limitation;

import com.google.common.collect.ImmutableList;
import com.oven.basic.common.enumerate.ResultEnum;
import com.oven.basic.common.util.IPUtils;
import com.oven.basic.framework.exception.MyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 使用aop拦截注解实现应用服务器分布式限流的目的,相同key共享限制次数
 *
 * @author Oven
 */
@Slf4j
@Aspect
@Configuration
public class LimitInterceptor {

    private final RedisTemplate<String, Serializable> limitRedisTemplate;

    @Autowired
    public LimitInterceptor(RedisTemplate<String, Serializable> limitRedisTemplate) {
        this.limitRedisTemplate = limitRedisTemplate;
    }

    @Around("execution(public * *(..)) && @annotation(com.oven.demo.framework.limitation.Limit)")
    public Object interceptor(ProceedingJoinPoint pjp) throws MyException {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Limit limitAnnotation = method.getAnnotation(Limit.class);
        LimitType limitType = limitAnnotation.limitType();
        String key;
        int limitPeriod = limitAnnotation.period();
        int limitCount = limitAnnotation.count();
        String errMsg = limitAnnotation.errMsg();
        switch (limitType) {
            case CUSTOMER:
                key = limitAnnotation.key();
                break;
            case IP_AND_METHOD:
                @SuppressWarnings("ConstantConditions")
                HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                key = LimitKey.LIMIT_KEY_PREFIX + IPUtils.getClientIPAddr(req) + "_" + limitAnnotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(limitAnnotation.prefix(), key));
        try {
            String luaScript = buildLuaScript();
            RedisScript<Number> redisScript = new DefaultRedisScript<>(luaScript, Number.class);
            Number count = limitRedisTemplate.execute(redisScript, keys, limitCount, limitPeriod);
            if (count != null && count.intValue() <= limitCount) {
                return pjp.proceed();
            } else {
                throw new RuntimeException(errMsg);
            }
        } catch (Throwable e) {
            if (e instanceof MyException) {
                throw new MyException(((MyException) e).getCode(), ((MyException) e).getMsg(), ((MyException) e).getLog(), ((MyException) e).getE());
            }
            if (e instanceof RuntimeException) {
                log.error("{}请求{}超过次数限制！", key, method);
            }
            throw new LimitException(ResultEnum.OVER_LIMIT_ERROR.getCode(), errMsg);
        }
    }

    /**
     * 限流 脚本
     *
     * @return lua脚本
     */
    public String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                // 调用不超过最大值，则直接返回
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                // 执行计算器自加
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                // 从第一次调用开始限流，设置对应键值的过期
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }

}