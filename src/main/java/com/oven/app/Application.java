package com.oven.app;

import com.oven.config.PropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Properties;

/**
 * 系统启动类
 *
 * @author Oven
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.oven")
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