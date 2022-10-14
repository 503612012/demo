package com.oven.demo.framework.config;

import java.util.Properties;

/**
 * 开发环境配置
 *
 * @author Oven
 */
public class DevEnvSet {

    // Mac
    public static final String LICENSE_PATH = "/Users/oven/logs/license/license.lic"; // 证书生成路径
    public static final String PUBLIC_KEYS_STORE_PATH = "/Users/oven/logs/license/publicCerts.keystore"; // 密钥库存储路径

    // Win
    // public static final String LICENSE_PATH = "E:\\logs\\demo\\license\\license.lic"; // 证书生成路径
    // public static final String PUBLIC_KEYS_STORE_PATH = "E:\\logs\\demo\\license\\publicCerts.keystore"; // 密钥库存储路径

    public static void config(Properties properties) {
        String profile = String.valueOf(properties.get("demo.profile"));
        if (PropertyConfig.PRO_PROFILE.equals(profile)) {
            return;
        }
        // Mac
        properties.put("logging.config", "classpath:logback-dev.xml");
        properties.put("avatar.path", "/Users/oven/logs/demo/files/avatar/");

        // Win
        // properties.put("logging.config", "classpath:logback-dev.xml");
        // properties.put("avatar.path", "E:\\logs\\demo\\files\\avatar\\");
    }

}
