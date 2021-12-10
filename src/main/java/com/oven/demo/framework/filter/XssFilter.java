package com.oven.demo.framework.filter;

import org.springframework.util.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Xss过滤器
 *
 * @author Oven
 */
public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String servletPath = httpServletRequest.getServletPath();
        List<String> unXssFilterUrl = getUnXssFilterUrl();
        if (unXssFilterUrl != null && unXssFilterUrl.contains(servletPath)) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response);
        }
    }

    private List<String> getUnXssFilterUrl() {
        String unXssFilterUrl = "/demo/xssfilter,/demo/unxss";
        return StringUtils.isEmpty(unXssFilterUrl) ? Collections.emptyList() : Arrays.asList(unXssFilterUrl.split(","));
    }

}
