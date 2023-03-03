package com.oven.demo.framework.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.oven.demo.common.enumerate.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * knife4j配置
 *
 * @author Oven
 */
@Slf4j
@EnableKnife4j
@Configuration
@EnableSwagger2
public class Knife4jConfig {

    @Value("${vcas.profile}")
    private String profile;

    private ApiInfo apiInfo() {
        String version = getVersion();
        return new ApiInfoBuilder()
                .title("财务管理系统接口文档")
                .description("财务管理系统接口文档")
                .termsOfServiceUrl("http://localhost:8080")
                .contact(new Contact("Oven", "http://cloveaire.com", "503612012@qq.com"))
                .version(version)
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
                .build()
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.PUT, responseMessageList())
                .globalResponses(HttpMethod.GET, responseMessageList())
                .globalResponses(HttpMethod.POST, responseMessageList())
                .globalResponses(HttpMethod.HEAD, responseMessageList())
                .globalResponses(HttpMethod.PATCH, responseMessageList())
                .globalResponses(HttpMethod.TRACE, responseMessageList())
                .globalResponses(HttpMethod.DELETE, responseMessageList())
                .globalResponses(HttpMethod.OPTIONS, responseMessageList());
    }

    private List<Response> responseMessageList() {
        List<Response> list = new ArrayList<>();
        for (ResultEnum resultEnum : ResultEnum.values()) {
            list.add(new ResponseBuilder().code(String.valueOf(resultEnum.getCode())).description(resultEnum.getValue()).build());
        }
        return list;
    }

    public String getVersion() {
        try {
            if (PropertyConfig.DEV_PROFILE.equals(profile)) {
                MavenXpp3Reader reader = new MavenXpp3Reader();
                String path = System.getProperty("user.dir") + File.separator + "pom.xml";
                Model model = reader.read(new FileReader(path));
                return model.getVersion();
            }
            String version = this.getClass().getPackage().getImplementationVersion();
            log.info("获取到的version是：" + version);
            return version;
        } catch (Exception e) {
            log.error("获取maven版本号异常：", e);
            return null;
        }
    }

}
