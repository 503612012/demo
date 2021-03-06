package com.oven.app;

import com.oven.common.constant.AppConst;
import com.oven.framework.config.PropertyConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * 系统启动类
 *
 * @author Oven
 */
@Slf4j
@EnableScheduling
@EnableTransactionManagement
@SpringBootApplication(scanBasePackages = "com.oven")
public class Application {

    /**
     * 系统启动入口
     */
    public static void main(String[] args) {
        Properties properties = PropertyConfig.loadProperties();
        if (properties == null) {
            log.error(AppConst.ERROR_LOG_PREFIX + "load properties error...");
            return;
        }
        SpringApplication application = new SpringApplication(Application.class);
        application.setDefaultProperties(properties);
        System.setProperty("net.sf.ehcache.enableShutdownHook", "true");
        application.run(args);
    }

}