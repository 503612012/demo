package com.oven.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 图片路径配置
 *
 * @author Oven
 */
@Configuration
@SuppressWarnings("deprecation")
public class ImagePathConfig extends WebMvcConfigurerAdapter {

    @Value("${avatar.path}")
    private String avatarPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:" + avatarPath);
    }

}
