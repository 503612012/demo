package com.oven.demo.framework.config;

import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * 开发环境配置
 *
 * @author Oven
 */
public class DevEnvSet {

    /**
     * 开发环境根据自己操作系统和相关文件路径修改一下配置
     */
    public static void config(Properties properties) {
        String profile = String.valueOf(properties.get("demo.profile"));
        if (PropertyConfig.PRO_PROFILE.equals(profile)) {
            return;
        }
        properties.put("logging.config", "classpath:logback-dev.xml");

        String platform = System.getenv("p");
        // Mac
        if (StringUtils.isEmpty(platform) || "m".equalsIgnoreCase(platform)) {
            properties.put("log.home", "/Users/oven/logs/demo/logs/");
        }

        // Win
        if ("w".equalsIgnoreCase(platform)) {
            properties.put("log.home", "E:\\logs\\demo\\");
        }
    }

}
