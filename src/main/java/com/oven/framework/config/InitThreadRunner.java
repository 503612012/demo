package com.oven.framework.config;

import com.oven.common.util.ThreadPoolUtils;
import com.oven.core.thread.ExecLogThread;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化队列线程开始工作
 *
 * @author Oven
 */
@Component
public class InitThreadRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        ThreadPoolUtils.getInstance().execute(new ExecLogThread());
    }

}
