package com.skyer.app;

import com.skyer.config.PropertyConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * 系统启动类
 *
 * @author SKYER
 */
@SpringBootApplication(scanBasePackages = "com.skyer")
@MapperScan(basePackages = "com.skyer.mapper")
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