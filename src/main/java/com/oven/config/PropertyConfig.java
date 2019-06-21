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

    /**
     * 生成Properties对象
     */
    public static Properties loadProperties() {
        Properties properties = new Properties();
        loadPropertiesFromDb(properties);
        return properties;
    }

    /**
     * 从数据库中加载配置信息
     */
    private static void loadPropertiesFromDb(Properties properties) {
        InputStream in = PropertyConfig.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String profile = properties.getProperty("profile");
        String driverClassName = properties.getProperty("spring.datasource.driver-class-name");
        String url = properties.getProperty("spring.datasource.url");
        String userName = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName(driverClassName);
            String tableName = "t_config_dev";
            if ("pro".equals(profile)) {
                tableName = "t_config_pro";
            }
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
        } catch (Exception e) {
            e.printStackTrace();
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
