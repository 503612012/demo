package com.oven.demo.framework.config;

import com.oven.demo.core.system.service.SysDicService;
import com.oven.demo.core.system.vo.SysDicEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 初始化系统级字典类
 *
 * @author Oven
 */
@Slf4j
@Component
public class InitSysDic implements CommandLineRunner {

    @Resource
    private SysDicService sysDicService;

    @Override
    public void run(String... args) {
        log.info("*************************** 开始加载系统字典 ***************************");
        List<SysDicEntity> list = sysDicService.findAll();
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
        for (SysDicEntity item : list) {
            map.put(item.getKey(), item.getValue());
            log.info("*************************** {} --- {}", item.getKey(), item.getValue());
        }
        SysDic.setSysDic(map);
        log.info("*************************** 系统字典加载完毕 ***************************");
    }

}
