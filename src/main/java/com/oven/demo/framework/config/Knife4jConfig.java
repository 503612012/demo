package com.oven.demo.framework.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * knife4j配置
 *
 * @author Oven
 */
@EnableKnife4j
@Configuration
@EnableSwagger2
public class Knife4jConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("财务管理系统接口文档")
                .description("财务管理系统接口文档")
                .termsOfServiceUrl("http://localhost:8080")
                .contact(new Contact("Oven", "http://cloveaire.com", "503612012@qq.com"))
                .version("1.0.0")
                .build();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("demo")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.oven.demo"))
                .paths(PathSelectors.any())
                .build();
    }

}
