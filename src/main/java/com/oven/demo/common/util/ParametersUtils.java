package com.oven.demo.common.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 获取请求参数工具类
 *
 * @author Oven
 */
public class ParametersUtils {

    public static String getParameters(HttpServletRequest req) {
        Enumeration<String> enums = req.getParameterNames();
        JSONObject parameter = new JSONObject();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            parameter.put(name, req.getParameter(name));
        }
        return parameter.toJSONString();
    }

}
