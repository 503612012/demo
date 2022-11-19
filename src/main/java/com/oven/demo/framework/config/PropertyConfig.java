package com.oven.demo.framework.config;

import com.oven.basic.common.util.EncryptUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * 从数据库中加载配置文件
 *
 * @author Oven
 */
@Slf4j
public class PropertyConfig {

    public static final String PRO_PROFILE = "pro"; // 生产环境
    private static final String DEV_PROFILE = "dev"; // 开发环境
    private static final String PROFILE = "@profile@"; // 由于IDEA开发环境无法进行变量替换，故这里识别到占位符时，默认为开发环境

    /**
     * 生成Properties对象
     */
    public static Properties loadProperties() {
        return loadPropertiesFromDb();
    }

    /**
     * 从数据库中加载配置信息
     */
    private static Properties loadPropertiesFromDb() {
        Properties properties = new Properties();
        InputStream in = PropertyConfig.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(in);
        } catch (Exception e) {
            log.error("加载配置文件异常，异常信息：", e);
            return null;
        }
        String profile = properties.getProperty("spring.profiles.active");
        if (PROFILE.equals(profile)) {
            profile = DEV_PROFILE;
        }
        log.info("active profile is {}", profile);
        if (DEV_PROFILE.equals(profile)) {
            in = PropertyConfig.class.getClassLoader().getResourceAsStream("application-dev.properties");
        } else if (PRO_PROFILE.equals(profile)) {
            in = PropertyConfig.class.getClassLoader().getResourceAsStream("application-pro.properties");
        } else {
            log.error("profile must be dev or pro!");
            return null;
        }
        try {
            properties.load(in);
        } catch (Exception e) {
            log.error("加载配置文件异常，异常信息：", e);
            return null;
        }
        properties.put("demo.profile", profile);
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = System.getenv("db.url");
        if (StringUtils.isEmpty(url)) {
            url = properties.getProperty("mysql.url");
        } else {
            url = "jdbc:mysql://" + url + "/db_demo?characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        }
        String userName = System.getenv("db.uname");
        if (StringUtils.isEmpty(userName)) {
            userName = properties.getProperty("mysql.username");
        }
        String password = System.getenv("db.pwd");
        if (StringUtils.isEmpty(password)) {
            password = properties.getProperty("mysql.password");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            if (PRO_PROFILE.equals(profile)) { // 生产环境，数据库密码解密
                password = EncryptUtils.aesDecrypt(password, EncryptUtils.KEY);
            }
            properties.put("spring.datasource.druid.driver-class-name", driverClassName);
            properties.put("spring.datasource.druid.url", url);
            properties.put("spring.datasource.druid.username", userName);
            properties.put("spring.datasource.druid.password", password);

            Class.forName(driverClassName);
            String sql = "select * from t_config";
            conn = DriverManager.getConnection(url, userName, password);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            log.info("从数据库中加载配置信息...");
            while (rs.next()) {
                String key = rs.getString("key");
                String value = rs.getString("value");
                log.info("{} --- {}", key, value);
                properties.put(key, value);
            }
            return properties;
        } catch (Exception e) {
            log.error("加载系统配置表异常，异常信息：", e);
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("加载配置信息类异常：", e);
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    log.error("加载配置信息类异常：", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log.error("加载配置信息类异常：", e);
                }
            }
        }
    }

}
