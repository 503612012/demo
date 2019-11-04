package com.oven.config;

import com.oven.thread.ExecLogThread;
import com.oven.util.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化队列线程开始工作
 *
 * @author Oven
 */
@Slf4j
@Component
public class InitThreadRun implements CommandLineRunner {

    @Override
    public void run(String... args) {
        ThreadPoolUtils.getInstance().execute(new ExecLogThread());
    }

}
