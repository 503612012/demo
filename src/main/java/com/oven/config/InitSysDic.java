package com.oven.config;

import com.oven.constant.AppConst;
import com.oven.service.SysDicService;
import com.oven.vo.SysDicVo;
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
        log.info(AppConst.INFO_LOG_PREFIX + "开始加载系统字典" + AppConst.INFO_LOG_PREFIX);
        List<SysDicVo> list = sysDicService.findAll();
        ConcurrentMap<String, Object> map = new ConcurrentHashMap<>();
        for (SysDicVo item : list) {
            map.put(item.getKey(), item.getValue());
            SysDic.setSysDic(map);
        }
    }

}
