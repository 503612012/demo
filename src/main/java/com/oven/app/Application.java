package com.oven.app;

import com.oven.config.PropertyConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * 系统启动类
 *
 * @author Oven
 */
@SpringBootApplication(scanBasePackages = "com.oven")
@MapperScan(basePackages = "com.oven.mapper")
public class Application {

    /**
     * 系统启动入口
     */
    public static void main(String[] args) {
        Properties properties = PropertyConfig.loadProperties();
        SpringApplication application = new SpringApplication(Application.class);
        application.setDefaultProperties(properties);
        System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
        application.run(args);
    }

}