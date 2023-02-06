package com.oven.demo.core.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@ApiIgnore
@RestController
public class VersionController {

    @GetMapping("/version")
    public String versionInformation() {
        return readGitProperties();
    }

    private String readGitProperties() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("git.properties");
        try {
            return readFromInputStream(inputStream);
        } catch (IOException e) {
            log.error("获取应用版本异常：", e);
            return "Version information could not be retrieved.";
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

}
