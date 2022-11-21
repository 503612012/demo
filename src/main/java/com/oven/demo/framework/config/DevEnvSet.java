package com.oven.demo.framework.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.Profile;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;

/**
 * 开发环境配置
 *
 * @author Oven
 */
@Slf4j
public class DevEnvSet {

    /**
     * Win
     */
    private static void configWithWin(Properties properties) {
        properties.put("avatar.path", "E:\\logs\\demo\\avatar\\");
        properties.put("log.home", "E:\\logs\\demo\\logs\\");
    }

    /**
     * Mac
     */
    private static void configWithMac(Properties properties) {
        properties.put("avatar.path", "/Users/oven/logs/demo/avatar/");
        properties.put("log.home", "/Users/oven/logs/demo/logs/");
    }

    /**
     * 开发环境根据自己操作系统和相关文件路径修改一下配置
     */
    public static void config(Properties properties) {
        try {
            String profile = String.valueOf(properties.get("demo.profile"));
            if (PropertyConfig.PRO_PROFILE.equals(profile)) {
                return;
            }
            log.info("=========================== >>> 开始初始化开发环境配置");
            properties.put("logging.config", "classpath:logback-dev.xml");

            MavenXpp3Reader reader = new MavenXpp3Reader();
            String path = System.getProperty("user.dir") + File.separator + "pom.xml";
            Model model = reader.read(new FileReader(path));
            List<Profile> profiles = model.getProfiles();
            for (Profile platform : profiles) {
                if (!platform.getActivation().isActiveByDefault()) {
                    continue;
                }
                // Mac
                if (platform.getProperties().get("platform").toString().startsWith("m")) {
                    configWithMac(properties);
                }
                // Win
                if (platform.getProperties().get("platform").toString().startsWith("w")) {
                    configWithWin(properties);
                }
            }
            log.info("=========================== >>> 初始化开发环境配置完成");
        } catch (Exception e) {
            log.info("=========================== >>> 初始化开发环境配置异常：", e);
        }
    }

}
