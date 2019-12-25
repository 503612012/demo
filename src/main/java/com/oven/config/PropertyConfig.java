package com.oven.config;

import com.oven.constant.AppConst;
import lombok.extern.slf4j.Slf4j;

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

    private final static String DEV_PROFILE = "dev"; // 开发环境
    private final static String PRO_PROFILE = "pro"; // 生产环境
    private final static String PROFILE = "@profile@"; // 由于IDEA开发环境无法进行变量替换，故这里识别到占位符时，默认为开发环境


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
            log.error(AppConst.ERROR_LOG_PREFIX + "加载配置文件异常，异常信息：", e);
            return null;
        }
        String profile = properties.getProperty("spring.profiles.active");
        if (PROFILE.equals(profile)) {
            profile = DEV_PROFILE;
        }
        log.info(AppConst.INFO_LOG_PREFIX + "active profile is {}", profile);
        if (DEV_PROFILE.equals(profile)) {
            in = PropertyConfig.class.getClassLoader().getResourceAsStream("application-dev.properties");
        } else if (PRO_PROFILE.equals(profile)) {
            in = PropertyConfig.class.getClassLoader().getResourceAsStream("application-pro.properties");
        } else {
            log.error(AppConst.ERROR_LOG_PREFIX + "profile must be dev or pro!");
            return null;
        }
        try {
            properties.load(in);
        } catch (Exception e) {
            log.error(AppConst.ERROR_LOG_PREFIX + "加载配置文件异常，异常信息：", e);
            return null;
        }
        String driverClassName = properties.getProperty("spring.datasource.driver-class-name");
        String url = properties.getProperty("spring.datasource.url");
        String userName = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName(driverClassName);
            String tableName = "t_config";
            String sql = "select * from " + tableName;
            conn = DriverManager.getConnection(url, userName, password);
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            log.info(AppConst.INFO_LOG_PREFIX + "从数据库中加载配置信息...");
            while (rs.next()) {
                String key = rs.getString("key");
                String value = rs.getString("value");
                log.info(AppConst.INFO_LOG_PREFIX + " {} --- {}", key, value);
                properties.put(key, value);
            }
            return properties;
        } catch (Exception e) {
            log.error(AppConst.ERROR_LOG_PREFIX + "加载系统配置表异常，异常信息：", e);
            return null;
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
