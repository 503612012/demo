package com.oven.framework.config;

import com.oven.framework.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 拦截器配置
 *
 * @author Oven
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor).excludePathPatterns("/login", "/css/**", "/js/**", "/layui/**", "/easyui/**", "/*.js");
    }

}
